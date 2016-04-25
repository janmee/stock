package com.janmee.stock.service;

import com.janmee.stock.entity.StockDaily;
import com.janmee.stock.vo.query.StockDailyQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StockDailyService {

	Page<StockDaily> findAll(Pageable pageable);

	Page<StockDaily> findAll(StockDailyQuery query, Pageable pageable);

	StockDaily findOne(String id);

	StockDaily create(StockDaily stockDaily);

	StockDaily update(StockDaily stockDaily);

	void delete(String id);
}
