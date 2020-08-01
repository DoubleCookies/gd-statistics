package gd;

import gd.enums.SortingCode;
import gd.model.EmptyListException;
import org.apache.log4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    final static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) throws EmptyListException {

        processFeatured(SortingCode.DEFAULT.getValue());
        processEpic(SortingCode.DEFAULT.getValue());
        generateTopDemons();
    }

    private static void processFeatured(int sortingCode) throws EmptyListException {
        String[] res = ResponseGenerator.processLevels(sortingCode);
        if (res == null) {
            return;
        }
        for(int j = 0; j < 11; j++)
        {
            String prefix = getDifficultName(j+1) + " featured";
            writeToFile(sortingCode, prefix, j+1, res[j].getBytes());
        }
        writeToFile(sortingCode, "Featured", 0, res[11].getBytes());
        writeToFile(SortingCode.LONGEST_DESCRIPTION.getValue(), "Featured", 0, res[12].getBytes());
        writeToFile(SortingCode.DEFAULT.getValue(), "Featured audio info", 0, res[13].getBytes());
        writeToFile(SortingCode.DEFAULT.getValue(), "Featured audio info expanded", 0, res[14].getBytes());
        writeToFile(SortingCode.DEFAULT.getValue(), "Featured builders info", 0, res[15].getBytes());
        logger.info("All featured lists are finished");
    }

    private static void processEpic(int sortingCode) throws EmptyListException {
        String[] res = ResponseGenerator.processLevels(sortingCode);
        for(int j = 0; j < 11; j++)
        {
            String prefix = getDifficultName(j+1) + " epic";
            writeToFile(sortingCode, prefix, j+1, res[j].getBytes());
        }
        writeToFile(sortingCode, "Epic", 0, res[11].getBytes());
        writeToFile(SortingCode.LONGEST_DESCRIPTION.getValue(), "Epic", 0, res[12].getBytes());
        writeToFile(SortingCode.DEFAULT.getValue(), "Epic audio info", 0, res[13].getBytes());
        writeToFile(SortingCode.DEFAULT.getValue(), "Epic audio info expanded", 0, res[14].getBytes());
        writeToFile(SortingCode.DEFAULT.getValue(), "Epic builders info", 0, res[15].getBytes());
        logger.info("All epic lists are finished");
    }

    private static void generateTopDemons() {
        String[] res = ResponseGenerator.generateTopDemonsList();
        writeToFile(SortingCode.DEFAULT.getValue(), "Top 50 popular demons", 0, res[0].getBytes());
    }


    private static void writeToFile(int sortingCode, String prefix, int difficultyCode, byte[] data) {
        FileOutputStream out;
        try {
            out = getFileOutputStream(sortingCode, prefix, difficultyCode);
            out.write(data);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static FileOutputStream getFileOutputStream(int sortingCode, String prefix, int diffcode) throws IOException {
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
            if(!Files.exists(path))
                Files.createDirectories(path);
            secondFolder=folder + "/";
        }

        switch (sortingCode)
        {
            case 1: { out = new FileOutputStream(baseFolder + secondFolder + prefix + " list with descending likes" + ".md"); break;}
            case 2: { out = new FileOutputStream(baseFolder + secondFolder + prefix + " list with ascending likes" + ".md"); break;}
            case 3: { out = new FileOutputStream(baseFolder + secondFolder + prefix + " list with descending downloads" + ".md"); break;}
            case 4: { out = new FileOutputStream(baseFolder + secondFolder + prefix + " list with ascending downloads" + ".md"); break;}
            case 5: { out = new FileOutputStream(baseFolder + secondFolder + prefix + " list with longest descriptions" + ".md"); break;}
            default: { out = new FileOutputStream(baseFolder + secondFolder + prefix + " list" + ".md"); break;}
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
