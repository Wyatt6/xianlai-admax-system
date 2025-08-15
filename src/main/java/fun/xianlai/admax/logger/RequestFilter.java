package fun.xianlai.admax.logger;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Wyatt
 * @date 2024/2/2
 */
@Slf4j
@Component
public class RequestFilter extends OncePerRequestFilter implements Filter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            String url = request.getRequestURL().toString();
            if (!url.matches(".*/actuator.*")) {
                // 生成traceId（traceId）
                String traceId = request.getHeader("traceId");
                if (traceId == null) {
                    traceId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
                }
                MDC.put("traceId", traceId);
                log.info(">>>>> 开始处理请求: {} {}", request.getMethod(), request.getRequestURL());
            }
            // 处理请求
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
