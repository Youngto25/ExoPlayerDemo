package com.example.exoplayerdemo;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface OnOrientationChangedListener {
    int SENSOR_UNKNOWN = -1;
    int SENSOR_PORTRAIT = SENSOR_UNKNOWN + 1;
    int SENSOR_LANDSCAPE = SENSOR_PORTRAIT + 1;

    @IntDef({SENSOR_UNKNOWN, SENSOR_PORTRAIT, SENSOR_LANDSCAPE})
    @Retention(RetentionPolicy.SOURCE)
    @interface SensorOrientationType {

    }

    void onChanged(@SensorOrientationType int orientation);
}