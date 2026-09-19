package lib.executor.stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.*;
import java.util.concurrent.ConcurrentHashMap;

public class PipelineRepository extends ClassLoader {

    private static final String HURL = "/handler/";
    private static final String RURL = "/pipeline/";

    private final String classDir;
    private final String repoDir;
    private final ConcurrentHashMap<String, Handler> services = new ConcurrentHashMap<>();

    public PipelineRepository(String classDir, String repoDir) {
        this.classDir = classDir;
        this.repoDir = repoDir;
    }

    private Class<?> loadClassFromFile(String className) throws IOException {
        File classFile = new File(classDir + className + ".class");
        byte[] classBytes = null;
        if (!classFile.exists()) {
            classBytes = loadClassFromURI(className);
        } else {
            classBytes = Files.readAllBytes(classFile.toPath());
        }
        return defineClass("lib.executor.stage." + className, classBytes, 0, classBytes.length);
    }

    private byte[] loadClassFromURI(String className) {
        String serverUrl = repoDir + HURL + className;
        try {
            URL url = new URL(serverUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                byte[] buffer = inputStream.readAllBytes();
                inputStream.close();
                return buffer;
            } else {
                System.out.println("error:" + responseCode);
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Handler getHandler(String handler) {
        handler = handler + "Handler";

        return services.computeIfAbsent(handler, key -> {
            try {
                Class<?> implClass = loadClassFromFile(key);
                return (Handler) implClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                System.out.println("Error loading handler: " + key);
                e.printStackTrace();
                return null;
            }
        });
        /*
        non atomic causes hidden exception, so we use computeIfAbsent to ensure atomicity
        if (!services.containsKey(handler)) {
            try {
                Class<?> implClass = loadClassFromFile(handler);
                services.put(handler, (Handler) implClass.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                System.out.println("Error loading handler: " + handler);
                e.printStackTrace();
            }
        }
        return services.get(handler); */
    }

    public String getPipeline(String type) {
        String serverUrl = repoDir + RURL + type;
        try {
            URL url = new URL(serverUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                byte[] buffer = inputStream.readAllBytes();
                inputStream.close();
                return new String(buffer);
            } else {
                System.out.println("error:" + responseCode);
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
