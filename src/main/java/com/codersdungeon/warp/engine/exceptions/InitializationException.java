package com.codersdungeon.warp.engine.exceptions;

public final class InitializationException extends Exception{
    public InitializationException(Throwable ex){
        addSuppressed(ex);
    }
}
