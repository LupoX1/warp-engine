package com.codersdungeon.warp.engine.graphics.v2;

import com.codersdungeon.warp.engine.Disposable;
import com.codersdungeon.warp.engine.Initializable;
import com.codersdungeon.warp.engine.exceptions.InitializationException;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public final class VertexArray implements Disposable, Initializable {
    private final List<Vbo> vbos;

    public VertexArray(List<Vbo> vbos){
        this.vbos = vbos;
    }

    @Override
    public void init() throws InitializationException {

    }

    public static VertexArray create(float[] data, VertexTemplate... templates){
        List<Vbo> vbos = new ArrayList<>();

        int totalSize = Arrays.stream(templates)
                .sorted(Comparator.comparingInt(VertexTemplate::getIndex))
                .mapToInt(VertexTemplate::getElements)
                .sum();

        FloatBuffer verticesBuffer = null;
        try{
            MemoryUtil.memAllocFloat(data.length);
            verticesBuffer.put(data).flip();

            int offset = 0;
            for(VertexTemplate template : templates){
                Vbo vbo = Vbo.createBuffer(verticesBuffer, template.getIndex(), template.getElements(), totalSize, offset);
                vbos.add(vbo);

                offset += template.getElements();
            }

            return new VertexArray(vbos);
        }catch (Exception ex){
            return null;
        }finally {
            if(verticesBuffer != null){
                MemoryUtil.memFree(verticesBuffer);
            }
        }
    }

    public int getSize(){
        return vbos.size();
    }

    public List<Vbo> getVbos() {
        return vbos;
    }

    @Override
    public void dispose() {

    }
}
