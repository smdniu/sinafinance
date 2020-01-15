package cn.mrsunflower.sinafinancecashfei.controller;

import cn.mrsunflower.sinafinancecashfei.pojo.WithdrawalBank;
import cn.mrsunflower.sinafinancecashfei.service.impl.WithdrawalBankServiceImpl;
import com.sinafinance.enums.ResponseCode;
import com.sinafinance.vo.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.List;

/**
 * 添加银行信息
 */
@RestController
public class WithdrawalBankController {
    @Autowired
    private WithdrawalBankServiceImpl withdrawalBankService;

    /**
     * 添加提现帐户
     */
    @PostMapping(value = "/withdrawalBank/add")
    public BaseResponse addWithdrawalBank(HttpServletRequest request, HttpServletResponse response, @RequestBody WithdrawalBank withdrawalBank) {
        try {
            if (withdrawalBank == null) {
                //假如传入对象有误的code是50001
                return new BaseResponse().newFailResponse(ResponseCode.OBJECT_CONVERSION_ERROR.getCode(), "传入对象有误");
            }
            Long uid = withdrawalBank.getUid();
            String bankCode = withdrawalBank.getBankCode();
            if (uid == null) {
                //假如传入参数错误的code是50002
                return new BaseResponse().newFailResponse(ResponseCode.PARAMETER_ERROR.getCode(), "参数有误");
            }
            //拿到当前银行卡的唯一编号
            WithdrawalBank dbWithdrawalBank = withdrawalBankService.getWithdrawalBankByUidAndBankCode(uid, bankCode);
            if (dbWithdrawalBank != null) {
                //假如卡已存在的code是50003
                return BaseResponse.newFailResponse(ResponseCode.EXISTING_ERROR.getCode(), "卡已存在,请重试");
            }

            //添加默认项
            withdrawalBank.setCreateTime(new Timestamp(System.currentTimeMillis()));
            withdrawalBank.setId(null);

            int result = withdrawalBankService.insertWithdrawalBank(withdrawalBank);
            if (result > 0) {
                //假如成功的状态码为20000
                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setRespCode("20000");
                baseResponse.setRespMsg("添加用户银行成功");
                return baseResponse;
            }
            System.out.println(result);
            //假如添加用户银行失败的状态码是50004
            return new BaseResponse().newFailResponse(ResponseCode.ERROR.getCode(), "添加用户银行失败");

        } catch (Exception e) {
            e.printStackTrace();
            //logger.error("[WithdrawalBankController][addWithdrawalBank] exception :", e);
            return new BaseResponse().newFailResponse(ResponseCode.SYSTEM_ERROR.getCode(), "系统错误,请重试");

        }
    }

    /**
     * 可提现银行卡列表查询
     * @param request
     * @param response
     * @param userId
     * @return
     */
    @RequestMapping(value = "/withdrawalBank/list", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse withdrawalBankList(HttpServletRequest request, HttpServletResponse response, Long userId ){
        try {
            List<WithdrawalBank> withdrawalBankList = withdrawalBankService.getWithdrawalBankByUid(userId);
            if (CollectionUtils.isEmpty(withdrawalBankList)) {
                return new BaseResponse().newFailResponse(ResponseCode.ERROR.getCode(), "用户未绑定银行卡");
            }
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setResult(withdrawalBankList);
            baseResponse.setRespCode(ResponseCode.SUCCESS.getCode());
            baseResponse.setRespMsg("查询用户银行卡信息");
            return baseResponse;
        } catch (Exception ex) {
            //logger.error("[WithdrawalBankController][withdrawalBankList] exception :", ex);
            return new BaseResponse().newFailResponse(ResponseCode.SYSTEM_ERROR.getCode(),"系统错误,请稍后重试");
        }
    }
}
