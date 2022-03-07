package com.codersdungeon.warp.engine;

public class DummyApplication extends Application {

    @Override
    GameLogic createGameLogic() {
        return new DummyGame();
    }
}
