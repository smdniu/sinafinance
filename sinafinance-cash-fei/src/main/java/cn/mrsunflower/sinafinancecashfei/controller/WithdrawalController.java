package cn.mrsunflower.sinafinancecashfei.controller;

import cn.mrsunflower.sinafinancecashfei.pojo.WithdrawalInfo;
import cn.mrsunflower.sinafinancecashfei.service.AlipayService;
import cn.mrsunflower.sinafinancecashfei.service.impl.PreWithdrawalImpl;
import cn.mrsunflower.sinafinancecashfei.service.impl.WithdrawalServiceImpl;
import com.sinafinance.annotation.LoggerOut;
import com.sinafinance.enums.ResponseCode;
import com.sinafinance.vo.BaseResponse;
import com.sinafinance.vo.CashOutRequest;
import com.sinafinance.vo.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

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
    @GetMapping(value = "/withdrawal/count")
    public BaseResponse countWithdraw(HttpServletRequest request, HttpServletResponse response,
                                      long withdrawApply, Long userId) {
        //logger.info("WithdrawalController.countWithdraw.start");
        try {
            Map map = preWithdrawalImpl.countWithdraw(withdrawApply,userId);

            //创建结果返回值
            BaseResponse baseResponse = new BaseResponse();
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
     * @param uid     用户id
     * @return
     */
    @PostMapping(value = "/withdrawal/apply")
    public BaseResponse applyWithDrawal(@RequestBody CashOutRequest request,Authentication user) {

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

            /**
             *发送消息通知
             * ps:这里没有实现,有空可以实现一下
             */
            //sendSmsNotice(withdrawal, userId, seller, billMoney, withdrawApplyTotal);

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
