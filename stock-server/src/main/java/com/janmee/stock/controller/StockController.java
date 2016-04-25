package com.janmee.stock.controller;

import com.seewo.core.base.Constants;
import com.seewo.core.base.DataMap;
import com.seewo.core.util.bean.BeanUtils;
import com.janmee.stock.base.StatusCode;
import com.janmee.stock.entity.Stock;
import com.janmee.stock.service.StockService;
import com.janmee.stock.vo.query.StockQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/api/v1/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    /**
     * 条件查询
     */
    @RequestMapping
    public DataMap showStocks(StockQuery query, @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Stock> page = stockService.findAll(query,pageable);
        return new DataMap().addAttribute(Constants.STATUS_CODE, StatusCode.SUCCESS.getStatusCode())
                .addAttribute(Constants.DATA, page);
    }

    /**
     * 单个查询
     */
    @RequestMapping(value = "/{id}")
    public DataMap showStock(@PathVariable String id) {
        DataMap dataMap = new DataMap();
        dataMap.addAttribute(Constants.STATUS_CODE, StatusCode.SUCCESS.getStatusCode());
        dataMap.addAttribute(Constants.DATA, stockService.findOne(id));
        return dataMap;
    }

    /**
     * 新增
     */
    @RequestMapping(method = RequestMethod.POST)
    public DataMap create(@RequestBody Stock stock) {
        DataMap dataMap = new DataMap();
        dataMap.addAttribute(Constants.DATA, stockService.create(stock));
        dataMap.addAttribute(Constants.STATUS_CODE, StatusCode.SUCCESS.getStatusCode());
        return dataMap;
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public DataMap update(@PathVariable String id, @RequestBody Stock stock) {
        DataMap dataMap = new DataMap();
        Stock persistStock = stockService.findOne(id);
        BeanUtils.copyProperties(stock, persistStock, "id", "id", "createdAt" ,"isDeleted");
        dataMap.addAttribute(Constants.DATA, stockService.update(persistStock));
        dataMap.addAttribute(Constants.STATUS_CODE, StatusCode.SUCCESS.getStatusCode());
        return dataMap;
    }

    /**
     * 删除
     */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public DataMap delete(@PathVariable String id) {
        DataMap dataMap = new DataMap();
        stockService.delete(id);
        dataMap.addAttribute(Constants.STATUS_CODE, StatusCode.SUCCESS.getStatusCode());
        return dataMap;
    }

}
