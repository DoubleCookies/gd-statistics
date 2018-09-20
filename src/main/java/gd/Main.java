package gd;

import gd.enums.LevelType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        generateEpicMarkdownListForDiffs(0);
        generateFeaturedMarkdownListForDiffs(0);
        generateEpicWithLongestDescr();
        generateFeaturedWithLongestDescr();
        generateFeaturedAudioInfo();
        generateEpicAudioInfo();
        generateTopDemons();
        generateEpicBuildersInfo();
        generateFeaturedBuildersInfo();
    }

    private static void generateEpicMarkdownListForDiffs(int sortingCode) {
        String[] res = ResponseGenerator.generateList(sortingCode, LevelType.Epic);
        for(int j =0; j< 11; j++) //magic 11!
        {
            byte data[] = res[j].getBytes();
            String prefix = getDifficultName(j+1) + " epic";
            writeToFile(sortingCode, prefix, j+1, data, ".md");
        }
        writeToFile(sortingCode, "Epic", 0, res[11].getBytes(), ".md");
    }

    private static void generateFeaturedMarkdownListForDiffs(int sortingCode) {
        String[] res = ResponseGenerator.generateList(sortingCode ,LevelType.Featured);
        for(int j =0; j< 11; j++) //magic 11!
        {
            byte data[] = res[j].getBytes();
            String prefix = getDifficultName(j+1) + " featured";
            writeToFile(sortingCode, prefix, j+1, data, ".md");
        }
        writeToFile(sortingCode, "Featured", 0, res[11].getBytes(), ".md");
    }

    private static void generateEpicWithLongestDescr() {
        String[] res = ResponseGenerator.generateListWithLongestDescr(LevelType.Epic);
        writeToFile(5, "Epic", 0, res[0].getBytes(), ".md");
    }

    private static void generateFeaturedWithLongestDescr() {
        String[] res = ResponseGenerator.generateListWithLongestDescr(LevelType.Featured);
        writeToFile(5, "Featured", 0, res[0].getBytes(), ".md");
    }

    private static void generateFeaturedAudioInfo() {
        String[] res = ResponseGenerator.generateMusicList(LevelType.Featured);
        writeToFile(-1, "Featured audio info", 0, res[0].getBytes(), ".md");
    }

    private static void generateEpicAudioInfo() {
        String[] res = ResponseGenerator.generateMusicList(LevelType.Epic);
        writeToFile(-1, "Epic audio info", 0, res[0].getBytes(), ".md");
    }

    private static void generateFeaturedBuildersInfo() {
        String[] res = ResponseGenerator.generateBuildersList(LevelType.Featured);
        writeToFile(-1, "Featured builders info", 0, res[0].getBytes(), ".md");
    }

    private static void generateEpicBuildersInfo() {
        String[] res = ResponseGenerator.generateBuildersList(LevelType.Epic);
        writeToFile(-1, "Epic builders info", 0, res[0].getBytes(), ".md");
    }

    private static void generateTopDemons() {
        String[] res = ResponseGenerator.generateTopDemonsList();
        writeToFile(0, "Top 50 popular demons", 0, res[0].getBytes(), ".md");
    }


    private static void writeToFile(int sortingCode, String prefix, int diffCode, byte[] data, String filetype) {
        FileOutputStream out;
        try {
            out = getFileOutputStream(sortingCode, prefix, diffCode, filetype);
            out.write(data);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static FileOutputStream getFileOutputStream(int sortingCode, String prefix, int diffcode, String filetype) throws IOException {
        FileOutputStream out;
        String baseFolder = "Statistics";
        Path path = Paths.get(baseFolder);
        if(!Files.exists(path))
            Files.createDirectories(path);
        baseFolder +="/";
        String folder = getDifficultName(diffcode);
        String secondFolder="";
        if(!folder.equals(""))
        {
            String p = "Statistics/" + folder.trim();
            path = Paths.get(p);
            System.out.println(path.toAbsolutePath().toString());
            if(!Files.exists(path))
                Files.createDirectories(path);
            secondFolder=folder + "/";
        }

        switch (sortingCode)
        {
            case 1: { out = new FileOutputStream(baseFolder + secondFolder + prefix + " list with descending likes" + filetype); break;}
            case 2: { out = new FileOutputStream(baseFolder + secondFolder + prefix + " list with ascending likes"+ filetype); break;}
            case 3: { out = new FileOutputStream(baseFolder + secondFolder + prefix + " list with descending downloads" + filetype); break;}
            case 4: { out = new FileOutputStream(baseFolder + secondFolder + prefix + " list with ascending downloads" + filetype); break;}
            case 5: { out = new FileOutputStream(baseFolder + secondFolder + prefix + " list with longest descriptions" + filetype); break;}
            default: {out = new FileOutputStream(baseFolder + secondFolder + prefix + " list"+ filetype); break;}
        }
        return out;
    }

    private static String getDifficultName(int number) {
        switch (number)
        {
            case 1: {return "Auto";}
            case 2: {return "Easy";}
            case 3: {return "Normal";}
            case 4: {return "Hard";}
            case 5: {return "Harder";}
            case 6: {return "Insane";}
            case 7: {return "Easy demon";}
            case 8: {return "Medium demon";}
            case 9: {return "Hard demon";}
            case 10: {return "Insane demon";}
            case 11: {return "Extreme demon";}
            default: {return "";}
        }
    }
}
