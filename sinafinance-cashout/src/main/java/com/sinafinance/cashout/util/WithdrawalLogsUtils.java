package com.sinafinance.cashout.util;

import com.sinafinance.cashout.pojo.WithdrawalInfo;
import com.sinafinance.cashout.pojo.WithdrawalLogs;
import com.sinafinance.enums.WithdrawalStateEnum;
import com.sinafinance.vo.CashOutRequest;

public class WithdrawalLogsUtils {

    public static WithdrawalLogs creatWithdrawalLogs(final WithdrawalInfo withdrawal, final CashOutRequest request) {
        return new WithdrawalLogs()
                .setId(System.currentTimeMillis())
                .setWithdrawId(withdrawal.getId())
                .setStatus(WithdrawalStateEnum.APPLY_WITHDRAWAL.getCode())
                .setOperater(request.getUid())

                .setRemark("提现已提交,审核中!");

    }
}
