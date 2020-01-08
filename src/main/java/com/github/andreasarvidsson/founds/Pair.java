package com.github.andreasarvidsson.founds;

/**
 *
 * @author Andreas Arvidsson
 * @param <F>
 * @param <S>
 */
public class Pair<F, S> {

    private final F first;
    private final S second;

    public Pair(final F first, final S second) {
        this.first = first;
        this.second = second;
    }

    public F first() {
        return first;
    }

    public S second() {
        return second;
    }

}
