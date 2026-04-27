package com.service;

import java.util.List;

import com.entity.view.DecisionAdviceItemView;
import com.entity.view.DecisionDashboardSnapshotView;

public interface DecisionRuleService {

    List<DecisionAdviceItemView> generateAdvice(DecisionDashboardSnapshotView snapshot);
}
