package com.codersdungeon.warp.engine;

import com.codersdungeon.warp.engine.exceptions.InitializationException;

public interface Initializable {
    void init() throws InitializationException;
}
