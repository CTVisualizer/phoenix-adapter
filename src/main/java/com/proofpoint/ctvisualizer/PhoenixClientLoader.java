package com.proofpoint.ctvisualizer;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

public class PhoenixClientLoader {

    public void loadPhoenixClient(Path phoenixClientPath) {
        try {
            URLClassLoader loader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(loader, phoenixClientPath.toUri().toURL());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
