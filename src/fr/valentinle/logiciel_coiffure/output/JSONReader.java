package fr.valentinle.logiciel_coiffure.output;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JSONReader<T> {
    
    public T read(String filename, Type type) {
	try {
	    File file = new File(filename);
	    if (!file.exists()) {
		file.createNewFile();
	    }
	    Gson gson = new GsonBuilder().create();
	    String content = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
	    return gson.fromJson(content, type);
	} catch (IOException ex) {
	    ex.printStackTrace();
	}
	return null;
    }
}
