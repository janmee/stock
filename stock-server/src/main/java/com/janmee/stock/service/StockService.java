package com.janmee.stock.service;

import com.janmee.stock.entity.Stock;
import com.janmee.stock.vo.query.StockQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StockService {

	Page<Stock> findAll(Pageable pageable);

	Page<Stock> findAll(StockQuery query, Pageable pageable);

	Stock findOne(String id);

	Stock create(Stock stock);

	Stock update(Stock stock);

	void delete(String id);
}
