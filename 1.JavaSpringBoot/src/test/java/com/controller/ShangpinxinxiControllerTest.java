package com.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.service.OrdersService;
import com.service.ShangpinxinxiService;
import com.service.StoreupService;
import com.service.TokenService;
import com.service.UserBehaviorService;
import com.utils.R;

@ExtendWith(MockitoExtension.class)
public class ShangpinxinxiControllerTest {

    @Mock
    private ShangpinxinxiService shangpinxinxiService;

    @Mock
    private StoreupService storeupService;

    @Mock
    private OrdersService ordersService;

    @Mock
    private TokenService tokenService;

    @Mock
    private UserBehaviorService userBehaviorService;

    private ShangpinxinxiController controller;

    @BeforeEach
    public void setUp() {
        controller = new ShangpinxinxiController();
        ReflectionTestUtils.setField(controller, "shangpinxinxiService", shangpinxinxiService);
        ReflectionTestUtils.setField(controller, "storeupService", storeupService);
        ReflectionTestUtils.setField(controller, "ordersService", ordersService);
        ReflectionTestUtils.setField(controller, "tokenService", tokenService);
        ReflectionTestUtils.setField(controller, "userBehaviorService", userBehaviorService);
    }

    @Test
    public void deleteAlsoRemovesRelatedFavorites() {
        Long[] ids = new Long[] { 101L, 202L };

        R result = controller.delete(ids);

        assertEquals(0, result.get("code"));
        verify(shangpinxinxiService).deleteBatchIds(java.util.Arrays.asList(ids));
        verify(storeupService).delete(any());
    }
}
