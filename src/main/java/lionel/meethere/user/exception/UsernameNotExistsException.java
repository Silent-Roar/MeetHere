package lionel.meethere.user.exception;

public class UsernameNotExistsException extends RuntimeException {
    public UsernameNotExistsException() {
        super(null, null, true, false);
    }
}

