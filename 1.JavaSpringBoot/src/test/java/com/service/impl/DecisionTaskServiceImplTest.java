package com.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.dao.DecisionTaskDao;
import com.entity.DecisionTaskEntity;

@ExtendWith(MockitoExtension.class)
public class DecisionTaskServiceImplTest {

    @Mock
    private DecisionTaskDao decisionTaskDao;

    private DecisionTaskServiceImpl service;

    @BeforeEach
    public void setUp() {
        service = new DecisionTaskServiceImpl();
        ReflectionTestUtils.setField(service, "baseMapper", decisionTaskDao);
    }

    @Test
    public void completeTaskRequiresResultNote() {
        IllegalArgumentException error = assertThrows(
            IllegalArgumentException.class,
            () -> service.completeTask(1L, "merchantA", false, " ")
        );

        assertEquals("处理说明不能为空", error.getMessage());
    }

    @Test
    public void ignoreTaskPersistsIgnoreReason() {
        DecisionTaskEntity task = new DecisionTaskEntity();
        task.setId(2L);
        task.setMerchantAccount("merchantA");
        task.setStatus("todo");
        task.setDueAt(new Date(System.currentTimeMillis() - 1000L));

        when(decisionTaskDao.selectById(2L)).thenReturn(task);
        when(decisionTaskDao.updateById(any(DecisionTaskEntity.class))).thenReturn(1);

        service.ignoreTask(2L, "merchantA", false, "当前不做促销");

        verify(decisionTaskDao).updateById(any(DecisionTaskEntity.class));
        assertEquals("ignored", task.getStatus());
        assertEquals("当前不做促销", task.getIgnoreReason());
    }
}
