package ru.agima.mobile.loader.callbacks.lifecycle;

import java.io.Serializable;

public interface OnProgress extends Serializable {
    void apply(int progress);
}