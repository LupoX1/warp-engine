package com.codersdungeon.warp.engine.util;

import com.codersdungeon.warp.engine.graphics.Image;
import com.codersdungeon.warp.engine.graphics.ModelData;
import com.codersdungeon.warp.engine.graphics.VertexTemplate;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Resources {
    private static final Logger LOG = LoggerFactory.getLogger(Resources.class);

    private Resources(){}

    public static ByteBuffer loadResource(String resource){
        try(InputStream inputStream = Resources.class.getClassLoader().getResourceAsStream(resource);
            ReadableByteChannel channel = Channels.newChannel(inputStream)){
            ByteBuffer byteBuffer = BufferUtils.createByteBuffer(inputStream.available());
            channel.read(byteBuffer);
            return byteBuffer;
        } catch (Exception e) {
            LOG.error("Error loading resource '" + resource + "'", e);
            throw new RuntimeException(e);
        }
    }

    public static String loadTextResource(String resource){
        Charset charset = StandardCharsets.UTF_8;
        ByteBuffer textBuffer = loadResource(resource);
        textBuffer.flip();
        String resourceContent = charset.decode(textBuffer).toString();
        textBuffer.clear();
        return resourceContent;
    }

    public static ModelData loadFromResource(String resource){
        String modelContent = Resources.loadTextResource(resource);
        String[] temp = modelContent.split("\r\n");

        String[] verticesData = temp[0].split(",");
        String[] indicesData = temp[1].split(",");
        float[] vertices = new float[verticesData.length];
        for(int i=0; i < vertices.length; i++){
            vertices[i] = Float.parseFloat(verticesData[i]);
        }
        int[] indices = Arrays.stream(indicesData).mapToInt(Integer::valueOf).toArray();
        List<VertexTemplate> vertexTemplates = new ArrayList<>();

        int templateIndex = 0;
        for(int i = 2; i< temp.length; i++){
            String[] vertexTemplate = temp[i].split(",");
            VertexTemplate template = new VertexTemplate(templateIndex, Integer.parseInt(vertexTemplate[0]), vertexTemplate[1]);
            vertexTemplates.add(template);
            templateIndex++;
        }

        return new ModelData(vertices, indices, vertexTemplates);
    }

    public static void serializeModel(String resource, ModelData model){
        Path output = Paths.get(resource);
        try {
            Files.deleteIfExists(output);
            Files.createFile(output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try(ObjectOutputStream outputStream = new ObjectOutputStream(Files.newOutputStream(output))){
            outputStream.writeObject(model);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ModelData deserializeModel(String resource){
        ByteBuffer buffer = loadResource(resource);
        byte[] temp = new byte[buffer.limit()];
        buffer.flip();
        buffer.get(temp);
        try(ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(temp))){
            return (ModelData)inputStream.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            buffer.clear();
        }
    }

    public static Image loadPng(String imagePath){
        ByteBuffer imageBuffer = loadResource(imagePath);
        imageBuffer.flip();

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w    = stack.mallocInt(1);
            IntBuffer h    = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            ByteBuffer buffer = STBImage.stbi_load_from_memory(imageBuffer, w, h, channels, 0);
            if (buffer == null) {
                throw new RuntimeException("Failed to load image '"+imagePath+"' : " + STBImage.stbi_failure_reason());
            }

            int width = w.get(0);
            int height = h.get(0);
            int components = channels.get(0);

            return new Image(width, height, components, buffer);
        }finally {
            imageBuffer.clear();
        }
    }
}
