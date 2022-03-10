package com.codersdungeon.warp.engine.scenes;

import com.codersdungeon.warp.engine.Disposable;
import com.codersdungeon.warp.engine.Initializable;

public interface Scene2D extends Disposable, Initializable {
    void enter();
    void update(float dt);
    void render(float dt);
    void exit();
}
