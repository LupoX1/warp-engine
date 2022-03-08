package com.codersdungeon.warp.engine;

import com.codersdungeon.warp.engine.exceptions.InitializationException;
import com.codersdungeon.warp.engine.util.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameEngine implements LifeCycleComponent, Runnable{
    private static final Logger LOG = LoggerFactory.getLogger(GameEngine.class);

    private final Window window;
    private final GameLogic gameLogic;

    public GameEngine(Window window, GameLogic gameLogic) {
        this.window = window;
        this.gameLogic = gameLogic;
    }

    @Override
    public void init() throws InitializationException {
        window.init();
        gameLogic.init();
    }

    @Override
    public void run() {
        try{
            init();
            mainLoop();
        }catch (Exception ex){
            LOG.error(ex.getMessage(), ex);
        }finally {
            dispose();
        }
    }

    @Override
    public void dispose() {
        gameLogic.dispose();
        window.dispose();
    }

    private void mainLoop(){
        LOG.debug("Main loop");

        long startTime = Time.getNanoTime();

        while (!window.shouldClose()) {
            long currentTime = Time.getNanoTime();
            long deltaTime = currentTime - startTime;
            startTime = currentTime;
            //double fps = 1E9 / deltaTime;

            window.update();
            window.closeOnEsc();
            gameLogic.handleInput(window);
            gameLogic.update(deltaTime);
            gameLogic.render(window);
        }
    }
}
