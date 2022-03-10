package com.codersdungeon.warp.engine.graphics;

import com.codersdungeon.warp.engine.Disposable;
import com.codersdungeon.warp.engine.Initializable;
import com.codersdungeon.warp.engine.Window;
import com.codersdungeon.warp.engine.exceptions.InitializationException;
import com.codersdungeon.warp.engine.util.Resources;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer implements Disposable, Initializable {

    private ShaderProgram shaderProgram;

    @Override
    public void init() throws InitializationException {
        String vertexSource = Resources.loadString("assets/shaders/vertex.vs");
        String fragmentSource = Resources.loadString("assets/shaders/fragment.fs");

        shaderProgram = ShaderProgram.create(vertexSource, fragmentSource);
        shaderProgram.init();
    }

    public void render(Window window, Mesh mesh){
        window.clear();

    /*    if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }*/

        shaderProgram.bind();

        // Draw the mesh
        glBindVertexArray(mesh.getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);

        // Restore state
        glBindVertexArray(0);

        shaderProgram.unbind();
    }

    @Override
    public void dispose() {
        shaderProgram.dispose();
    }
}
