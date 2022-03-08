package com.codersdungeon.warp.engine.scenes;

import com.codersdungeon.warp.engine.LifeCycleComponent;

public interface Scene2D extends LifeCycleComponent {
    void enter();
    void update(float dt);
    void render(float dt);
    void exit();
}
