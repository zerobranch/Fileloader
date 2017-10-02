package ru.agima.mobile.loader.callbacks.receiving;

import java.io.Serializable;

public interface ReceivedFileSource extends Serializable {
    void set(byte[] source, String name);
}