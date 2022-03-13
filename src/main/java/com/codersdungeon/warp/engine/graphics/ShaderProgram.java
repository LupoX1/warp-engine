package com.codersdungeon.warp.engine.graphics;

import com.codersdungeon.warp.engine.Disposable;
import com.codersdungeon.warp.engine.exceptions.InitializationException;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public final class ShaderProgram implements Disposable {
    private static final Logger LOG = LoggerFactory.getLogger(ShaderProgram.class);

    private final int programId;
    private final Map<String, Integer> uniforms;

    ShaderProgram(int programId){
        LOG.debug("new shader program: ID '{}'", programId);
        this.programId = programId;
        uniforms = new HashMap<>();
    }

    void loadShaders(String vertexSource, String fragmentSource) throws InitializationException {
        LOG.debug("load shaders");

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

    public void createUniform(String uniformName) throws InitializationException {
        int uniformLocation = glGetUniformLocation(programId, uniformName);
        if (uniformLocation < 0) {
            throw new InitializationException("Could not find uniform:" + uniformName);
        }
        uniforms.put(uniformName, uniformLocation);
    }

    public void setUniform(String uniformName, Matrix4f value) {
        // Dump the matrix into a float buffer
        try (MemoryStack stack = MemoryStack.stackPush()) {
            glUniformMatrix4fv(uniforms.get(uniformName), false, value.get(stack.mallocFloat(16)));
        }
    }

    public void setUniform(String uniformName, int value) {
        glUniform1i(uniforms.get(uniformName), value);
    }

    public void bind() {
        LOG.debug("use program ID '{}'", programId);

        glUseProgram(programId);
    }

    public void unbind() {
        LOG.debug("unbind program ID '{}'", programId);

        glUseProgram(0);
    }

    @Override
    public void dispose() {
        LOG.debug("dispose program ID '{}'", programId);

        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }

    private int createVertexShader(String shaderCode) throws InitializationException {
        LOG.debug("create vertex shader: '{}'", shaderCode);

        return createShader(shaderCode, GL_VERTEX_SHADER);
    }

    private int createFragmentShader(String shaderCode) throws InitializationException {
        LOG.debug("create fragment shader: '{}'", shaderCode);

        return createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    private int createShader(String shaderCode, int shaderType) throws InitializationException {
        LOG.debug("create shader");

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
        LOG.debug("link shaders '{}', '{}'", vertexShaderId, fragmentShaderId);

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
