package com.codersdungeon.warp.engine.graphics;

import com.codersdungeon.warp.engine.exceptions.ShaderException;

import static org.lwjgl.opengl.GL33.*;

public final class VertexShader extends Shader{

    private VertexShader(int vertexID, String shaderSource) {
        super(vertexID, shaderSource);
    }

    public static VertexShader createShader(String shaderSource) throws ShaderException {
        int shaderID = glCreateShader(GL_VERTEX_SHADER);
        VertexShader shader = new VertexShader(shaderID, shaderSource);
        shader.createShader();
        return shader;
    }
}
