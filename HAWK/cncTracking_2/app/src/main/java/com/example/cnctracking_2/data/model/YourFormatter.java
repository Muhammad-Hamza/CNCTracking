package com.example.cnctracking_2.data.model;

import com.github.mikephil.charting.formatter.ValueFormatter;

public class YourFormatter extends ValueFormatter {
    @Override
    public String getFormattedValue(float value) {
        return "" + ((int) value);
    }
}

