package com.codersdungeon.warp.engine.graphics;

import com.codersdungeon.warp.engine.exceptions.ShaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.lwjgl.opengl.GL33.*;

public final class ShaderProgram {
    private static final Logger LOG = LoggerFactory.getLogger(ShaderProgram.class);

    private final int programID;
    private final VertexShader vertexShader;
    private final FragmentShader fragmentShader;

    private ShaderProgram(int programID, VertexShader vertexShader, FragmentShader fragmentShader){
        this.programID = programID;
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
    }

    public void useProgram(){
        glUseProgram(programID);
    }

    public void endProgram(){
        glUseProgram(0);
    }

    public void deleteProgram(){
        glDeleteProgram(programID);
    }

    public static ShaderProgram createProgram(VertexShader vertexShader, FragmentShader fragmentShader) throws ShaderException {
        int programID = glCreateProgram();
        glAttachShader(programID, vertexShader.getShaderID());
        glAttachShader(programID, fragmentShader.getShaderID());
        glLinkProgram(programID);

        int success = glGetProgrami(programID, GL_LINK_STATUS);
        if(success == GL_FALSE){
            int logLength = glGetProgrami(programID, GL_INFO_LOG_LENGTH);
            String logContent = glGetProgramInfoLog(programID, logLength);
            LOG.error("{}", logContent);
            throw new ShaderException(logContent);
        }

        vertexShader.deleteShader();
        fragmentShader.deleteShader();

        return new ShaderProgram(programID, vertexShader, fragmentShader);
    }
}