package com.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.entity.view.ForecastItemView;
import com.service.SalesForecastService;

@Service("salesForecastService")
public class SalesForecastServiceImpl implements SalesForecastService {

    private static final List<String> VALID_ORDER_STATUSES = Arrays.asList("已支付", "已发货", "已完成");
    private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<ForecastItemView> listForecasts(String enterpriseAccount) {
        List<Map<String, Object>> productRows = queryProductRows(enterpriseAccount);
        List<Map<String, Object>> salesRows = querySalesRows(enterpriseAccount);
        Map<String, int[]> salesMap = buildSalesMap(salesRows);

        Map<String, ForecastItemView> forecastMap = new LinkedHashMap<String, ForecastItemView>();
        for (Map<String, Object> row : productRows) {
            ForecastItemView item = buildForecastItem(row, salesMap);
            forecastMap.put(buildProductKey(item.getEnterpriseAccount(), item.getProductName()), item);
        }

        for (Map<String, Object> row : salesRows) {
            String enterprise = stringValue(row.get("enterpriseAccount"));
            String productName = stringValue(row.get("productName"));
            String key = buildProductKey(enterprise, productName);
            if (!forecastMap.containsKey(key)) {
                Map<String, Object> product = new LinkedHashMap<String, Object>();
                product.put("productName", productName);
                product.put("category", "未分类");
                product.put("brand", "平台精选");
                product.put("enterpriseAccount", enterprise);
                forecastMap.put(key, buildForecastItem(product, salesMap));
            }
        }

        List<ForecastItemView> result = new ArrayList<ForecastItemView>(forecastMap.values());
        Collections.sort(result, (left, right) -> {
            int compare = safeInt(right.getForecast7Days()) - safeInt(left.getForecast7Days());
            if (compare != 0) {
                return compare;
            }
            return safeInt(right.getRecentSales()) - safeInt(left.getRecentSales());
        });
        return result;
    }

    private List<Map<String, Object>> queryProductRows(String enterpriseAccount) {
        StringBuilder sql = new StringBuilder(
            "select shangpinmingcheng as productName, coalesce(shangpinfenlei,'未分类') as category, " +
            "coalesce(pinpai,'平台精选') as brand, zhanghao as enterpriseAccount from shangpinxinxi"
        );
        List<Object> args = new ArrayList<Object>();
        if (enterpriseAccount != null) {
            sql.append(" where zhanghao = ?");
            args.add(enterpriseAccount);
        }
        sql.append(" order by addtime desc");
        return jdbcTemplate.queryForList(sql.toString(), args.toArray());
    }

    private List<Map<String, Object>> querySalesRows(String enterpriseAccount) {
        LocalDate today = LocalDate.now();
        Timestamp startTime = Timestamp.valueOf(today.minusDays(6).atStartOfDay());
        Timestamp endTime = Timestamp.valueOf(today.plusDays(1).atStartOfDay());

        StringBuilder sql = new StringBuilder(
            "select goodname as productName, zhanghao as enterpriseAccount, DATE_FORMAT(addtime,'%Y-%m-%d') as dayKey, " +
            "coalesce(sum(buynumber),0) as sales from orders where status in (?,?,?) and addtime >= ? and addtime < ?"
        );
        List<Object> args = new ArrayList<Object>(VALID_ORDER_STATUSES);
        args.add(startTime);
        args.add(endTime);
        if (enterpriseAccount != null) {
            sql.append(" and zhanghao = ?");
            args.add(enterpriseAccount);
        }
        sql.append(" group by goodname, zhanghao, DATE_FORMAT(addtime,'%Y-%m-%d')");
        return jdbcTemplate.queryForList(sql.toString(), args.toArray());
    }

