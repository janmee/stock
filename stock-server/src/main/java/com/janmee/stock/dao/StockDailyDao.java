package com.janmee.stock.dao;

import com.seewo.core.repository.BaseJpaRepository;
import com.janmee.stock.entity.StockDaily;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StockDailyDao extends BaseJpaRepository<StockDaily, String>, JpaSpecificationExecutor<StockDaily> {
}
