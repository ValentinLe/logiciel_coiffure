package fr.valentinle.logiciel_coiffure.output;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.Objects;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


public class JsonUtils {

    public static <T> String toJson(T object, boolean pretty) {
        Gson gson;
        if (pretty) {
            gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        } else {
            gson = new GsonBuilder().create();
        }
        return gson.toJson(object);
    }

    public static <T> String toJson(T object) {
        return toJson(object, true);
    }

    public static <T> T fromJson(String json, Type type) {
        return new GsonBuilder().create().fromJson(json, type);
    }

    public static <T> void write(String filename, T object, boolean pretty) {
        File file = new File(filename);
        BufferedWriter writer = null;
        try {
            String content = toJson(object, pretty);
            if (!file.exists()) {
                boolean creationResult = file.createNewFile();
                if (!creationResult) {
                    System.out.println("Write : File " + filename + " wasn't created");
                }
            }
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getPath()), StandardCharsets.UTF_8));
            writer.write(content);
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (Objects.nonNull(writer)) {
                    writer.close();
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static <T> void write(String filename, T object) {
        write(filename, object, true);
    }

    public static <T> T read(String filename, Type type) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                boolean creationResult = file.createNewFile();
                if (!creationResult) {
                    System.out.println("Read : File " + filename + " wasn't created");
                }
            }
            String content = Files.readString(Paths.get(filename));
            return fromJson(content, type);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}