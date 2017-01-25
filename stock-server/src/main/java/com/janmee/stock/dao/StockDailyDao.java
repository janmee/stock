package com.janmee.stock.dao;

import com.janmee.stock.entity.StockDaily;
import com.seewo.core.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface StockDailyDao extends BaseJpaRepository<StockDaily, String>, JpaSpecificationExecutor<StockDaily> {
    public List<StockDaily> findByDate(Date date);

    public List<StockDaily> findByDateAndStockSymbolIn(Date date, List<String> symbols);

    public List<StockDaily> findByDateBetweenOrderByDateDesc(Date startDate, Date endDate);

    @Query(value = "select * from stock_daily o where o.date = ?1 and o.stock_symbol in ?2 limit 3000",nativeQuery = true)
    public List<StockDaily> findByDateAndStockSymbolInNative(Date date, List<String> symbols);

    @Query(value = "select * from stock_daily o where o.date between ?1 and ?2 order by o.date desc limit 3000",nativeQuery = true)
    public List<StockDaily> findByDateBetweenOrderByDateDescNative(Date startDate, Date endDate);

    @Query(value = "select * from stock_daily o where o.date = ?1 and o.current > 1 limit ?2,?3",nativeQuery = true)
    public List<StockDaily> findByDateNative(Date date,int start,int end);

    @Query(value = "select count(*) from stock_daily o where o.date = ?1 and o.current < 1",nativeQuery = true)
    public int countByDate(Date date);
}
