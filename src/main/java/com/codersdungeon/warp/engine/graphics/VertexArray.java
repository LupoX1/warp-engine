package com.codersdungeon.warp.engine.graphics;

import java.util.Arrays;

public final class VertexArray {
    private final float[] data;
    private final VertexTemplate[] template;

    public VertexArray(float[] data, VertexTemplate... template){
        this.data = data;
        this.template = template;
    }

    public int getSize(){
        return data.length;
    }

    public float[] getData() {
        return data;
    }

    public VertexTemplate[] getTemplate() {
        return template;
    }

    public int getVertexSize() {
        int sum = 0;
        for(VertexTemplate t : template){
            sum += t.getElements();
        }
        return sum;
    }
}
