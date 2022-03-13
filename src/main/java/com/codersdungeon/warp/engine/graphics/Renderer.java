package com.codersdungeon.warp.engine.graphics;

import com.codersdungeon.warp.engine.*;
import com.codersdungeon.warp.engine.exceptions.InitializationException;
import org.joml.Matrix4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Renderer implements Disposable, Initializable {
    private static final Logger LOG = LoggerFactory.getLogger(Renderer.class);

    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;


    private final Window window;
    private final Transformation transformation;

    private ShaderProgram shaderProgram;
    
    public Renderer(Window window){
        this.window = window;
        this.transformation = new Transformation();
    }

    @Override
    public void init() throws InitializationException {
        LOG.debug("init renderer");

        shaderProgram = Graphics.createShaderProgram("assets/shaders/vertex.vs", "assets/shaders/fragment.fs");
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("worldMatrix");
        shaderProgram.createUniform("texture_sampler");

        window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public void render(GameItem[] gameItems){
        window.clear();

    /*    if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }*/

        shaderProgram.bind();
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        for(GameItem gameItem : gameItems) {
            // Set world matrix for this item
            Matrix4f worldMatrix =
                    transformation.getWorldMatrix(
                            gameItem.getPosition(),
                            gameItem.getRotation(),
                            gameItem.getScale());
            shaderProgram.setUniform("worldMatrix", worldMatrix);
            shaderProgram.setUniform("texture_sampler", 0);
            // Render the mes for this game item
            gameItem.getMesh().render();
        }

        shaderProgram.unbind();
    }

    @Override
    public void dispose() {
        LOG.debug("dispose renderer");
        if(shaderProgram!=null){
            shaderProgram.dispose();
        }
    }
}
