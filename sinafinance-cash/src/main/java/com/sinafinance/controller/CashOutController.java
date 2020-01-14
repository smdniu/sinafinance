package com.sinafinance.controller;

import com.sinafinance.annotation.LoggerOut;
import com.sinafinance.service.AccountService;
import com.sinafinance.service.AlipayService;
import com.sinafinance.vo.BaseResponse;
import com.sinafinance.vo.CashOutRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/cash")
public class CashOutController {


    @Autowired
    private AccountService accountService;

    @Autowired
    private AlipayService alipayService;

    @LoggerOut
    @RequestMapping("/out")
    public BaseResponse cashout(@RequestBody CashOutRequest request, Authentication user){
        String uid = user.getName();
        request.setUid(uid);
        BaseResponse baseResponse = accountService.cashOut(request);
        return baseResponse;

    }

    /**
     * 回调
     */
    @LoggerOut
    @RequestMapping("/notify")
    public void notifyLogic(HttpServletRequest request){
        System.out.println("支付成功回调。。。。");
        try {
            InputStream inputStream = (InputStream)request.getInputStream();
            ByteArrayOutputStream outputStream=new ByteArrayOutputStream();

            byte[] buffer=new byte[1024];
            int len=0;
            while( ( len= inputStream.read(buffer) )!=-1   ){
                outputStream.write( buffer,0,len );
            }
            outputStream.close();
            inputStream.close();
            String result=new String( outputStream.toByteArray(),"utf-8" );
            System.out.println(result);
//            alipayService.changed();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @LoggerOut
    @GetMapping("/queryPayStatus")
    public BaseResponse queryPayStatus(String id,String orderId){
        return  alipayService.query(id,orderId);
    }

}
