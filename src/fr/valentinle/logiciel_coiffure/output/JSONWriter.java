package fr.valentinle.logiciel_coiffure.output;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class JSONWriter<T> {
    
    public void write(T object, String filename) {
	// .setPrettyPrinting()
	Gson gson = new GsonBuilder().create();
	File file = new File(filename);
	BufferedWriter writer = null;
	try {
	    String content = gson.toJson(object);
	    if (!file.exists()) {
		file.createNewFile();
	    }
	    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getPath()), StandardCharsets.UTF_8));
	    writer.write(content);

	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (writer != null) {
		    writer.close();
		}
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }
}
