package org.dddlib.codegen.api;

/**
 * Created by yyang on 2016/12/23.
 */
public class UnsupportedFileFormatException extends RuntimeException {
    public UnsupportedFileFormatException() {
    }

    public UnsupportedFileFormatException(String message) {
        super(message);
    }

    public UnsupportedFileFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedFileFormatException(Throwable cause) {
        super(cause);
    }

}
