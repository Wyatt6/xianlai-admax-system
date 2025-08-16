package fun.xianlai.admax.definition.response;

import lombok.Getter;

/**
 * @author WyattLau
 * @date 2024/1/31
 */
@Getter
public class Res {
    private Boolean success;        // 处理成功/失败
    private String message;         // 响应信息
    private Map data = new Map();   // 响应数据
    private String traceId;         // 日志跟踪ID

    public Res() {
        this.success = false;
    }

    public Res success() {
        this.success = true;
        return this;
    }

    public Res fail() {
        this.success = false;
        return this;
    }

    public Res setMessage(String message) {
        this.message = message;
        return this;
    }

    public Res setData(Map data) {
        this.data = data;
        return this;
    }

    public Res addData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public Res setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }
}
