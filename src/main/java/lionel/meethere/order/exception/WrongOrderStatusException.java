package lionel.meethere.order.exception;

public class WrongOrderStatusException extends RuntimeException {
    public WrongOrderStatusException(){
        super(null,null,true,false);
    }
}
