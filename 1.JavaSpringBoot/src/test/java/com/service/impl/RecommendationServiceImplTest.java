package com.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.dao.RecommendationDao;
import com.entity.RecommendationEntity;

@ExtendWith(MockitoExtension.class)
public class RecommendationServiceImplTest {

    @Mock
    private RecommendationDao recommendationDao;

    private RecommendationServiceImpl service;

    @BeforeEach
    public void setUp() {
        service = spy(new RecommendationServiceImpl());
        ReflectionTestUtils.setField(service, "baseMapper", recommendationDao);

        when(recommendationDao.deleteByUserid(7L)).thenReturn(0);
        when(recommendationDao.insert(any(RecommendationEntity.class))).thenReturn(1);
    }

    @Test
    public void generateRecommendationsUsesReadableChineseReason() {
        Map<Long, Double> itemScores = new LinkedHashMap<Long, Double>();
        itemScores.put(11L, 0.9D);

        doReturn(itemScores).when(service).collaborativeFilteringByItem(7L);
        doReturn(Collections.emptyMap()).when(service).collaborativeFilteringByUser(7L);
        doReturn(Collections.emptyMap()).when(service).contentBasedRecommendation(7L);
        doReturn(Collections.emptyMap()).when(service).hotRecommendation();

        service.generateRecommendations(7L);

        ArgumentCaptor<RecommendationEntity> captor = ArgumentCaptor.forClass(RecommendationEntity.class);
        verify(recommendationDao).insert(captor.capture());

        RecommendationEntity inserted = captor.getValue();
        assertEquals("\u7efc\u5408\u63a8\u8350\u7b2c1\u4f4d", inserted.getReason());
        assertFalse(inserted.getReason().contains("?"));
    }
}
