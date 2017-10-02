package ru.agima.mobile.loader.callbacks.lifecycle;

import java.io.Serializable;

public interface OnStartNext extends Serializable {
    void apply(String fileName);
}