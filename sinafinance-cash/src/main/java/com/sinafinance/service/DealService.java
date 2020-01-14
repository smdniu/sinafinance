package com.sinafinance.service;

import com.sinafinance.vo.CashOutRequest;
import com.sinafinance.vo.DealVO;

public interface DealService {
    String addDeal(CashOutRequest request);
    DealVO findById(String id);
}
