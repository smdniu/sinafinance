package cn.mrsunflower.sinafinancecashfei.service;

import java.util.Map;

public interface PreWithdrawal {
    /**
     * 计算可提现金额
     * @param withdrawApply
     * @param userId 用户id
     * @return
     * @throws Exception
     */
     Map countWithdraw(long withdrawApply,long userId) throws Exception;

    /**
     * 是否实名制
     * @param userid
     * @return
     */
     Boolean isRealName(long userid);


}
