package xin.harrison.hcode.utils;

/**
 * @author Harrison
 * @version 1.0.0
 */
public class Result<T> {
    private T data;
    private String message;
    private boolean success;
    private int code;

    public Result(T data, String message, boolean success, int code) {
        this.data = data;
        this.message = message;
        this.success = success;
        this.code = code;
    }

    public static <T> Result<T> success(T data, String message) {
        return new Result<T>(data, message, true, 200);
    }

    public static <T> Result<T> success(T data) {
        return new Result<T>(data, "操作成功", true, 200);
    }

    public static <T> Result<T> success() {
        return new Result<T>(null, "操作成功", true, 200);
    }

    public static <T> Result<T> fail(T data, String message) {
        return new Result<T>(data, message, false, 500);
    }

    public static <T> Result<T> fail(T data, String message, int code) {
        return new Result<T>(data, message, false, code);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<T>(null, message, false, 500);
    }

    public static <T> Result<T> fail(String message, int code) {
        return new Result<T>(null, message, false, code);
    }

    public static <T> Result<T> fail() {
        return new Result<T>(null, "操作失败", false, 500);
    }

    public static <T> Result<T> error(T data, String message) {
        return new Result<T>(data, message, false, 500);
    }

    public static <T> Result<T> error(T data, String message, int code) {
        return new Result<T>(data, message, false, code);
    }

    public static <T> Result<T> error(String message) {
        return new Result<T>(null, message, false, 500);
    }

    public static <T> Result<T> error(String message, int code) {
        return new Result<T>(null, message, false, code);
    }

    public static <T> Result<T> error() {
        return new Result<T>(null, "操作异常", false, 500);
    }
}
