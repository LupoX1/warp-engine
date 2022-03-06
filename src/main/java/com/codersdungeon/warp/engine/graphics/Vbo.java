package com.codersdungeon.warp.engine.graphics;

import org.lwjgl.BufferUtils;

import java.nio.Buffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL33.*;

public final class Vbo {

    private final int vboID;
    private final VertexArray vertexArray;

    private Vbo(int vboID, VertexArray vertexArray){
        this.vboID = vboID;
        this.vertexArray = vertexArray;
    }

    private void bindBuffer(int vboID){
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.getSize());
        vertexBuffer.put(vertexArray.getData());
        vertexBuffer.flip();

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
    }

    private void createLayout(){
        int vertexSizeBytes = vertexArray.getVertexSize() * Float.BYTES;

        long offset = 0;
        for(VertexTemplate template : vertexArray.getTemplate()){
            glVertexAttribPointer(template.getIndex(), template.getElements(), GL_FLOAT, false, vertexSizeBytes, offset * Float.BYTES);
            offset += template.getElements();
        }
    }

    public void enableArrays(){
        for(VertexTemplate template : vertexArray.getTemplate()){
            glEnableVertexAttribArray(template.getIndex());
        }
    }

    public void disableArrays(){
        for(VertexTemplate template : vertexArray.getTemplate()){
            glDisableVertexAttribArray(template.getIndex());
        }
    }

    public static Vbo createBuffer(VertexArray vertexArray){
        int vboID = glGenBuffers();
        Vbo vbo = new Vbo(vboID, vertexArray);
        vbo.bindBuffer(vboID);
        vbo.createLayout();
        return vbo;
    }
}
