package lib.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.TreeMap;

public class Cache {
    private TreeMap<String, String> cacheMap;

    public Cache() {
        this.cacheMap = new TreeMap<>();
    }

    public void put(String key, String value) {
        cacheMap.put(key, value);
    }

    public String get(String key) {
        return cacheMap.get(key);
    }

    public boolean hasKey(String key) {
        return cacheMap.containsKey(key);
    }

    public TreeMap<String, String> getAll() {
        return cacheMap;
    }

    public void load(String id,String location) {
        System.out.println("Loading cache from " + location);
        // Implementation for loading cache from a location
        File file = new File(location);
        if (file.exists()) {
            //read as string
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // Assuming the cache is stored as key=value pairs
                    String[] parts = line.split("=", 2);
                    if (parts.length == 2) {
                        cacheMap.put(parts[0], parts[1]);
                        System.out.println("Loaded cache entry: " + parts[0] + "=" + parts[1]);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            cacheMap.put(id, "loaded");
        }
    }

    public void save(String id,String location) {
        // Implementation for saving cache to a location
        // This is a placeholder; actual implementation would write to a file
        System.out.println("Cache saved to " + location);
        cacheMap.put(id, "saved" + System.currentTimeMillis());
        File file = new File(location);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            // Write the cacheMap to the file
            java.io.FileWriter writer = new java.io.FileWriter(file);
            for (String key : cacheMap.keySet()) {
                writer.write(key + "=" + cacheMap.get(key) + "\n");
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
