package com.sinafinance.cashout.controller;

import com.sinafinance.annotation.LoggerOut;
import com.sinafinance.pojo.WithdrawalInfo;
import com.sinafinance.cashout.service.AlipayService;
import com.sinafinance.cashout.service.impl.PreWithdrawalImpl;
import com.sinafinance.cashout.service.impl.WithdrawalServiceImpl;
import com.sinafinance.enums.ResponseCode;
import com.sinafinance.vo.BaseResponse;
import com.sinafinance.vo.CashOutRequest;
import com.sinafinance.vo.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @description: 用户提现
 * @author: sunmengdi
 * @time: 2020/1/7 14:06
 */
@RestController
public class WithdrawalController {
    @Autowired
    private PreWithdrawalImpl preWithdrawalImpl;

    @Autowired
    private WithdrawalServiceImpl withdrawalService;

    @Autowired
    private AlipayService alipayService;

//    @LoggerOut
//    @RequestMapping("/test")
//    public BaseResponse test(@RequestBody CashOutRequest request, Authentication user){
//        String uid = user.getName();
//        return BaseResponse.newSuccResponse(uid);
//
//    }

    /**
     * 提现金额计算
     */
    @LoggerOut
    @GetMapping(value = "/withdrawal/count")
    public BaseResponse countWithdraw(HttpServletRequest request, HttpServletResponse response,
                                      long withdrawApply, Long userId) {
        //logger.info("WithdrawalController.countWithdraw.start");
        try {
            Map map = preWithdrawalImpl.countWithdraw(withdrawApply,userId);

            //创建结果返回值
            BaseResponse baseResponse = new BaseResponse<>();
            baseResponse.setRespCode("20000");
            baseResponse.setRespMsg("计算成功");
            baseResponse.setResult(map);

            return baseResponse;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseResponse().newFailResponse("50005", "系统错误,请重试");
        }
    }

    /**
     * 申请提现接口
     *
     * @param request 提现的一些基本信息
     * @return
     */
    @LoggerOut
    @PostMapping(value = "/withdrawal/apply")
    public BaseResponse applyWithDrawal(@RequestBody CashOutRequest request) {

        /*uid,用户id,可以改成从token中获取*/
//        String uid = user.getName();
        String uid = "13426009535";
        try {
            // 验证参数
            if (uid == null) {
                return new BaseResponse().newFailResponse(ResponseCode.PARAMETER_ERROR.getCode(), "参数错误");
            }
            request.setUid(uid);
            BaseResponse baseResponse = withdrawalService.cashOut(request);
            return baseResponse;
        } catch (Exception ex) {
            ex.printStackTrace();
            //logger.error("[WithdrawalController][applyWithDrawal]exception ", ex);
            return new BaseResponse().newFailResponse(ResponseCode.SYSTEM_ERROR.getCode(), "申请失败，系统异常，请稍后再试");
        }
    }

    /**
     * 分页查询提现信息
     */
    @LoggerOut
    @GetMapping(value = "/withdrawal/applyList")
    public BaseResponse applyWithDrawalList( Long userId, int currentPageNum, int currentPageSize) {
        //logger.info("WithdrawalController.applyWithDrawalList.start");
        try {
            if (null == userId) {
                return new BaseResponse().newFailResponse(ResponseCode.SYSTEM_ERROR.getCode(), "系统错误,请重试");
            }
            PageResult pageResult = withdrawalService.applyWithDrawalList(userId, currentPageNum, currentPageSize);

            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setRespCode(ResponseCode.SUCCESS.getCode());
            baseResponse.setRespMsg("success");
            baseResponse.setResult(pageResult);
            return baseResponse;


        } catch (Exception ex) {
            //logger.error("[WithdrawalController][applyWithDrawalList]exception ", ex);
            return new BaseResponse().newFailResponse(ResponseCode.SYSTEM_ERROR.getCode(), "申请失败，系统异常，请稍后再试");
        }
    }

    /**
     * 根据提现订单号获取交易详情
     */
    @GetMapping(value = "/withdrawal/order")
    public BaseResponse withdrawalOrder(String withdrawalOrder,String withdrawalStatus) {
        //logger.info("WithdrawalController.withdrawalOrder.start");
        try {
            if (StringUtils.isBlank(withdrawalOrder)) {
                //return new JsonResult(JsonResultCode.FAILURE, "提现订单号有误，请重新输入", "");
                return new BaseResponse().newFailResponse(ResponseCode.SYSTEM_ERROR.getCode(), "提现订单号有误，请重新输入");
            }
            WithdrawalInfo withdrawal = withdrawalService.getWithdrawalByWithdrawalOrderAndStatus(withdrawalOrder,withdrawalStatus);
            if (withdrawal == null) {
                return new BaseResponse().newFailResponse(ResponseCode.ERROR.getCode(), "提现订单号不存在，请重新填写");
            }
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setResult(withdrawal);
            baseResponse.setRespMsg("SUCCESS");
            baseResponse.setRespCode(ResponseCode.SUCCESS.getCode());
            return baseResponse;
        } catch (Exception ex) {
            // logger.error("[WithdrawalController][applyWithDrawalList]exception ", ex);
            return new BaseResponse().newFailResponse(ResponseCode.SYSTEM_ERROR.getCode(), "系统异常，请稍后再试");
        }


    }


    @LoggerOut
    @GetMapping("/queryPayStatus")
    public BaseResponse queryPayStatus(String id,String orderId){
        return  alipayService.query(id,orderId);
    }


}
