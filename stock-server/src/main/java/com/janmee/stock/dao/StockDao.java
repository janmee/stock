package com.janmee.stock.dao;

import com.seewo.core.repository.BaseJpaRepository;
import com.janmee.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StockDao extends BaseJpaRepository<Stock, String>, JpaSpecificationExecutor<Stock> {
}
