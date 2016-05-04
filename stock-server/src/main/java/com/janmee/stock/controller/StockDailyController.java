package com.janmee.stock.controller;

import com.janmee.stock.base.StatusCode;
import com.janmee.stock.entity.StockDaily;
import com.janmee.stock.service.StockDailyService;
import com.janmee.stock.vo.query.StockDailyQuery;
import com.janmee.stock.vo.query.StragegyParam;
import com.seewo.core.base.Constants;
import com.seewo.core.base.DataMap;
import com.seewo.core.util.bean.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/stockDailys")
public class StockDailyController {

    @Autowired
    private StockDailyService stockDailyService;

    /**
     * 条件查询
     */
    @RequestMapping
    public DataMap showStockDailys(StockDailyQuery query, @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<StockDaily> page = stockDailyService.findAll(query, pageable);
        return new DataMap().addAttribute(Constants.STATUS_CODE, StatusCode.SUCCESS.getStatusCode())
                .addAttribute(Constants.DATA, page);
    }

    /**
     * 单个查询
     */
    @RequestMapping(value = "/{id}")
    public DataMap showStockDaily(@PathVariable String id) {
        DataMap dataMap = new DataMap();
        dataMap.addAttribute(Constants.STATUS_CODE, StatusCode.SUCCESS.getStatusCode());
        dataMap.addAttribute(Constants.DATA, stockDailyService.findOne(id));
        return dataMap;
    }

    /**
     * 新增
     */
    @RequestMapping(method = RequestMethod.POST)
    public DataMap create(@RequestBody StockDaily stockDaily) {
        DataMap dataMap = new DataMap();
        dataMap.addAttribute(Constants.DATA, stockDailyService.create(stockDaily));
        dataMap.addAttribute(Constants.STATUS_CODE, StatusCode.SUCCESS.getStatusCode());
        return dataMap;
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public DataMap update(@PathVariable String id, @RequestBody StockDaily stockDaily) {
        DataMap dataMap = new DataMap();
        StockDaily persistStockDaily = stockDailyService.findOne(id);
        BeanUtils.copyProperties(stockDaily, persistStockDaily, "id", "id", "createdAt", "isDeleted");
        dataMap.addAttribute(Constants.DATA, stockDailyService.update(persistStockDaily));
        dataMap.addAttribute(Constants.STATUS_CODE, StatusCode.SUCCESS.getStatusCode());
        return dataMap;
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public DataMap delete(@PathVariable String id) {
        DataMap dataMap = new DataMap();
        stockDailyService.delete(id);
        dataMap.addAttribute(Constants.STATUS_CODE, StatusCode.SUCCESS.getStatusCode());
        return dataMap;
    }

    /**
     * 策略查询
     */
    @RequestMapping(value = "/strategy")
    public DataMap findByStragegy(StragegyParam stragegyParam, @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return new DataMap().addAttribute(Constants.STATUS_CODE, StatusCode.SUCCESS.getStatusCode())
                .addAttribute(Constants.DATA, stockDailyService.findByStragegy(stragegyParam));
    }

}
