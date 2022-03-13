package com.codersdungeon.warp.engine.graphics;

import java.io.Serializable;
import java.util.List;

public class ModelData implements Serializable {
    private float[] vertices;
    private int[] indices;
    private  List<VertexTemplate> vertexTemplates;

    public ModelData(){

    }

    public ModelData(float[] vertices, int[] indices,  List<VertexTemplate> vertexTemplates) {
        this.vertices = vertices;
        this.indices = indices;
        this.vertexTemplates = vertexTemplates;
    }

    public float[] getVertices() {
        return vertices;
    }

    public void setVertices(float[] vertices) {
        this.vertices = vertices;
    }

    public int[] getIndices() {
        return indices;
    }

    public void setIndices(int[] indices) {
        this.indices = indices;
    }

    public List<VertexTemplate> getVertexTemplates() {
        return vertexTemplates;
    }

    public void setVertexTemplates( List<VertexTemplate> vertexTemplates) {
        this.vertexTemplates = vertexTemplates;
    }
}
