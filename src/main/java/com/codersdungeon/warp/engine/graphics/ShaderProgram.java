package com.codersdungeon.warp.engine.graphics;

import com.codersdungeon.warp.engine.Disposable;
import com.codersdungeon.warp.engine.exceptions.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram implements Disposable {
    private static final Logger LOG = LoggerFactory.getLogger(ShaderProgram.class);

    private final int programId;

    ShaderProgram(int programId){
        this.programId = programId;
    }

    void loadShaders(String vertexSource, String fragmentSource) throws InitializationException {
        if(vertexSource == null){
            throw new InitializationException("Vertex source is null");
        }
        if(fragmentSource == null){
            throw new InitializationException("Fragment source is null");
        }

        int vertexShaderId = createVertexShader(vertexSource);
        int fragmentShaderId = createFragmentShader(fragmentSource);

        link(vertexShaderId, fragmentShaderId);
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    @Override
    public void dispose() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }

    private int createVertexShader(String shaderCode) throws InitializationException {
        return createShader(shaderCode, GL_VERTEX_SHADER);
    }

    private int createFragmentShader(String shaderCode) throws InitializationException {
        return createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    private int createShader(String shaderCode, int shaderType) throws InitializationException {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new InitializationException("Error creating shader. Type: " + shaderType);
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        boolean compileSuccess = glGetShaderi(shaderId, GL_COMPILE_STATUS) != GL_FALSE;
        if (!compileSuccess) {
            int logLength = glGetShaderi(shaderId, GL_INFO_LOG_LENGTH);
            String logContent = glGetShaderInfoLog(shaderId, logLength);
            throw new InitializationException("Error compiling Shader code: " + logContent);
        }

        return shaderId;
    }

    private void link(int vertexShaderId, int fragmentShaderId) throws InitializationException {
        glAttachShader(programId, vertexShaderId);
        glAttachShader(programId, fragmentShaderId);

        glLinkProgram(programId);

        boolean linkSuccess = glGetProgrami(programId, GL_LINK_STATUS) != GL_FALSE;
        if (!linkSuccess) {
            int logLength = glGetProgrami(programId, GL_INFO_LOG_LENGTH);
            String logContent = glGetProgramInfoLog(programId, logLength);
            throw new InitializationException("Error linking Shader code: " + logContent);
        }

        glDetachShader(programId, vertexShaderId);
        glDetachShader(programId, fragmentShaderId);

        glValidateProgram(programId);

        boolean validateSuccess = glGetProgrami(programId, GL_VALIDATE_STATUS) != GL_FALSE;
        if (!validateSuccess) {
            int logLength = glGetProgrami(programId, GL_INFO_LOG_LENGTH);
            String logContent = glGetProgramInfoLog(programId, logLength);
            LOG.warn("Warning validating Shader code: {}",logContent);
        }
    }
}
