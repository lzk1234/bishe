package com.controller;

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

import com.entity.TeabaseEntity;
import com.entity.TokenEntity;
import com.interceptor.AuthorizationInterceptor;
import com.service.TeabaseService;
import com.service.TokenService;

@ExtendWith(MockitoExtension.class)
public class TeabaseControllerSecurityTest {

    @Mock
    private TeabaseService teabaseService;

    @Mock
    private TokenService tokenService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        TeabaseController controller = new TeabaseController();
        ReflectionTestUtils.setField(controller, "teabaseService", teabaseService);

        AuthorizationInterceptor interceptor = new AuthorizationInterceptor();
        ReflectionTestUtils.setField(interceptor, "tokenService", tokenService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).addInterceptors(interceptor).build();
    }

    @Test
    public void listRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/teabase/list").header("Origin", "http://localhost"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void merchantCannotViewForeignTeabase() throws Exception {
        when(tokenService.getTokenEntity("merchant-token")).thenReturn(createToken("merchantA"));
        when(teabaseService.selectById(7L)).thenReturn(createEntity(7L, "merchantB"));

        mockMvc.perform(get("/teabase/info/7").header("Origin", "http://localhost").header("Token", "merchant-token"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(403));
    }

    @Test
    public void merchantCannotUpdateForeignTeabase() throws Exception {
        when(tokenService.getTokenEntity("merchant-token")).thenReturn(createToken("merchantA"));
        when(teabaseService.selectById(7L)).thenReturn(createEntity(7L, "merchantB"));

        mockMvc.perform(post("/teabase/update")
                .header("Origin", "http://localhost")
                .header("Token", "merchant-token")
                .contentType("application/json;charset=UTF-8")
                .content("{\"id\":7,\"enterpriseaccount\":\"merchantB\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(403));
    }

    @Test
    public void merchantCannotDeleteForeignTeabase() throws Exception {
        when(tokenService.getTokenEntity("merchant-token")).thenReturn(createToken("merchantA"));
        when(teabaseService.selectById(7L)).thenReturn(createEntity(7L, "merchantB"));

        mockMvc.perform(post("/teabase/delete")
                .header("Origin", "http://localhost")
                .header("Token", "merchant-token")
                .contentType("application/json;charset=UTF-8")
                .content("[7]"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(403));
    }

    private TokenEntity createToken(String username) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUserid(1L);
        tokenEntity.setUsername(username);
        tokenEntity.setTablename("shangjia");
        tokenEntity.setRole("商家");
        tokenEntity.setToken("merchant-token");
        tokenEntity.setExpiratedtime(new Date(System.currentTimeMillis() + 60000));
        return tokenEntity;
    }

    private TeabaseEntity createEntity(Long id, String enterpriseAccount) {
        TeabaseEntity entity = new TeabaseEntity();
        entity.setId(id);
        entity.setEnterpriseaccount(enterpriseAccount);
        return entity;
    }
}
