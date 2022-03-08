package com.codersdungeon.warp.engine;

public class DummyApplication extends Application {

    @Override
    protected GameLogic createGameLogic() {
        return new DummyGame();
    }
}