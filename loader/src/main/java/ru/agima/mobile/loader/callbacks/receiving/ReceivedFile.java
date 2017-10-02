package ru.agima.mobile.loader.callbacks.receiving;

import java.io.File;
import java.io.Serializable;

public interface ReceivedFile extends Serializable {
    void set(File path);
}