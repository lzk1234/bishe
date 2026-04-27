package com.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.entity.InventoryrecordEntity;
import com.entity.TokenEntity;
import com.interceptor.AuthorizationInterceptor;
import com.service.InventoryrecordService;
import com.service.TokenService;

@ExtendWith(MockitoExtension.class)
public class InventoryrecordControllerSecurityTest {

    @Mock
    private InventoryrecordService inventoryrecordService;

    @Mock
    private TokenService tokenService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        InventoryrecordController controller = new InventoryrecordController();
        ReflectionTestUtils.setField(controller, "inventoryrecordService", inventoryrecordService);

        AuthorizationInterceptor interceptor = new AuthorizationInterceptor();
        ReflectionTestUtils.setField(interceptor, "tokenService", tokenService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).addInterceptors(interceptor).build();
    }

    @Test
    public void listRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/inventoryrecord/list").header("Origin", "http://localhost"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void merchantCannotViewForeignInventoryRecord() throws Exception {
        when(tokenService.getTokenEntity("merchant-token")).thenReturn(createToken(1L, "merchantA", "shangjia", "商家"));
        when(inventoryrecordService.selectById(9L)).thenReturn(createEntity(9L, "merchantB"));

        mockMvc.perform(get("/inventoryrecord/info/9").header("Origin", "http://localhost").header("Token", "merchant-token"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(403));
    }

    @Test
    public void merchantCannotUpdateForeignInventoryRecord() throws Exception {
        when(tokenService.getTokenEntity("merchant-token")).thenReturn(createToken(1L, "merchantA", "shangjia", "商家"));
        when(inventoryrecordService.selectById(9L)).thenReturn(createEntity(9L, "merchantB"));

        mockMvc.perform(post("/inventoryrecord/update")
                .header("Origin", "http://localhost")
                .header("Token", "merchant-token")
                .contentType("application/json;charset=UTF-8")
                .content("{\"id\":9,\"enterpriseaccount\":\"merchantB\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(403));
    }

    @Test
    public void merchantCannotDeleteForeignInventoryRecord() throws Exception {
        when(tokenService.getTokenEntity("merchant-token")).thenReturn(createToken(1L, "merchantA", "shangjia", "商家"));
        when(inventoryrecordService.selectById(9L)).thenReturn(createEntity(9L, "merchantB"));

        mockMvc.perform(post("/inventoryrecord/delete")
                .header("Origin", "http://localhost")
                .header("Token", "merchant-token")
                .contentType("application/json;charset=UTF-8")
                .content("[9]"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(403));
    }

    @Test
    public void merchantCanViewOwnInventoryRecord() throws Exception {
        when(tokenService.getTokenEntity("merchant-token")).thenReturn(createToken(1L, "merchantA", "shangjia", "商家"));
        when(inventoryrecordService.selectById(9L)).thenReturn(createEntity(9L, "merchantA"));

        mockMvc.perform(get("/inventoryrecord/info/9").header("Origin", "http://localhost").header("Token", "merchant-token"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(0))
            .andExpect(jsonPath("$.data.enterpriseaccount").value("merchantA"));
    }

    private TokenEntity createToken(Long userId, String username, String tableName, String role) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUserid(userId);
        tokenEntity.setUsername(username);
        tokenEntity.setTablename(tableName);
        tokenEntity.setRole(role);
        tokenEntity.setToken(username + "-token");
        tokenEntity.setExpiratedtime(new Date(System.currentTimeMillis() + 60000));
        return tokenEntity;
    }

    private InventoryrecordEntity createEntity(Long id, String enterpriseAccount) {
        InventoryrecordEntity entity = new InventoryrecordEntity();
        entity.setId(id);
        entity.setEnterpriseaccount(enterpriseAccount);
        return entity;
    }
}
