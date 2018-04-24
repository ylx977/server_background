package com.gws.utils.crypto;

/**
 * An unexpected exception
 */
public class CryptoException extends Exception {

    public CryptoException(String message) {
        super(message);
    }

    public CryptoException(Throwable exception) {
        super("Unexpected Error", exception);
    }

    public CryptoException(String message, Throwable cause) {
        super(message, cause);
    }

}

