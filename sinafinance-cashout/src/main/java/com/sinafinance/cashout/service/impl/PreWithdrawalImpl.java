package com.sinafinance.cashout.service.impl;

import com.sinafinance.cashout.service.AccountService;
import com.sinafinance.cashout.service.PreWithdrawal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service("preWithdrawal")
public class PreWithdrawalImpl implements PreWithdrawal {
    @Autowired
    private AccountService accountService;

    /**
     * 计算提现金额
     * @param withdrawApply
     * @param userId
     * @return
     * @throws Exception
     */
    public Map countWithdraw(long withdrawApply,long userId) throws Exception {
        /**
         * 1.提现额度与总余额比较，如果全部提现，则服务费从提现金额中扣除，否则总共扣款为提现额+服务费
         * 2.费率为0.10%
         */
        //logger.info("WithdrawalController.countWithdraw.start");
        //提现申请总数
        BigDecimal withdrawApplyTotal = new BigDecimal(withdrawApply);

        Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
        //手续费用
        BigDecimal withdrawCharge = null;
        //提现实际总数
        BigDecimal withdrawRealityTotal = null;

        //查询账户余额
        BigDecimal account = accountService.findAccountByUid(userId);
        if(account != null){
            withdrawCharge = getWithdrawCharge(withdrawApplyTotal);
            // 全部提现
            if (account.compareTo(withdrawApplyTotal) == 0){
                withdrawApplyTotal = withdrawApplyTotal.divide(withdrawCharge);
            }
            withdrawRealityTotal = withdrawApplyTotal.subtract(withdrawCharge);
        }

        result.put("withdrawCharge", withdrawCharge);
        result.put("withdrawRealityTotal", withdrawRealityTotal);
        result.put("withdrawApplyTotal", withdrawApplyTotal);

        return result;
    }

    /**
     * 计算服务费
     * @param withdrawApplyTotal
     * @return
     */
    private BigDecimal getWithdrawCharge(BigDecimal withdrawApplyTotal) {
        //计算手续费与实际提现金额
        //1500之内0.0055
        //1500之外0.007
        BigDecimal min = BigDecimal.valueOf(1500L);
        BigDecimal withdrawCharge;
        if ((withdrawApplyTotal.compareTo(min)) > 0) {
            // 计算提现手续费
            withdrawCharge = (new BigDecimal(2)
                    .add(withdrawApplyTotal.multiply(new BigDecimal(0.0055))))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
            // 计算实际提现金额
        } else {
            // 计算提现手续费
            withdrawCharge = withdrawApplyTotal.multiply(new BigDecimal(0.007)).setScale(2,
                    BigDecimal.ROUND_HALF_UP);                // 实际提现金额
            //计算实际提现金额

        }
        return withdrawCharge;
    }

    @Override
    public Boolean isRealName(long userid) {
        System.out.println("已经实名制");
        return true;
    }


}
