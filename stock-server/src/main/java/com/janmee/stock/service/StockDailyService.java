package com.janmee.stock.service;

import com.janmee.stock.entity.StockDaily;
import com.janmee.stock.vo.query.StockDailyQuery;
import com.janmee.stock.vo.StragegyParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface StockDailyService {

    Page<StockDaily> findAll(Pageable pageable);

    Page<StockDaily> findAll(StockDailyQuery query, Pageable pageable);

    StockDaily findOne(String id);

    StockDaily create(StockDaily stockDaily);

    StockDaily update(StockDaily stockDaily);

    void delete(String id);

    List<String> findByStragegy(StragegyParam stragegyParam);

    public Map<String,List<String>> scanAllDate(StragegyParam stragegyParam);
}
