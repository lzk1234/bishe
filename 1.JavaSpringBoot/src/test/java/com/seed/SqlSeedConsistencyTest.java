package com.seed;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

public class SqlSeedConsistencyTest {

    @Test
    public void mainSqlUsesCanonicalMerchantAccountsAcrossArchiveTables() throws IOException {
        String sql = readSql(Paths.get("..", "4.MysqlDatabase", "springbooty2rp6.sql"));

        Set<String> merchantAccounts = columnValues(extractRows(sql, "shangjia"), 2);
        assertFalse(merchantAccounts.isEmpty());
        assertFalse(merchantAccounts.contains("1"), "商家账号基线不应再混用纯数字账号");
        assertTrue(
            merchantAccounts.stream().allMatch(account -> account.matches("账号\\d+")),
            "商家账号必须统一为 账号N 格式"
        );

        assertAllAccountsExist(columnValues(extractRows(sql, "shangpinxinxi"), 6), merchantAccounts, "茶品档案账号");
        assertAllAccountsExist(columnValues(extractRows(sql, "orders"), 20), merchantAccounts, "订单账号");
        assertAllAccountsExist(columnValues(extractRows(sql, "teabase"), 10), merchantAccounts, "茶园基地账号");
        assertAllAccountsExist(columnValues(extractRows(sql, "teabatch"), 11), merchantAccounts, "生产批次账号");
        assertAllAccountsExist(columnValues(extractRows(sql, "inventoryrecord"), 10), merchantAccounts, "库存台账账号");
        assertAllAccountsExist(columnValues(extractRows(sql, "production_sales_plan"), 3), merchantAccounts, "产销计划账号");
        assertAllAccountsExist(columnValues(extractRows(sql, "warehouse_info"), 7), merchantAccounts, "仓库档案账号");
        assertAllAccountsExist(columnValues(extractRows(sql, "inventory_transfer_suggestion"), 1), merchantAccounts, "库存调拨建议账号");
        assertAllAccountsExist(columnValues(extractRows(sql, "product_lifecycle_rule"), 1), merchantAccounts, "生命周期规则账号");
    }

    @Test
    public void mainSqlKeepsEnterpriseEightConnectedAcrossArchiveAndSupplyChainData() throws IOException {
        String sql = readSql(Paths.get("..", "4.MysqlDatabase", "springbooty2rp6.sql"));

        assertTrue(columnValues(extractRows(sql, "shangjia"), 2).contains("账号8"), "商家档案中应存在 账号8");
        assertTrue(columnValues(extractRows(sql, "shangpinxinxi"), 6).contains("账号8"), "茶品档案中应存在 账号8 归属记录");
        assertTrue(columnValues(extractRows(sql, "orders"), 20).contains("账号8"), "订单中应存在 账号8 归属记录");
        assertTrue(columnValues(extractRows(sql, "teabase"), 10).contains("账号8"), "茶园基地中应存在 账号8 归属记录");
        assertTrue(columnValues(extractRows(sql, "teabatch"), 11).contains("账号8"), "生产批次中应存在 账号8 归属记录");
        assertTrue(columnValues(extractRows(sql, "inventoryrecord"), 10).contains("账号8"), "库存台账中应存在 账号8 归属记录");
        assertTrue(columnValues(extractRows(sql, "production_sales_plan"), 3).contains("账号8"), "产销计划中应存在 账号8 归属记录");
        assertTrue(columnValues(extractRows(sql, "warehouse_info"), 7).contains("账号8"), "仓库档案中应存在 账号8 归属记录");
        assertTrue(columnValues(extractRows(sql, "product_lifecycle_rule"), 1).contains("账号8"), "生命周期规则中应存在 账号8 归属记录");
    }

