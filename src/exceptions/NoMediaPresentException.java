package exceptions;

public class NoMediaPresentException extends Exception {
    public NoMediaPresentException(String title) {
        super("No show / title \"" + title + "\" present in database");
    }
}
