package lionel.meethere.order.exception;

public class BookingTimeConflictException extends RuntimeException{
    public BookingTimeConflictException(){
        super(null,null,true,false);
    }
}
