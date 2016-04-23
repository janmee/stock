package com.janmee.stock.service.impl;

import com.janmee.stock.dao.TestDao;
import com.janmee.stock.entity.Test;
import com.janmee.stock.service.TestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * @author luojianming on 2016/4/23.
 * @version 1.0
 */
@Service
@Transactional
public class TestServiceImpl implements TestService{
    @Resource
    private TestDao testMapper;

    @Override
    public Test create(Test test) {
        testMapper.insert(test);
        return test;
    }
}
