package com.codersdungeon.warp.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class Application{
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public Application() {
        LOG.debug("new");
    }

    public void run() {
        LOG.debug("Run");
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("application.properties")) {
            Window window = createWindow(inputStream);
            GameLogic gameLogic = createGameLogic(window);
            GameEngine gameEngine = new GameEngine(window, gameLogic);

            Thread thread = new Thread(gameEngine);
            thread.run();
            thread.join();
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        } finally {
            LOG.debug("End");
        }

    }

    private Window createWindow(InputStream inputStream) throws IOException {
        Properties properties = new Properties();
        properties.load(inputStream);

        int width = Integer.parseInt(properties.getProperty("window.width"));
        int height = Integer.parseInt(properties.getProperty("window.height"));
        String title = properties.getProperty("window.title");

        return new Window(title, width, height);
    }

    protected abstract GameLogic createGameLogic(Window window);
}