package fun.xianlai.admax.definition.exception;

/**
 * @author WyattLau
 * @date 2024/2/2
 */
public class OnceException extends RuntimeException {
    public OnceException() {
        super("系统错误");
    }

    public OnceException(String message) {
        super(message);
    }

    public OnceException(String message, Throwable cause) {
        super(message, cause);
    }
}
