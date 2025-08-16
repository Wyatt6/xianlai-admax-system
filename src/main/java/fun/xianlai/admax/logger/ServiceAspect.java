package fun.xianlai.admax.logger;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author WyattLau
 * @date 2024/2/4
 */
@Slf4j
@Aspect
@Component
public class ServiceAspect {
    @Pointcut("within(fun.xianlai.admax.module.*.service.impl.*ServiceImpl)")
    public void pointcut() {
        // 仅用于定义切点
    }

    /**
     * 进入和退出Service时打印日志（环绕通知）
     *
     * @param joinPoint 切点
     * @return 方法return的值
     */
    @Around("pointcut()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("--- CALL SERVICE {}() IN {} ---", joinPoint.getSignature().getName(), joinPoint.getSignature().getDeclaringTypeName());
        try {
            Object serviceReturn = joinPoint.proceed();
            log.info("--- EXIT SERVICE {}() ---", joinPoint.getSignature().getName());
            return serviceReturn;
        } catch (Throwable e) {
            log.info("--- EXIT SERVICE {}() WITH EXCEPTION ---", joinPoint.getSignature().getName());
            throw e;
        }
    }

    /**
     * Advice: Service抛出异常后打印的日志
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
    public void afterThrowingAdvice(JoinPoint joinPoint, Exception e) {
        log.info("抛出异常: {} {}", e.getMessage(), e.getClass().getName());
    }
}
