package lionel.meethere.result;


import lionel.meethere.result.Result;

public class UserResult {

    public static final int INVALID_USERNAME_OR_PASSWORD = 101;

    public static final int INCORRECT_USERNAME_OR_PASSWORD = 102;

    public static final int USERNAME_ALREADY_EXISTS = 103;

    public static final int USERNAME_NOT_EXISTS = 104;

    public static final int INVALID_TELEPHONE_LENGTH = 105;

    public static Result invalidUsernameOrPassword() {
        return new Result(INVALID_USERNAME_OR_PASSWORD, "用户名或密码不合法");
    }

    public static Result incorrectUsernameOrPassword() {
        return new Result(INCORRECT_USERNAME_OR_PASSWORD, "用户名或密码不正确");
    }

    public static Result usernameAlreadyExists() {
        return new Result(USERNAME_ALREADY_EXISTS, "用户名已存在");
    }

    public static Result usernameNotExists() {
        return new Result(USERNAME_NOT_EXISTS, "该用户名没有注册过");
    }

    public static Result invalidTelephone(){
        return new Result(INVALID_TELEPHONE_LENGTH,"输入手机位数不合法");
    }
}