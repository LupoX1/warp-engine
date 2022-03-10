package com.codersdungeon.warp.engine.graphics;

import com.codersdungeon.warp.engine.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

public final class VertexBuffer implements Disposable {
    private static final Logger LOG = LoggerFactory.getLogger(VertexBuffer.class);

    private final int vboID;
    private final int index;

    VertexBuffer(int vboID, int index){
        LOG.debug("new vertex buffer: ID '{}', Index '{}'", vboID, index);
        this.vboID = vboID;
        this.index = index;
    }

    public void bind(){
        LOG.debug("bind vertex buffer: ID '{}', Index '{}'", vboID, index);
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
    }

    public void enable() {
        LOG.debug("enable vertex buffer: ID '{}', Index '{}'", vboID, index);
        glEnableVertexAttribArray(index);
    }

    public void disable() {
        LOG.debug("disable vertex buffer: ID '{}', Index '{}'", vboID, index);
        glDisableVertexAttribArray(index);
    }

    public void unbind(){
        LOG.debug("unbind vertex buffer: ID '{}', Index '{}'", vboID, index);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    @Override
    public void dispose(){
        LOG.debug("dispose vertex buffer: ID '{}', Index '{}'", vboID, index);
        if(vboID != 0){
            unbind();
            glDeleteBuffers(vboID);
        }
    }
}