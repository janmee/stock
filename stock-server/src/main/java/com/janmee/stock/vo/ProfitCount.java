package com.janmee.stock.vo;

import org.springframework.data.redis.support.atomic.RedisAtomicDouble;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author cvtpc_janmee on 2016/11/21.
 * @version 1.0
 */
public class ProfitCount {
    private AtomicInteger gainCount = new AtomicInteger(0);
    private AtomicInteger lossCount = new AtomicInteger(0);
    //最大收益
    private AtomicInteger maxProfit = new AtomicInteger(0);
    //最大亏损
    private AtomicInteger maxLoss = new AtomicInteger(0);

    //总收益
    private AtomicInteger totalProfit = new AtomicInteger(0);
    //总亏损
    private AtomicInteger totalLoss = new AtomicInteger(0);

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

    public AtomicInteger getMaxProfit() {
        return maxProfit;
    }

    public void setMaxProfit(AtomicInteger maxProfit) {
        this.maxProfit = maxProfit;
    }

    public AtomicInteger getMaxLoss() {
        return maxLoss;
    }

    public void setMaxLoss(AtomicInteger maxLoss) {
        this.maxLoss = maxLoss;
    }

    public AtomicInteger getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(AtomicInteger totalProfit) {
        this.totalProfit = totalProfit;
    }

    public AtomicInteger getTotalLoss() {
        return totalLoss;
    }

    public void setTotalLoss(AtomicInteger totalLoss) {
        this.totalLoss = totalLoss;
    }
}

