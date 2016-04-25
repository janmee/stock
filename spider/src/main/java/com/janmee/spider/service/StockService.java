package com.janmee.spider.service;

import com.janmee.spider.dao.StockDao;
import com.janmee.spider.entity.Stock;
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
public class StockService {
    private static SqlSessionFactory sqlSessionFactory = null;
    private static StockDao stockDao = null;
    public StockService(){
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

    public void insertStock(Stock stock) {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            StockDao stockDao = session.getMapper(StockDao.class);
            stockDao.insert(stock);
            session.commit();
        }catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        }finally {
            session.close();
        }
    }

    public void insertBatch(List<Stock>stocks){
        SqlSession session = sqlSessionFactory.openSession();
        StockDao stockDao = session.getMapper(StockDao.class);
        try {
//            for (Stock stock : stocks){
//                stockDao.insert(stock);
//            }
            stockDao.insertBatch(stocks);
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
        StockDao stockDao = session.getMapper(StockDao.class);
        List<Stock>stocks = null;
        try {
            stocks = stockDao.selectAll();
            session.commit();
        }catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        }finally {
            session.close();
        }
        return stocks;
    }

    public Stock selectBySymbol(String symbol){
        SqlSession session = sqlSessionFactory.openSession();
        StockDao stockDao = session.getMapper(StockDao.class);
        Stock stocks = null;
        try {
            stocks = stockDao.selectBySymbol(symbol);
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




