package lionel.meethere.result;

public class OrderResult {
    public static final int  RESERVE_TIME_CONFLICT= 501;


    public static Result reserveTimeConflict() {
        return new Result(RESERVE_TIME_CONFLICT, "预约时间冲突");
    }

}
