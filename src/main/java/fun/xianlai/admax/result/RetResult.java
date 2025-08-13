package fun.xianlai.admax.result;

/**
 * @author Wyatt6
 * @date 2025/8/13
 */
public class RetResult {
    private Boolean success;    // true - 成功 / false - 失败
    private DataMap data;       // 返回数据
    private String traceId;     // 日志跟踪ID

    public RetResult() {
        this.success = false;   // 出于安全考量，构造函数中默认设置success标记为false（失败）
    }

    public RetResult success() {
        this.success = true;
        return this;
    }

    public RetResult fail() {
        this.success = false;
        return this;
    }

    // 常用于 success = false 时携带错误信息提示
    public RetResult setMessage(String message) {
        if (this.data == null) {
            this.data = new DataMap("message", message);
        } else {
            this.data.put("message", message);
        }
        return this;
    }

    public RetResult setData(DataMap data) {
        this.data = data;
        return this;
    }

    public RetResult addData(String key, Object value) {
        if (this.data == null) {
            this.data = new DataMap(key, value);
        } else {
            this.data.put(key, value);
        }
        return this;
    }

    public RetResult setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }
}
