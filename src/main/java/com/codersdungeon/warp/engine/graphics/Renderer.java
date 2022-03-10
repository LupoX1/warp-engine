package com.codersdungeon.warp.engine.graphics;

import com.codersdungeon.warp.engine.Disposable;
import com.codersdungeon.warp.engine.Initializable;
import com.codersdungeon.warp.engine.Window;
import com.codersdungeon.warp.engine.exceptions.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.lwjgl.opengl.GL11.*;

public class Renderer implements Disposable, Initializable {
    private static final Logger LOG = LoggerFactory.getLogger(Renderer.class);

    private ShaderProgram shaderProgram;

    @Override
    public void init() throws InitializationException {
        LOG.debug("init renderer");
        shaderProgram = Graphics.createShaderProgram("assets/shaders/vertex.vs", "assets/shaders/fragment.fs");
    }

    public void render(Window window, Mesh mesh){
        window.clear();

    /*    if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }*/

        shaderProgram.bind();

        mesh.bindVertexArray();
        mesh.enableBuffers();
        glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
        mesh.unbindVertexArray();

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