    private Map<String, int[]> buildSalesMap(List<Map<String, Object>> salesRows) {
        Map<String, int[]> salesMap = new LinkedHashMap<String, int[]>();
        LocalDate startDay = LocalDate.now().minusDays(6);
        for (Map<String, Object> row : salesRows) {
            String key = buildProductKey(stringValue(row.get("enterpriseAccount")), stringValue(row.get("productName")));
            int[] values = salesMap.containsKey(key) ? salesMap.get(key) : new int[7];
            int dayIndex = resolveDayIndex(stringValue(row.get("dayKey")), startDay);
            if (dayIndex >= 0 && dayIndex < 7) {
                values[dayIndex] = safeInt(row.get("sales"));
            }
            salesMap.put(key, values);
        }
        return salesMap;
    }

    private ForecastItemView buildForecastItem(Map<String, Object> row, Map<String, int[]> salesMap) {
        ForecastItemView item = new ForecastItemView();
        item.setProductName(stringValue(row.get("productName")));
        item.setCategory(stringValue(row.get("category")));
        item.setBrand(stringValue(row.get("brand")));
        item.setEnterpriseAccount(stringValue(row.get("enterpriseAccount")));

        int[] salesSeries = salesMap.getOrDefault(buildProductKey(item.getEnterpriseAccount(), item.getProductName()), new int[7]);
        int previousFourDays = sumSales(salesSeries, 0, 4);
        int recentThreeDays = sumSales(salesSeries, 4, 7);
        int recentSales = previousFourDays + recentThreeDays;

        BigDecimal previousAverage = divide(previousFourDays, 4);
        BigDecimal recentAverage = divide(recentThreeDays, 3);
        BigDecimal weightedDaily = recentAverage.multiply(new BigDecimal("0.60"))
            .add(previousAverage.multiply(new BigDecimal("0.40")))
            .setScale(2, RoundingMode.HALF_UP);

        item.setRecentSales(recentSales);
        item.setDailyAverage(weightedDaily);
        item.setForecast3Days(roundUp(weightedDaily.multiply(new BigDecimal("3"))));
        item.setForecast7Days(roundUp(weightedDaily.multiply(new BigDecimal("7"))));
        item.setTrend(resolveTrend(previousAverage, recentAverage));
        return item;
    }

    private int resolveDayIndex(String dayKey, LocalDate startDay) {
        if (dayKey == null || dayKey.trim().isEmpty()) {
            return -1;
        }
        LocalDate day = LocalDate.parse(dayKey, DAY_FORMATTER);
        return (int) (day.toEpochDay() - startDay.toEpochDay());
    }

    private int sumSales(int[] series, int start, int end) {
        int sum = 0;
        for (int index = start; index < end; index++) {
            sum += series[index];
        }
        return sum;
    }

    private BigDecimal divide(int value, int divisor) {
        if (value <= 0) {
            return new BigDecimal("0.00");
        }
        return BigDecimal.valueOf(value)
            .divide(BigDecimal.valueOf(divisor), 2, RoundingMode.HALF_UP);
    }

    private int roundUp(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }
        return value.setScale(0, RoundingMode.CEILING).intValue();
    }

    private String resolveTrend(BigDecimal previousAverage, BigDecimal recentAverage) {
        if (recentAverage.compareTo(BigDecimal.ZERO) == 0 && previousAverage.compareTo(BigDecimal.ZERO) == 0) {
            return "flat";
        }
        if (recentAverage.compareTo(previousAverage.multiply(new BigDecimal("1.10"))) > 0) {
            return "up";
        }
        if (previousAverage.compareTo(BigDecimal.ZERO) > 0
            && recentAverage.compareTo(previousAverage.multiply(new BigDecimal("0.90"))) < 0) {
            return "down";
        }
        return "flat";
    }

    private String buildProductKey(String enterpriseAccount, String productName) {
        return normalize(enterpriseAccount) + "::" + normalize(productName);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

    private String stringValue(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private int safeInt(Object value) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value == null) {
            return 0;
        }
        return Integer.parseInt(String.valueOf(value));
    }
}
