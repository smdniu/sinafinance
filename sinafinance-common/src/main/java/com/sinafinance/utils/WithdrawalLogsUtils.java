package com.sinafinance.utils;

import com.sinafinance.enums.WithdrawalStateEnum;
import com.sinafinance.pojo.WithdrawalInfo;
import com.sinafinance.pojo.WithdrawalLogs;
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
