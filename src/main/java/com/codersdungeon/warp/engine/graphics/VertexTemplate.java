package com.codersdungeon.warp.engine.graphics;

public final class VertexTemplate {
    private final int index;
    private final int elements;
    private final String description;

    public VertexTemplate(int index, int elements, String description) {
        this.index = index;
        this.elements = elements;
        this.description = description;
    }

    public int getIndex() {
        return index;
    }

    public int getElements() {
        return elements;
    }

    public String getDescription() {
        return description;
    }
}
