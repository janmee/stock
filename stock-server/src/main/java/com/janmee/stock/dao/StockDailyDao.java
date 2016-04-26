package com.janmee.stock.dao;

import com.seewo.core.repository.BaseJpaRepository;
import com.janmee.stock.entity.StockDaily;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

public interface StockDailyDao extends BaseJpaRepository<StockDaily, String>, JpaSpecificationExecutor<StockDaily> {
    public List<StockDaily>findByDate(Date date);
    public List<StockDaily>findByDateBetween(Date startDate,Date endDate);
}
