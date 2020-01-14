package com.sinafinance.service.impl;

import com.sinafinance.pojo.Deal;
import com.sinafinance.dao.mapper.DealMapper;
import com.sinafinance.exception.SinafinanceException;
import com.sinafinance.service.AccountService;
import com.sinafinance.service.DealService;
import com.sinafinance.utils.BeanUtils;
import com.sinafinance.utils.IdWorker;
import com.sinafinance.vo.CashOutRequest;
import com.sinafinance.vo.DealVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

@Service("dealService")
public class DealServiceImpl implements DealService {
    private Logger logger = LoggerFactory.getLogger(DealServiceImpl.class);
    @Autowired
    private IdWorker idWorker;

    @Autowired
    private DealMapper dealMapper;

    @Autowired
    private AccountService accountService;

    private static final BigDecimal serverRate = new BigDecimal(0.001);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addDeal(CashOutRequest request) {
        logger.info("本地账户处理提现请求，提现金额 [{}]",request.getMoney());
        //计算服务费 按照0.1%收取服务费
        BigDecimal serverMoney = request.getMoney().multiply(serverRate);
        BigDecimal totalMoney = request.getMoney().add(serverMoney);
        // 1.扣减本地账户
        if (!accountService.reduceAccount(request.getUid(),totalMoney)){
            logger.info("扣减本地账户余额出错，请稍后重试！");
            throw new RuntimeException("扣减本地账户余额出错，请稍后重试！");
        }

        // 2.保存交易
        Deal deal = new Deal();
        deal.setId(idWorker.nextId()+"");
        deal.setDealMoney(request.getMoney());
        deal.setServerMoney(serverMoney);
        deal.setDealState("1");//交易状态 1：发起提现 2：处理中 3：到账 4：提现失败 5：退回成功
        deal.setDealType("1"); //交易类型 1：提现 2：充值
        deal.setUid(request.getUid());
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR,2);
        deal.setCreateTime(date);
        deal.setUpdateTime(date);
        deal.setExpectTime(calendar.getTime());//预计到账时间
        deal.setDeleted(0); // 0:不删除 1：删除
        try {
            dealMapper.insert(deal);
        } catch (Exception e) {
            e.printStackTrace();
            //发送回滚消息
//            rabbitTemplate.convertAndSend("","queue.cashback", JSON.toJSONString(orderItemList));
            throw new SinafinanceException("创建交易明细失败");
        }

        return deal.getId();
    }

    @Override
    public DealVO findById(String id) {
        Deal deal = (Deal) dealMapper.selectByPrimaryKey(id);
        return BeanUtils.convert(deal,DealVO.class);
    }
}
