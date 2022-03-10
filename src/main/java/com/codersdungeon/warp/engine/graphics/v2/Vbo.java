package com.codersdungeon.warp.engine.graphics.v2;

import com.codersdungeon.warp.engine.Disposable;
import com.codersdungeon.warp.engine.Initializable;
import com.codersdungeon.warp.engine.exceptions.InitializationException;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL33.*;

public final class Vbo implements Disposable, Initializable {

    private final int vboID;
    private final int index;
    private final int size;
    private final int stride;
    private final int offset;
    private final FloatBuffer verticesBuffer;

    private Vbo(int vboID, int index, int size, int stride, int offset, FloatBuffer verticesBuffer){
        this.vboID = vboID;
        this.index = index;
        this.size = size;
        this.stride = stride;
        this.offset = offset;
        this.verticesBuffer = verticesBuffer;
    }

    public static Vbo createBuffer(FloatBuffer verticesBuffer, int index, int size, int stride, int offset){
        int vboId = glGenBuffers();
        return new Vbo(vboId, index, size, stride, offset, verticesBuffer);
    }

    @Override
    public void init() throws InitializationException {
        if(vboID == 0){
            throw new InitializationException("Cannot create vertex buffer");
        }

        bind();
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        enable();
        glVertexAttribPointer(index, size, GL_FLOAT, false, stride * Float.BYTES, offset);
        unbind();
    }

    public void bind(){
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
    }

    public void enable(){
        glEnableVertexAttribArray(index);
    }

    public void disable(){
        glDisableVertexAttribArray(index);
    }

    public void unbind(){
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    @Override
    public void dispose() {
        unbind();
        glDeleteBuffers(vboID);
    }
}
