package gargoyle.sfx;

public class SFXException extends RuntimeException {
    private static final long serialVersionUID = -4261502832993031333L;

    public SFXException(String message, Exception e) {
        super(message, e);
    }

    public SFXException(String message) {
        super(message);
    }
}
