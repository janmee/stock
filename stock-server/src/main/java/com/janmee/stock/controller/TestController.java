package com.janmee.stock.controller;

import com.janmee.stock.base.StatusCode;
import com.janmee.stock.entity.Test;
import com.janmee.stock.service.TestService;
import com.seewo.core.base.Constants;
import com.seewo.core.base.DataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luojianming on 2016/4/23.
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/test")
public class TestController {
    @Autowired
    private TestService testService;

    @RequestMapping(method = RequestMethod.POST)
    public DataMap create(@RequestBody Test test){
        DataMap dataMap = new DataMap();
        test.setName("test");
        dataMap.addAttribute(Constants.DATA, testService.create(test));
        dataMap.addAttribute(Constants.STATUS_CODE, StatusCode.SUCCESS.getStatusCode());
        return dataMap;
    }
}
