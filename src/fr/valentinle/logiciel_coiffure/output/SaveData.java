package fr.valentinle.logiciel_coiffure.output;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;

public class SaveData {
    
    public static LocalDate saveData(String sourceFolder, List<String> sinkFolders, LocalDate date) {
	for (String folder : sinkFolders) {
	    File folderFile = new File(folder);
	    if (folderFile.exists()) {
		String rootDirStr = folder.concat("/data_coiffure");
		File rootDir = new File(rootDirStr);
		if (!rootDir.exists()) {
		    rootDir.mkdir();
		}
		String dirName = date.getYear() + "_" + date.getMonthValue() + "_" + date.getDayOfMonth();
		File dir = new File(rootDirStr + "/" + dirName);
		if (!dir.exists()) {
		    dir.mkdir();
		} else {
		    try {
			FileDeleteStrategy.FORCE.delete(dir);
		    } catch (IOException e) {
			e.printStackTrace();
		    }
		    dir.mkdir();
		}
		File source = new File(sourceFolder);
		try {
		    FileUtils.copyDirectory(source, dir);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    } else {
		System.out.println("DIR \"" + folder + "\" doesn't exists");
	    }
	}
	return date;
    }
    
    public static LocalDate saveData(String sourceFolder, List<String> sinkFolders) {
	return saveData(sourceFolder, sinkFolders, LocalDate.now());
    }
    
    public static List<String> loadSavePoints(String filename) {
		System.out.println("Read " + filename);
	List<String> savePoints = new ArrayList<>();
	File file = new File(filename);
	BufferedReader br;
	try {
	    if (!file.exists()) {
		file.createNewFile();
	    }
	    br = new BufferedReader(new FileReader(filename));
	    String line;
	    while ((line = br.readLine()) != null) {
		savePoints.add(line.trim());
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	return savePoints;
    }
    
    public static void writeSavePoints(String filename, List<String> savePoints) {
		System.out.println("Write " + filename);
	FileWriter fw;
	File file = new File(filename);
	try {
	    if (!file.exists()) {
		file.createNewFile();
	    }
	    fw = new FileWriter(filename);
	    for (int i = 0; i < savePoints.size(); i++) {
		    fw.write(savePoints.get(i));
		    if (i < savePoints.size() - 1) {
			fw.write("\n");
		    }
	    }
	    fw.close();
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }
}
