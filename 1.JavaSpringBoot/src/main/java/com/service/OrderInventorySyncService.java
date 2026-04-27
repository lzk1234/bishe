package com.service;

import com.entity.OrdersEntity;

public interface OrderInventorySyncService {
    void syncOnOrderEffectiveStatusChange(OrdersEntity previousOrder, OrdersEntity currentOrder);
}
