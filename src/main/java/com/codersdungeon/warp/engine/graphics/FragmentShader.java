package com.codersdungeon.warp.engine.graphics;

import com.codersdungeon.warp.engine.exceptions.ShaderException;

import static org.lwjgl.opengl.GL33.*;

public final class FragmentShader extends Shader{

    private FragmentShader(int vertexID, String shaderSource) {
        super(vertexID, shaderSource);
    }

    public static FragmentShader createShader(String shaderSource) throws ShaderException {
        int shaderID = glCreateShader(GL_FRAGMENT_SHADER);
        FragmentShader shader = new FragmentShader(shaderID, shaderSource);
        shader.createShader();
        return shader;
    }
}
