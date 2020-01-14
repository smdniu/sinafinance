package cn.mrsunflower.sinafinancecashfei.service;

import cn.mrsunflower.sinafinancecashfei.pojo.WithdrawalBank;

import java.util.List;

public interface WithdrawalBankService {
    /**
     * 返回可提现的银行列表
     * @param uid 用户ID
     * @param bankCode 银行的缩写,例如ICBC
     * @return
     */
     WithdrawalBank getWithdrawalBankByUidAndBankCode(Long uid, String bankCode);

    /**
     * 插入新帐户
     * @param withdrawalBank 银行卡实体类
     * @return
     */
     int insertWithdrawalBank(WithdrawalBank withdrawalBank);

    /**
     * 根据用户id返回银行卡号列表
     * @param userId
     * @return
     */
     List<WithdrawalBank> getWithdrawalBankByUid(Long userId);
}
