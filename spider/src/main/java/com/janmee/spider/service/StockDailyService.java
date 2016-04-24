package com.janmee.spider.service;

import com.janmee.spider.dao.StockDailyDao;
import com.janmee.spider.entity.Stock;
import com.janmee.spider.entity.StockDaily;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Administrator on 2016/4/24.
 */
public class StockDailyService {
    private static SqlSessionFactory sqlSessionFactory = null;
    private static StockDailyDao stockDailyDao = null;
    public StockDailyService(){
        this.init();
    }

    public void init() {
        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insertStock(StockDaily stockDaily) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            StockDailyDao stockDailyDao = session.getMapper(StockDailyDao.class);
            stockDailyDao.insert(stockDaily);
            session.commit();
        }catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        }finally {
            session.close();
        }
    }

    public void insertBatch(List<StockDaily>stockDailies,Integer stockId){
        SqlSession session = sqlSessionFactory.openSession();
        StockDailyDao stockDailyDao = session.getMapper(StockDailyDao.class);
        for (StockDaily stockDaily : stockDailies){
            stockDaily.setStockId(stockId);
        }
        try {
            stockDailyDao.insertBatch(stockDailies);
            session.commit();
        }catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        }finally {
            session.close();
        }
    }

    public List<Stock> selectAll(){
        SqlSession session = sqlSessionFactory.openSession();
        StockDailyDao stockDailyDao = session.getMapper(StockDailyDao.class);
        List<Stock>stocks = null;
        try {
//            stocks = stockDailyDao.selectAll();
            session.commit();
        }catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        }finally {
            session.close();
        }
        return stocks;
    }


}




