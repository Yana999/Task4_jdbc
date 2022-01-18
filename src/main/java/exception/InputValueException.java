package exception;

public final class InputValueException extends Exception{

    private final StringBuilder message;

    public InputValueException(String wrongParamName, String wrongParam) {
        message = new StringBuilder("Inappropriate value of the ");
        message.append(wrongParamName);
        message.append(" ");
        message.append(wrongParam);
    }
    public String getMessage() {
        return message.toString();
    }
}
