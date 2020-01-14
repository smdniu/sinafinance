package cn.mrsunflower.sinafinancecashfei.service.impl;

import cn.mrsunflower.sinafinancecashfei.service.PreWithdrawal;
import com.sinafinance.vo.BaseResponse;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class PreWithdrawalImpl implements PreWithdrawal {
    /**
     * 计算提现金额
     * @param withdrawApply
     * @param userId
     * @return
     * @throws Exception
     */
    public Map countWithdraw(long withdrawApply) throws Exception {
        //logger.info("WithdrawalController.countWithdraw.start");
        //提现申请总数
        BigDecimal withdrawApplyTotal = new BigDecimal(withdrawApply);

        Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
        //手续费用
        BigDecimal withdrawCharge = null;
        //提现实际总数
        BigDecimal withdrawRealityTotal = null;

        //计算手续费与实际提现金额
        //1500之内0.0055
        //1500之外0.007
        BigDecimal min = BigDecimal.valueOf(1500L);
        if ((withdrawApplyTotal.compareTo(min)) > 0) {
            // 计算提现手续费
            withdrawCharge = (new BigDecimal(2)
                    .add(withdrawApplyTotal.multiply(new BigDecimal(0.0055))))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
            // 计算实际提现金额
            withdrawRealityTotal = withdrawApplyTotal.subtract(withdrawCharge);
        } else {
            // 计算提现手续费
            withdrawCharge = withdrawApplyTotal.multiply(new BigDecimal(0.007)).setScale(2,
                    BigDecimal.ROUND_HALF_UP);                // 实际提现金额
            //计算实际提现金额
            withdrawRealityTotal = withdrawApplyTotal.subtract(withdrawCharge);
        }

        result.put("withdrawCharge", withdrawCharge);
        result.put("withdrawRealityTotal", withdrawRealityTotal);
        result.put("withdrawApplyTotal", withdrawApplyTotal);

        return result;
    }

    @Override
    public Boolean isRealName(long userid) {
        System.out.println("已经实名制");
        return true;
    }


}
