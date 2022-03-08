package com.codersdungeon.warp.engine.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class Resources {
    private static final Logger LOG = LoggerFactory.getLogger(Resources.class);

    private Resources(){}

    public static String loadString(String resource){
        try (   ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                BufferedInputStream inputStream = new BufferedInputStream(Objects.requireNonNull(Resources.class.getClassLoader().getResourceAsStream(resource)))){
            byte[] buffer = new byte[4096];
            int read;
            while((read = inputStream.read(buffer)) != -1){
                byteArrayOutputStream.write(buffer, 0, read);
            }
            return byteArrayOutputStream.toString("UTF-8");
        } catch (Exception e) {
            LOG.error("Error loading resource '" + resource + "'", e);
        }
        return null;
    }
}
