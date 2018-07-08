package gd;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        generateEpicMarkdownList(0, "Epic", 0);
        generateFeaturedMarkdownList(0, "Featured", 0);
        //generateEpicList(0, "Epic", 0);
        //generateFeaturedList(0, "Featured", 0);
        //generateAwardedList(0, "Awarded", 0);
        for(int i = 1; i < 12; i++)
        {
            generateEpicMarkdownList(0, getDifficultName(i) + " epic", i);
            generateFeaturedMarkdownList(0, getDifficultName(i) + " featured", i);
        }
    }

    private static void generateEpicList(int sortingCode, String prefix, int diffCode) {

        String res = ResponseGenerator.generateEpicList(sortingCode, diffCode);
        byte data[] = res.getBytes();
        FileOutputStream out;
        writeToFile(sortingCode, prefix, diffCode, data, ".txt");

    }

    private static void generateEpicMarkdownList(int sortingCode, String prefix, int diffCode) {

        String res = ResponseGenerator.generateEpicMarkdownList(sortingCode, diffCode);
        byte data[] = res.getBytes();
        FileOutputStream out;
        writeToFile(sortingCode, prefix, diffCode, data, ".md");
    }

    private static void generateFeaturedList(int sortingCode, String prefix, int diffCode) {

        String res = ResponseGenerator.generateFeaturedList(sortingCode, diffCode);
        byte data[] = res.getBytes();
        writeToFile(sortingCode, prefix, diffCode, data, ".txt");
    }

    private static void generateFeaturedMarkdownList(int sortingCode, String prefix, int diffCode) {

        String res = ResponseGenerator.generateFeaturedMarkdownList(sortingCode, diffCode);
        byte data[] = res.getBytes();
        FileOutputStream out;
        writeToFile(sortingCode, prefix, diffCode, data, ".md");
    }

    private static void generateAwardedList(int sortingCode, String prefix, int diffCode) {

        String res = ResponseGenerator.generateAwardedList(sortingCode, diffCode);
        byte data[] = res.getBytes();
        writeToFile(sortingCode, prefix, diffCode, data, ".txt");
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
        Path path = Paths.get("results");
        System.out.println(path.toAbsolutePath().toString());
        if(!Files.exists(path))
            Files.createDirectories(path);
        String folder = getDifficultName(diffcode);
        String secondFolder="";
        if(!folder.equals(""))
        {
            String p = "results/" + folder.trim();
            path = Paths.get(p);
            System.out.println(path.toAbsolutePath().toString());
            if(!Files.exists(path))
                Files.createDirectories(path);
            secondFolder=folder + "/";
        }

        switch (sortingCode)
        {
            case 1: { out = new FileOutputStream("results/" + secondFolder + prefix + " list with descending likes" + filetype); break;}
            case 2: { out = new FileOutputStream("results/" + secondFolder + prefix + " list with ascending likes"+ filetype); break;}
            case 3: { out = new FileOutputStream("results/" + secondFolder + prefix + " list with descending downloads" + filetype); break;}
            case 4: { out = new FileOutputStream("results/" + secondFolder + prefix + " list with ascending downloads" + filetype); break;}
            default: {out = new FileOutputStream("results/" + secondFolder + prefix + " list"+ filetype); break;}
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
