package cn.mrsunflower.sinafinancecashfei.aop;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sinafinance.annotation.LoggerOut;
import com.sinafinance.enums.LoggerFormat;
import com.sinafinance.vo.BaseResponse;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: LoggerInterceptor
 *
 * @author attractor
 *         功能描述：Logger AOP日志输出，统计调用执行时间和日志
 */
@Component
@Aspect
public class LoggerInterceptor implements MethodInterceptor {
    private static Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);
    private ObjectMapper objectMapper;
    public LoggerInterceptor() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }
    /**
     * Aspect输出日志
     *
     * @param mi
     *
     * @return
     *
     * @throws Throwable
     */
    @Around("@annotation(com.sinafinance.annotation.LoggerOut)")
    public Object logerOut(ProceedingJoinPoint mi) throws Throwable {
        ProxyMethodInvocation method =
                (ProxyMethodInvocation) FieldUtils.readDeclaredField(mi, "methodInvocation", true);
        // 调用AOP方法拦截输出
        return this.invoke(method);
    }
    public Object invoke(MethodInvocation mi) throws Throwable {
        // 整理调用方法的信息;
        String methodSimpleInfo = getMethodSimpleName(mi);
        long start = System.currentTimeMillis();
        // 判断Annotation是否需要记录日志
        LoggerOut loggerOut = mi.getMethod().getAnnotation(LoggerOut.class);
        try {
            Object result = mi.proceed();

            if (null == loggerOut) {
                return result;
            }

            if (!loggerOut.enable() || (!logger.isDebugEnabled() && loggerOut.debug())) {
                return result;
            }
            if (loggerOut.enable()) {
                String status = "OK";
                if (result instanceof BaseResponse) {
                    if (!((BaseResponse) result).isSuccess()) {
                        status = "FAIL";
                    }
                }
                try {
                    outputLog(loggerOut, methodSimpleInfo, status, mi.getArguments(), result, start);
                } catch (Throwable t) {
                    logger.debug("printReq fail.");
                }
            }
            return result;
        } catch (Throwable t) {
            if (loggerOut.duration()) {
                logger.error("Call={} Status=ERR Spend={} Request={}", methodSimpleInfo,
                        (System.currentTimeMillis() - start), mi.getArguments(), t);
            } else {
                logger.error("Call={} Status=ERR Request={}", methodSimpleInfo, mi.getArguments(), t);
            }
            throw t;
        }
    }
    /**
     * 取得方法名称
     *
     * @param mi
     *
     * @return
     */
    private static String getMethodSimpleName(MethodInvocation mi) {
        return new StringBuilder(ClassUtils.getSimpleName(mi.getThis().getClass())).append(".")
                .append(mi.getMethod().getName()).toString();
    }
    private void outputLog(LoggerOut loggerOut, String methodInfo, String status, Object[] reqArgs, Object result,
                           long start) {
        StringBuilder logBuilder = new StringBuilder("Call={} Status={} ");
        List<Object> logParams = new ArrayList<>();
        logParams.add(methodInfo);
        logParams.add(status);
        if (loggerOut.duration()) {
            logBuilder.append("Spend={} ");
            logParams.add(System.currentTimeMillis() - start);
        }
        logBuilder.append("Request={}");
        logParams.add(getLogString(loggerOut, reqArgs));
        if (!loggerOut.onlyReq()) {
            logBuilder.append(" Response=[{}]");
            logParams.add(getLogString(loggerOut, result));
        }
        logger.info(logBuilder.toString(), logParams.toArray());
    }
    private String getLogString(LoggerOut loggerOut, Object param) {
        if (loggerOut.format() == LoggerFormat.JSON) {
            try {
                return objectMapper.writeValueAsString(param);
            } catch (JsonProcessingException e) {
                logger.debug("reqArgs serialize failed.");
            }
        }
        return param.toString();
    }
}
