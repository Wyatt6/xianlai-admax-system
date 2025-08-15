package fun.xianlai.admax.logger;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import fun.xianlai.admax.definition.response.Res;

/**
 * @author Wyatt
 * @date 2024/3/8
 */
@Slf4j
@ControllerAdvice
public class SatokenExceptionHandler {
    @ExceptionHandler(NotLoginException.class)
    @ResponseBody
    public Res notLoginExceptionHandler(Exception e) {
        log.info("Sa-Token验证异常：未登录");
        Res res = new Res();
        res.fail().addData("code", 401).setMessage("未登录").setTraceId(MDC.get("traceId"));
        log.info("响应: {}", JSONObject.toJSONString(res));
        return res;
    }


    @ExceptionHandler(NotRoleException.class)
    @ResponseBody
    public Res notRoleExceptionHandler(Exception e) {
        log.info("Sa-Token验证异常：角色异常");
        Res res = new Res();
        res.fail().addData("code", 503).setMessage("用户权限不足").setTraceId(MDC.get("traceId"));
        log.info("响应: {}", JSONObject.toJSONString(res));
        return res;
    }

    @ExceptionHandler(NotPermissionException.class)
    @ResponseBody
    public Res notPermissionExceptionHandler(Exception e) {
        log.info("Sa-Token验证异常：权限异常");
        Res res = new Res();
        res.fail().addData("code", 503).setMessage("用户权限不足").setTraceId(MDC.get("traceId"));
        log.info("响应: {}", JSONObject.toJSONString(res));
        return res;
    }
}
