package com.codersdungeon.warp.engine;

import com.codersdungeon.warp.engine.exceptions.InitializationException;
import com.codersdungeon.warp.engine.util.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameEngine implements Disposable, Initializable, Runnable{
    private static final Logger LOG = LoggerFactory.getLogger(GameEngine.class);

    public static final int TARGET_FPS = 75;
    public static final int TARGET_UPS = 30;

    private final Window window;
    private final GameLogic gameLogic;
    private final Timer timer;

    public GameEngine(Window window, GameLogic gameLogic) {
        this.window = window;
        this.gameLogic = gameLogic;
        this.timer = new Timer();
    }

    @Override
    public void init() throws InitializationException {
        window.init();
        timer.init();
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

        float elapsedTime;
        float accumulator = 0f;
        float interval = 1f / TARGET_UPS;

        boolean running = true;
        while (running && !window.shouldClose()) {
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input();

            while (accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            render();

//            if (!window.isvSync()) {
//                sync();
//            }
        }
    }

    private void input() {
        gameLogic.handleInput();
        window.closeOnEsc();
    }

    private void update(float interval) {
        gameLogic.update(interval);
    }

    private void render() {
        gameLogic.render();
        window.update();
    }
}
