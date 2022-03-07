package com.codersdungeon.warp.engine;

import com.codersdungeon.warp.engine.exceptions.InitializationException;

public interface LifeCycleComponent {
    void init() throws InitializationException;
    void destroy();
}
