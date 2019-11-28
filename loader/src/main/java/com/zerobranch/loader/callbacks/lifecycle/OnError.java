package com.zerobranch.loader.callbacks.lifecycle;

public interface OnError {
    void apply(String fileName, Throwable throwable);
}
