package com.codersdungeon.warp.engine.scenes;

public interface Scene2D {
    void init();
    void enter();
    void update(float dt);
    void render(float dt);
    void exit();
}
