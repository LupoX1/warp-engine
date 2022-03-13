package com.codersdungeon.warp.engine.graphics;

import com.codersdungeon.warp.engine.exceptions.InitializationException;
import com.codersdungeon.warp.engine.util.Resources;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Graphics {
    private static final Logger LOG = LoggerFactory.getLogger(Graphics.class);

    public static ShaderProgram createShaderProgram(String vertexShader, String fragmentShader) throws InitializationException {
        LOG.debug("create shader program: '{}', '{}'", vertexShader, fragmentShader);

        int programId = glCreateProgram();
        if(programId == 0){
            throw new InitializationException("Cannot create shader program");
        }

        String vertexSource = Resources.loadTextResource(vertexShader);
        String fragmentSource = Resources.loadTextResource(fragmentShader);

        ShaderProgram shaderProgram = new ShaderProgram(programId);
        shaderProgram.loadShaders(vertexSource, fragmentSource);
        return shaderProgram;
    }

    private static Texture createTexture(String imagePath) throws InitializationException {
        LOG.debug("create texture: '{}'", imagePath);

        int textureId = glGenTextures();
        if(textureId == 0){
            throw new InitializationException("Cannot create texture");
        }

        Image image = Resources.loadPng(imagePath);

        try{
            Texture texture = new Texture(textureId);
            texture.bind();
            //texture.setParameters();
            texture.setData(image);
            return texture;
        }catch (Exception ex){
            throw new InitializationException(ex);
        }finally {
            image.dispose();
        }
    }

    public static Mesh createMesh(float[] vertices, int[] indices, List<VertexTemplate> templateList, String texturePath) throws InitializationException {
        LOG.debug("create mesh");

        templateList.sort(Comparator.comparingInt(VertexTemplate::getIndex));

        VertexArray vertexArray = createVertexArray();
        vertexArray.bind();
        List<VertexBuffer> vertexBuffers = createVertexBuffers(vertices, templateList);
        ElementBuffer elementBuffer = createElementBuffer(indices);
        vertexArray.unbind();

        Texture texture = createTexture(texturePath);

        Mesh mesh = new Mesh(vertexArray, vertexBuffers, elementBuffer, texture);
        return mesh;
    }

    private static VertexArray createVertexArray() throws InitializationException {
        LOG.debug("create vertex array");

        int vaoID = glGenVertexArrays();
        if(vaoID == 0){
            throw new InitializationException("Cannot create vertex array");
        }
        VertexArray vertexArray = new VertexArray(vaoID);
        return vertexArray;
    }

    private static List<VertexBuffer> createVertexBuffers(float[] vertices, List<VertexTemplate> templateList) throws InitializationException {
        LOG.debug("create vertex buffers");

        List<VertexBuffer> result = new ArrayList<>();

        int totalSize = templateList.stream().mapToInt(VertexTemplate::getElements).sum();
        FloatBuffer floatBuffer = null;
        try {
            floatBuffer = MemoryUtil.memAllocFloat(vertices.length);
            floatBuffer.put(vertices).flip();

            int offset = 0;
            for (VertexTemplate template : templateList) {
                VertexBuffer vbo = createVertexBuffer(floatBuffer, template.getIndex(), template.getElements(), totalSize, offset);
                result.add(vbo);
                offset += template.getElements();
            }

            return result;
        }catch (Exception ex){
            throw new InitializationException(ex);
        }finally {
            if(floatBuffer != null){
                MemoryUtil.memFree(floatBuffer);
            }
        }
    }

    private static VertexBuffer createVertexBuffer(FloatBuffer floatBuffer, int index, int size, int stride, int offset) throws InitializationException {
        LOG.debug("create vertex buffer: Index '{}' Size '{}' Stride '{}' Offset '{}'", index, size, stride, offset);

        int vboID = glGenBuffers();
        if(vboID == 0){
            throw new InitializationException("Cannot create vertex buffer");
        }
        VertexBuffer vertexBuffer = new VertexBuffer(vboID, index);
        vertexBuffer.bind();
        glBufferData(GL_ARRAY_BUFFER, floatBuffer, GL_STATIC_DRAW);
        vertexBuffer.enable();
        glVertexAttribPointer(index, size, GL_FLOAT, false, stride * Float.BYTES, (long) offset * Float.BYTES);
        vertexBuffer.unbind();

        return vertexBuffer;
    }

    private static ElementBuffer createElementBuffer(int[] indices) throws InitializationException {
        LOG.debug("create element buffer");

        int eboID = glGenBuffers();
        if(eboID == 0){
            throw new InitializationException("Cannot create element buffer");
        }
        ElementBuffer elementBuffer = new ElementBuffer(eboID, indices.length);

        IntBuffer indicesBuffer = null;
        try{
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();

            elementBuffer.bind();
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
            elementBuffer.unbind();

            return elementBuffer;
        }catch (Exception ex){
            throw new InitializationException(ex);
        }finally {
            if(indicesBuffer != null){
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }

    private static void premultiplyAlpha(int w, int h, ByteBuffer image){
        int stride = w * 4;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int i = y * stride + x * 4;

                float alpha = (image.get(i + 3) & 0xFF) / 255.0f;
                image.put(i + 0, (byte) Math.round(((image.get(i + 0) & 0xFF) * alpha)));
                image.put(i + 1, (byte) Math.round(((image.get(i + 1) & 0xFF) * alpha)));
                image.put(i + 2, (byte) Math.round(((image.get(i + 2) & 0xFF) * alpha)));
            }
        }
    }

}
