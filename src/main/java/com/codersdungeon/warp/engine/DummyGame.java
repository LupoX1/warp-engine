package com.codersdungeon.warp.engine;

import com.codersdungeon.warp.engine.exceptions.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DummyGame implements GameLogic{
    private static final Logger LOG = LoggerFactory.getLogger(DummyGame.class);

    @Override
    public void handleInput(Window window) {
        LOG.debug("handleInput");
    }

    @Override
    public void update(float deltaTime) {
        LOG.debug("update {}", deltaTime);
    }

    @Override
    public void render(Window window) {
        LOG.debug("render");
    }

    @Override
    public void init() throws InitializationException {
        LOG.debug("init");
    }

    @Override
    public void destroy() {
        LOG.debug("destroy");
    }
}
