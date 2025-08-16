package fun.xianlai.admax.logger;

import cn.dev33.satoken.exception.NotLoginException;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import fun.xianlai.admax.definition.exception.OnceException;
import fun.xianlai.admax.definition.response.Res;

/**
 * Around --> Before --> 目标方法 --> After --> AfterReturning --> AfterThrowing --> Around
 *
 * @author WyattLau
 * @date 2024/2/2
 */
@Slf4j
@Aspect
@Component
public class ControllerAspect {
    private final int RESPONSE_MAX_LENGTH = 5000;

    @Pointcut("within(fun.xianlai.admax.module.*.controller.*Controller)")
    public void pointcut() {
        // 仅用于定义切点
    }

    /**
     * 进入和退出Controller时打印日志，封装返回结果
     *
     * @param joinPoint 切点
     * @return 目标方法返回的值
     */
    @Around("pointcut()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) {
        log.info("===== RUN CONTROLLER {}() IN {} =====", joinPoint.getSignature().getName(), joinPoint.getSignature().getDeclaringTypeName());
        try {
            Object result = joinPoint.proceed();
            log.info("===== EXIT CONTROLLER {}() =====", joinPoint.getSignature().getName());
            return result;
        } catch (Throwable e) {
            // 当Controller抛出异常时，封装成失败的响应信息
            Res res = new Res();
            if (e instanceof OnceException) {
                res.setMessage(e.getMessage());
            } else if (e instanceof IllegalArgumentException) {
                res.addData("code", 400).setMessage("请求参数错误");
            } else if (e instanceof NotLoginException) {
                res.addData("code", 401).setMessage("用户未登录");
            } else {
                res.addData("code", 500).setMessage("服务器内部错误");
            }
            res.fail().setTraceId(MDC.get("traceId"));
            logRes(res);
            log.info("===== EXIT CONTROLLER {}() WITH FAIL =====", joinPoint.getSignature().getName());
            return res;
        }
    }

    /**
     * 为Controller的响应数据装配traceId，并打印日志
     *
     * @param joinPoint 切点
     * @param res       主方法的返回
     */
    @AfterReturning(pointcut = "pointcut()", returning = "res")
    public void afterReturningAdvice(JoinPoint joinPoint, Res res) {
        res.setTraceId(MDC.get("traceId"));
        logRes(res);
    }

    /**
     * 当Controller抛出异常时，打印日志
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
    public void afterThrowingAdvice(JoinPoint joinPoint, Exception e) {
        log.info("错误: {} {}", e.getMessage(), e.getClass().getName());
    }

    /**
     * 返回的数据可能很大，此时就不能写入到日志中占用太多的I/O资源
     *
     * @param res 响应对象
     */
    private void logRes(Res res) {
        String jsonString = JSONObject.toJSONString(res);
        if (jsonString.length() > RESPONSE_MAX_LENGTH) {
            log.info("响应数据过大，不写入日志");
        } else {
            log.info("响应: {}", jsonString);
        }
    }
}
