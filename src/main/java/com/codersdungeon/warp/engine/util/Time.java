package com.codersdungeon.warp.engine.util;

public class Time {
    private static final long applicationStart = System.nanoTime();

    private Time(){}

    public static double getTime(){
        return (System.nanoTime() - applicationStart) * 1E-9;
    }

    public static long getNanoTime(){
        return System.nanoTime() - applicationStart;
    }
}
