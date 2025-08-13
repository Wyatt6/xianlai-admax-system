package fun.xianlai.admax.exception;

/**
 * @author Wyatt6
 * @date 2025/8/13
 */
public class SystemException extends RuntimeException {
    public SystemException() {
        super("系统错误");
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
