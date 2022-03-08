package com.codersdungeon.warp.engine;

import com.codersdungeon.warp.engine.exceptions.InitializationException;
import com.codersdungeon.warp.engine.graphics.Mesh;
import com.codersdungeon.warp.engine.graphics.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

public class DummyGame implements GameLogic{
    private static final Logger LOG = LoggerFactory.getLogger(DummyGame.class);

    private int direction = 0;
    private float color = 0.0f;
    private Renderer renderer;
    private Mesh mesh;

    public DummyGame() {

    }

    @Override
    public void init() throws InitializationException {
        renderer = new Renderer();
        LOG.debug("init");
        renderer.init();

        float[] positions = new float[]{
                -0.5f, 0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.5f, 0.5f, 0.0f
        };
        float[] colors = new float[]{
                1.0f, 0.0f, 0.0f, 1.0f,
                0.0f, 1.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 0.0f, 1.0f
        };
        int[] indices = new int[]{
                0, 1, 3, 3, 1, 2
        };
        mesh = new Mesh(positions, colors, indices);
        mesh.init();
    }

    @Override
    public void handleInput(Window window) {
        if (KeyboardListener.isKeyPressed(GLFW_KEY_UP)) {
            direction = 1;
        } else if (KeyboardListener.isKeyPressed(GLFW_KEY_DOWN)) {
            direction = -1;
        } else {
            direction = 0;
        }
    }

    @Override
    public void update(float interval) {
        color += direction * 0.01f;
        if (color > 1) {
            color = 1.0f;
        } else if (color < 0) {
            color = 0.0f;
        }
    }

    @Override
    public void render(Window window) {
        window.setClearColor(0.3f, 0.3f, 0.3f, 1.0f);
        renderer.render(window, mesh);
    }

    @Override
    public void dispose() {
        LOG.debug("destroy");
        renderer.dispose();
        mesh.dispose();
    }
}