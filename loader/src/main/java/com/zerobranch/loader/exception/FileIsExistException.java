package com.zerobranch.loader.exception;

import java.io.IOException;

public class FileIsExistException extends IOException {
    public FileIsExistException(String message) {
        super(message);
    }
}
