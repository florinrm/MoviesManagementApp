package exceptions;

public class IncorrectFormatException extends Exception {
    public IncorrectFormatException() {
        super("Incorrect season name format - cannot contain =");
    }
}
