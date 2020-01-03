package lionel.meethere.result;

public class CommonResult {
    public static final int SUCCESS = 200;

    public static final int FAILED = 500;

    public static final int UNAUTHORIZED = 401;

    public static final int ACCESS_DENIED = 402;

    public static <T> Result<T> success() {
        return new Result<>(SUCCESS, "成功");
    }

    public static <T> Result<T> failed() {
        return new Result<>(FAILED, "失败");
    }

    public static <T> Result<T> unauthorized() {
        return new Result<>(UNAUTHORIZED, "用户未认证");
    }

    public static <T> Result<T> accessDenied() {
        return new Result<>(ACCESS_DENIED, "没有权限访问");
    }
}