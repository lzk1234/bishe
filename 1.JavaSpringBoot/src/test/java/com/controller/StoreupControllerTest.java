package com.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.entity.ShangpinxinxiEntity;
import com.entity.StoreupEntity;
import com.service.ShangpinxinxiService;
import com.service.StoreupService;
import com.service.UserBehaviorService;
import com.utils.PageUtils;
import com.utils.R;

@ExtendWith(MockitoExtension.class)
public class StoreupControllerTest {

    @Mock
    private StoreupService storeupService;

    @Mock
    private ShangpinxinxiService shangpinxinxiService;

    @Mock
    private UserBehaviorService userBehaviorService;

    private StoreupController controller;

    @BeforeEach
    public void setUp() {
        controller = new StoreupController();
        ReflectionTestUtils.setField(controller, "storeupService", storeupService);
        ReflectionTestUtils.setField(controller, "shangpinxinxiService", shangpinxinxiService);
        ReflectionTestUtils.setField(controller, "userBehaviorService", userBehaviorService);
    }

    @Test
    public void listRemovesDeletedGoodsAndRefreshesFavoriteSnapshot() {
        StoreupEntity validFavorite = new StoreupEntity();
        validFavorite.setId(11L);
        validFavorite.setTablename("shangpinxinxi");
        validFavorite.setRefid(101L);
        validFavorite.setName("旧名称");
        validFavorite.setPicture("upload/old.jpg");

        StoreupEntity staleFavorite = new StoreupEntity();
        staleFavorite.setId(12L);
        staleFavorite.setTablename("shangpinxinxi");
        staleFavorite.setRefid(202L);
        staleFavorite.setName("已删除商品");
        staleFavorite.setPicture("upload/missing.jpg");

        List<StoreupEntity> favorites = new ArrayList<StoreupEntity>();
        favorites.add(validFavorite);
        favorites.add(staleFavorite);

        when(storeupService.queryPage(any(Map.class), any())).thenReturn(new PageUtils(favorites, 2, 10, 1));

        ShangpinxinxiEntity currentGoods = new ShangpinxinxiEntity();
        currentGoods.setId(101L);
        currentGoods.setShangpinmingcheng("高山茶4");
        currentGoods.setTupian("upload/gaoshancha4.jpg");
        when(shangpinxinxiService.selectBatchIds(Arrays.asList(101L, 202L))).thenReturn(Arrays.asList(currentGoods));

        R result = controller.list(new HashMap<String, Object>(), new StoreupEntity(), (HttpServletRequest) null);

        Object data = result.get("data");
        assertTrue(data instanceof PageUtils);
        PageUtils page = (PageUtils) data;
        assertEquals(1L, page.getTotal());
        assertEquals(1, page.getList().size());

        StoreupEntity refreshedFavorite = (StoreupEntity) page.getList().get(0);
        assertEquals("高山茶4", refreshedFavorite.getName());
        assertEquals("upload/gaoshancha4.jpg", refreshedFavorite.getPicture());
        assertFalse(refreshedFavorite.getId().equals(staleFavorite.getId()));

        verify(storeupService).deleteBatchIds(Arrays.asList(12L));
    }
}
