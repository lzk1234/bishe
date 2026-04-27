package com.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class QueryTest {

    @Test
    public void mapConstructorShouldAcceptNumericPagingParams() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("page", Integer.valueOf(2));
        params.put("limit", Integer.valueOf(15));

        Query<Object> query = new Query<Object>(params);

        assertEquals(2, query.getCurrPage());
        assertEquals(15, query.getLimit());
        assertEquals(15, query.get("offset"));
        assertEquals(2, query.get("page"));
        assertEquals(15, query.get("limit"));
    }
}
