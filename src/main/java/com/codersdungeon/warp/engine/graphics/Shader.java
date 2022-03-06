package com.codersdungeon.warp.engine.graphics;

import com.codersdungeon.warp.engine.exceptions.ShaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.lwjgl.opengl.GL33.*;

public abstract class Shader {
    private static final Logger LOG = LoggerFactory.getLogger(Shader.class);

    private final int shaderID;
    private final String shaderSource;

    Shader(int shaderID, String shaderSource){
        this.shaderID = shaderID;
        this.shaderSource = shaderSource;
    }

    public int getShaderID(){
        return shaderID;
    }

    public String getShaderSource(){
        return shaderSource;
    }

    public void deleteShader(){
        glDeleteShader(shaderID);
    }

    void createShader() throws ShaderException {
        glShaderSource(shaderID, shaderSource);
        glCompileShader(shaderID);

        int success = glGetShaderi(shaderID, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int logLength = glGetShaderi(shaderID, GL_INFO_LOG_LENGTH);
            String logContent = glGetShaderInfoLog(shaderID, logLength);
            LOG.error("{}", logContent);
            throw new ShaderException(logContent);
        }
    }
}
