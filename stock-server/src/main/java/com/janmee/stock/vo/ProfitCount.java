package com.janmee.stock.vo;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author cvtpc_janmee on 2016/11/21.
 * @version 1.0
 */
public class ProfitCount {
    AtomicInteger gainCount = new AtomicInteger(0);
    AtomicInteger lossCount = new AtomicInteger(0);

    public AtomicInteger getGainCount() {
        return gainCount;
    }

    public void setGainCount(AtomicInteger gainCount) {
        this.gainCount = gainCount;
    }

    public AtomicInteger getLossCount() {
        return lossCount;
    }

    public void setLossCount(AtomicInteger lossCount) {
        this.lossCount = lossCount;
    }
}

