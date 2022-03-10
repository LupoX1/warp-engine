package com.codersdungeon.warp.engine.graphics;

import com.codersdungeon.warp.engine.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Mesh implements Disposable {
    private static final Logger LOG = LoggerFactory.getLogger(Mesh.class);

    private final VertexArray vertexArray;
    private final List<VertexBuffer> vertexBuffers;
    private final ElementBuffer elementBuffer;

    public Mesh(VertexArray vertexArray, List<VertexBuffer> vertexBuffers, ElementBuffer elementBuffer) {
        LOG.debug("new mesh");
        this.vertexArray = vertexArray;
        this.vertexBuffers = vertexBuffers;
        this.elementBuffer = elementBuffer;
    }

    public int getVertexCount() {
        LOG.debug("get vertex count: {}", elementBuffer.getSize());
        return elementBuffer.getSize();
    }

    public void bindVertexArray() {
        LOG.debug("bind vertex array");
        vertexArray.bind();
    }

    public void unbindVertexArray() {
        LOG.debug("unbind vertex array");
        vertexArray.unbind();
    }

    public void enableBuffers() {
        LOG.debug("enable buffers");
        vertexBuffers.forEach(VertexBuffer::enable);
        elementBuffer.bind();
    }

    public void render() {
        LOG.debug("render");
        bindVertexArray();
        enableBuffers();
        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
        disableBuffers();
        unbindVertexArray();
    }

    public void disableBuffers() {
        LOG.debug("disable buffers");
        vertexBuffers.forEach(VertexBuffer::disable);
        elementBuffer.unbind();
    }

    @Override
    public void dispose() {
        LOG.debug("dispose mesh");
        vertexBuffers.forEach(VertexBuffer::disable);
        vertexBuffers.forEach(VertexBuffer::dispose);
        vertexArray.dispose();
    }
}
