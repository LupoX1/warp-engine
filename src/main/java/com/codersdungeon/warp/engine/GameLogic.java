package com.codersdungeon.warp.engine;

public interface GameLogic extends LifeCycleComponent{

    void handleInput(Window window);

    void update(float deltaTime);

    void render(Window window);
}