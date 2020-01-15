package com.github.andreasarvidsson.founds.util;

/**
 *
 * @author Andreas Arvidsson
 */
public abstract class BaseUtil {

    protected String format(final double value) {
        return String.format("%.2f", Math.round(value * 100.0) / 100.0);
    }

    protected int max(final int value, final int... values) {
        int res = value;
        for (int i = 0; i < values.length; ++i) {
            if (values[i] > res) {
                res = values[i];
            }
        }
        return res;
    }

}
