package com.janmee.stock.base;

import com.seewo.core.base.DataMap;

/**
 * Created by Administrator on 2017/4/3.
 */
public class DataMapBuilder extends DataMap {
    private DataMap dataMap = new DataMap();

    public DataMap success() {
        return dataMap.addAttribute(com.seewo.core.base.Constants.STATUS_CODE, StatusCode.SUCCESS.getStatusCode());
    }
}
