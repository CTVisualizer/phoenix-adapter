package com.proofpoint.ctvisualizer;



import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class PhoenixClientLoader {

    private Map<String, String> params;

    @Inject
    public PhoenixClientLoader(@Named("params")Map<String, String> params) {
        this.params = params;
    }

    public void loadPhoenixClient() {
        try {
            System.out.println("Params: "+ params.toString());
            Path phoenixClientPath = Paths.get(params.get("phoenixClient"));
            URLClassLoader loader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(loader, phoenixClientPath.toUri().toURL());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
