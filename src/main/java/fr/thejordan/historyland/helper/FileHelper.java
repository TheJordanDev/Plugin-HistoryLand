package fr.thejordan.historyland.helper;

import fr.thejordan.historyland.Historyland;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileHelper {

    public static File[] getFilesInFolder(File folder) {
        if (!folder.exists()) folder.mkdirs();
        return folder.listFiles(((dir, name) -> name.endsWith(".yml")));
    }

    public static InputStreamReader getStreamFromResource(String fileName) {
        InputStream stream = Historyland.instance().getResource(fileName);
        if (stream == null) return null;
        else return new InputStreamReader(stream);
    }

}
