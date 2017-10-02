package ru.agima.mobile.loader.callbacks.lifecycle;

import java.io.Serializable;

public interface OnCompletedNext extends Serializable {
    void apply(String fileName);
}
