package org.sgomez.test.springboot.app.exceptions;

public class MoneyIsNotEnoughException extends  RuntimeException{
    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public MoneyIsNotEnoughException(String message) {
        super(message);
    }
}
