package gd.service;

import gd.SortingCode;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class SaveResultsService {

    public static void writeToFileWikiLists(String fileNamePrefix, byte[] data) {
        writeToFile(SortingCode.DEFAULT, "Wiki", fileNamePrefix, 0, data);
    }

    public static void writeToFileDemonsList(byte[] data) {
        writeToFile(SortingCode.DEFAULT, "Regular", "Top 50 popular demons", 0, data);
    }

    public static void writeToFileRegularLists(SortingCode sortingCode, String fileNamePrefix, int difficultyCode, byte[] data) {
        writeToFile(sortingCode, "Regular", fileNamePrefix, difficultyCode, data);
    }

    public static void writeToFile(SortingCode sortingCode, String subfolder, String fileNamePrefix, int difficultyCode, byte[] data) {
        FileOutputStream out;
        try {
            out = getFileOutputStream(sortingCode, subfolder, fileNamePrefix, difficultyCode);
            out.write(data);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static FileOutputStream getFileOutputStream(SortingCode sortingCode, String subfolder, String prefix, int difficultyFolder) throws IOException {
        String folder = "Statistics/" + subfolder + "/";
        if (!difficultyFolderMap.get(difficultyFolder).equals(""))
            folder += difficultyFolderMap.get(difficultyFolder) + "/";
        Path path = Paths.get(folder);
        if (!Files.exists(path))
            Files.createDirectories(path);

        return new FileOutputStream(folder + prefix + sortingFileSuffix.get(sortingCode));
    }

    //Difficulties map for folder names
    public static final Map<Integer, String> difficultyFolderMap = initDifficultyFolderMap();

    private static Map<Integer, String> initDifficultyFolderMap() {
        Map<Integer, String> map = new HashMap<>();
        map.put(0, "");
        map.put(1, "Auto");
        map.put(2, "Easy");
        map.put(3, "Normal");
        map.put(4, "Hard");
        map.put(5, "Harder");
        map.put(6, "Insane");
        map.put(7, "Easy demon");
        map.put(8, "Medium demon");
        map.put(9, "Hard demon");
        map.put(10, "Insane demon");
        map.put(11, "Extreme demon");
        return map;
    }

    //List names for different sorting types
    public static final Map<SortingCode, String> sortingFileSuffix = initSortingFileSuffix();

    private static Map<SortingCode, String> initSortingFileSuffix() {
        Map<SortingCode, String> map = new HashMap<>();
        map.put(SortingCode.DEFAULT, " list.md");
        map.put(SortingCode.DESCENDING_LIKES, " list with descending likes.md");
        map.put(SortingCode.ASCENDING_LIKES, " list with ascending likes.md");
        map.put(SortingCode.DESCENDING_DOWNLOADS, " list with descending downloads.md");
        map.put(SortingCode.ASCENDING_DOWNLOADS, " list with ascending downloads.md");
        map.put(SortingCode.LONGEST_DESCRIPTION, " list with longest descriptions.md");
        return map;
    }
}