    @Test
    public void supplyChainPatchUsesCanonicalMerchantAccounts() throws IOException {
        String sql = readSql(Paths.get("..", "run-logs", "supply-chain-import-fixed.sql"));

        assertFalse(columnValues(extractRows(sql, "teabase"), 10).contains("1"), "补丁脚本不应再使用纯数字企业账号");
        assertFalse(columnValues(extractRows(sql, "teabatch"), 11).contains("1"), "补丁脚本不应再使用纯数字企业账号");
        assertFalse(columnValues(extractRows(sql, "inventoryrecord"), 8).contains("1"), "补丁脚本不应再使用纯数字企业账号");
    }

    @Test
    public void incrementalSeedScriptContainsExpectedDemoMarkers() throws IOException {
        String sql = readSql(Paths.get("..", "4.MysqlDatabase", "demo_incremental_seed.sql"));

        assertTrue(sql.contains("demo_user_01"));
        assertTrue(sql.contains("DEMOORD20260425"));
        assertTrue(sql.contains("PC202604-B08"));
        assertTrue(sql.contains("recent 30d preference boost"));
    }

    private void assertAllAccountsExist(Set<String> actualAccounts, Set<String> merchantAccounts, String label) {
        Set<String> invalidAccounts = actualAccounts.stream()
            .filter(account -> account != null && !account.isEmpty())
            .filter(account -> !merchantAccounts.contains(account))
            .collect(Collectors.toCollection(LinkedHashSet::new));
        assertTrue(invalidAccounts.isEmpty(), label + "存在未在商家档案中定义的账号: " + invalidAccounts);
    }

    private Set<String> columnValues(List<List<String>> rows, int columnIndex) {
        Set<String> values = new LinkedHashSet<String>();
        for (List<String> row : rows) {
            if (row.size() > columnIndex) {
                String value = row.get(columnIndex);
                if (value != null && !value.isEmpty() && !"NULL".equalsIgnoreCase(value)) {
                    values.add(value);
                }
            }
        }
        return values;
    }

    private List<List<String>> extractRows(String sql, String tableName) {
        Pattern pattern = Pattern.compile(
            "INSERT INTO `" + Pattern.quote(tableName) + "`(?:\\s*\\([^;]*?\\))?\\s*VALUES\\s*(.*?);",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
        );
        Matcher matcher = pattern.matcher(sql);
        List<List<String>> rows = new ArrayList<List<String>>();
        while (matcher.find()) {
            rows.addAll(parseTuples(matcher.group(1)));
        }
        return rows;
    }

    private List<List<String>> parseTuples(String valuesBlock) {
        List<List<String>> rows = new ArrayList<List<String>>();
        boolean inString = false;
        int tupleStart = -1;

        for (int index = 0; index < valuesBlock.length(); index += 1) {
            char current = valuesBlock.charAt(index);
            if (current == '\'') {
                if (inString && index + 1 < valuesBlock.length() && valuesBlock.charAt(index + 1) == '\'') {
                    index += 1;
                    continue;
                }
                inString = !inString;
            }
            if (!inString && current == '(') {
                tupleStart = index + 1;
            } else if (!inString && current == ')' && tupleStart >= 0) {
                rows.add(splitColumns(valuesBlock.substring(tupleStart, index)));
                tupleStart = -1;
            }
        }
        return rows;
    }

    private List<String> splitColumns(String tupleContent) {
        List<String> columns = new ArrayList<String>();
        StringBuilder current = new StringBuilder();
        boolean inString = false;

        for (int index = 0; index < tupleContent.length(); index += 1) {
            char ch = tupleContent.charAt(index);
            if (ch == '\'') {
                if (inString && index + 1 < tupleContent.length() && tupleContent.charAt(index + 1) == '\'') {
                    current.append('\'');
                    index += 1;
                    continue;
                }
                inString = !inString;
                continue;
            }
            if (!inString && ch == ',') {
                columns.add(cleanValue(current.toString()));
                current.setLength(0);
                continue;
            }
            current.append(ch);
        }
        columns.add(cleanValue(current.toString()));
        return columns;
    }

    private String cleanValue(String rawValue) {
        return rawValue == null ? "" : rawValue.trim();
    }

    private String readSql(Path path) throws IOException {
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }
}
