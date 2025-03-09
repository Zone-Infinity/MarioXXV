package me.infinity.util;

public class Time {
    private static final float timeStarted = System.nanoTime();

    public static float getTime() {
        return (System.nanoTime() - timeStarted) * 1E-9f;
    }
}
