package com.codersdungeon.warp.engine.graphics;

import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33.*;

public final class Ebo {

    private final int eboID;
    private final int[] elementArray;

    private Ebo(int eboID, int[] elementArray){
        this.eboID = eboID;
        this.elementArray = elementArray;
    }

    public int getEboID() {
        return eboID;
    }

    public int[] getElementArray() {
        return elementArray;
    }

    public static Ebo createBuffer(int[] elementArray){
        int eboID = glGenBuffers();

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray);
        elementBuffer.flip();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        return new Ebo(eboID, elementArray);
    }
}
