package com.codersdungeon.warp.engine;

public interface GameLogic extends Disposable, Initializable {

    void handleInput(Window window);

    void update(float deltaTime);

    void render(Window window);
}