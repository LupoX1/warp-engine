package com.codersdungeon.warp.engine.graphics;

import com.codersdungeon.warp.engine.exceptions.InitializationException;
import com.codersdungeon.warp.engine.util.Resources;

import static org.lwjgl.opengl.GL20.glCreateProgram;

public class Graphics {
    public static ShaderProgram createShaderProgram(String vertexShader, String fragmentShader) throws InitializationException {

        int programId = glCreateProgram();
        if(programId == 0){
            throw new InitializationException("Cannot create shader program");
        }

        String vertexSource = Resources.loadString(vertexShader);
        String fragmentSource = Resources.loadString(fragmentShader);

        ShaderProgram shaderProgram = new ShaderProgram(programId);
        shaderProgram.loadShaders(vertexSource, fragmentSource);
        return shaderProgram;
    }
}
