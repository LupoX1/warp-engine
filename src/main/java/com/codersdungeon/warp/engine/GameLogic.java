package com.codersdungeon.warp.engine;

public interface GameLogic extends Disposable, Initializable {

    void handleInput();

    void update(float deltaTime);

    void render();
}