package gd;

import gd.enums.DemonDifficulty;
import gd.enums.Difficulty;
import gd.model.GDLevel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Class for generating level lists
 */
public class ResponseGenerator {

    private static Comparator<GDLevel> descendingLikesComparator = (o1, o2) -> (int) (o2.getLikes() - o1.getLikes());
    private static Comparator<GDLevel> ascendingLikesComparator = (o1, o2) -> (int) (o1.getLikes() - o2.getLikes());

    private static Comparator<GDLevel> descendingDownloadsComparator = (o1, o2) -> (int) (o2.getDownloads() - o1.getDownloads());
    private static Comparator<GDLevel> ascendingDownloadsComparator = (o1, o2) -> (int) (o1.getDownloads() - o2.getDownloads());

    private static List<GDLevel> getMostPopularEpics(int diffCode) {
        List<GDLevel> list = new ArrayList<>();

        int i = 0;
        try {

            while(true) {
                String res = GDServer.fetchRecentEpicLevels(i);
                addingSelection(diffCode, list, i, res);

                i++;
            }
        } catch (Exception e) {
            System.out.println("Limit reached!");
        }
        return list;
    }

    private static List<GDLevel> getMostPopularFeatured(int diffCode) {
        List<GDLevel> list = new ArrayList<>();

        int i =0;
        try {

            while(true) {
                String res = GDServer.fetchRecentFeaturedLevels(i);
                addingSelection(diffCode, list, i, res);
                i++;
            }
        } catch (Exception e) {
            System.out.println("Limit reached!");
        }
        return list;
    }

    private static List<GDLevel> getMostPopularAwarded(int diffCode) {
        List<GDLevel> list = new ArrayList<>();

        int i =0;
        try {

            while(true) {
                String res = GDServer.fetchRecentAwardedLevels(i);
                addingSelection(diffCode, list, i, res);
                i++;
            }
        } catch (Exception e) {
            System.out.println("Limit reached!");
        }
        return list;
    }

    static String generateEpicList(int sortingCode, int diffCode) {
        List<GDLevel> list = getMostPopularEpics(diffCode);
        sortLevelList(list, sortingCode);
        StringBuilder result = new StringBuilder();
        int i = 1;
        for(GDLevel level : list)
        {
            result.append(i+". " + level.toString() + "\r\n");
            i++;
        }
        System.out.println("There are " + list.size() + " levels!");
        return result.toString();
    }

    static String generateFeaturedList(int sortingCode, int diffCode) {
        List<GDLevel> list = getMostPopularFeatured(diffCode);
        sortLevelList(list, sortingCode);
        StringBuilder result = new StringBuilder();
        int i = 1;
        for(GDLevel level : list)
        {
            result.append(i+". " + level.toString() + "\r\n");
            i++;
        }
        System.out.println("There are " + list.size() + " levels!");
        return result.toString();
    }

    static String generateEpicMarkdownList(int sortingCode, int diffCode) {
        List<GDLevel> list = getMostPopularEpics(diffCode);
        sortLevelList(list, sortingCode);
        StringBuilder result = new StringBuilder();
        result.append("| Name | Creator | ID | Downloads | Likes |\n");
        result.append("|:---:|:---:|:---:|:---:|:---:|\n");
        for(GDLevel level : list)
            result.append(level.markdownString() + "\n");
        result.insert(0, "####Total: " + list.size() + " levels\n\n");
        return result.toString();
    }

    static String generateFeaturedMarkdownList(int sortingCode, int diffCode) {
        List<GDLevel> list = getMostPopularFeatured(diffCode);
        sortLevelList(list, sortingCode);
        StringBuilder result = new StringBuilder();
        result.append("| Name | Creator | ID | Downloads | Likes |\n");
        result.append("|:---:|:---:|:---:|:---:|:---:|\n");
        for(GDLevel level : list)
            result.append(level.markdownString() + "\n");
        result.insert(0, "####Total: " + list.size() + " levels\n\n");
        return result.toString();
    }

    static String generateAwardedList(int sortingCode, int diffCode) {
        List<GDLevel> list = getMostPopularAwarded(diffCode);
        sortLevelList(list, sortingCode);
        StringBuilder result = new StringBuilder();
        int i = 1;
        for(GDLevel level : list)
        {
            result.append(i+". " + level.toString() + "\r\n");
            i++;
        }
        System.out.println("There are " + list.size() + " levels!");
        return result.toString();
    }


    private static void addLevelsToList(List<GDLevel> list, int i, String res) {
        for (int j = 0; j < 10; j++) {
            int index = i * 10 + j;
            GDLevel level = getLevel(j, res);
            if (level != null)
                list.add(level);
            System.out.println("Level #" + index + " checked");
        }
    }

    private static void addLevelsToList(List<GDLevel> list, int i, String res, Difficulty difficulty) {
        for (int j = 0; j < 10; j++) {
            int index = i * 10 + j;
            GDLevel level = getLevel(j, res);
            if (level != null && level.getDifficulty() == difficulty)
                list.add(level);
            System.out.println("Level #" + index + " checked");
        }
    }

    private static void addLevelsToList(List<GDLevel> list, int i, String res, DemonDifficulty demonDifficulty) {
        for (int j = 0; j < 10; j++) {
            int index = i * 10 + j;
            GDLevel level = getLevel(j, res);
            if (level != null && level.getDifficulty() == Difficulty.DEMON && level.getDemonDifficulty() == demonDifficulty)
                list.add(level);
            System.out.println("Level #" + index + " checked");
        }
    }

    private static GDLevel getLevel(int j, String res) {
        return GDLevelFactory.buildGDLevelSearchedByFilter(res, j, false);
    }

    private static void addingSelection(int diffCode, List<GDLevel> list, int i, String res) {
        switch (diffCode)
        {
            case 1: {addLevelsToList(list, i, res, Difficulty.AUTO); break;}
            case 2: {addLevelsToList(list, i, res, Difficulty.EASY); break;}
            case 3: {addLevelsToList(list, i, res, Difficulty.NORMAL); break;}
            case 4: {addLevelsToList(list, i, res, Difficulty.HARD); break;}
            case 5: {addLevelsToList(list, i, res, Difficulty.HARDER); break;}
            case 6: {addLevelsToList(list, i, res, Difficulty.INSANE); break;}
            case 7: {addLevelsToList(list, i, res, DemonDifficulty.EASY); break;}
            case 8: {addLevelsToList(list, i, res, DemonDifficulty.MEDIUM); break;}
            case 9: {addLevelsToList(list, i, res, DemonDifficulty.HARD); break;}
            case 10: {addLevelsToList(list, i, res, DemonDifficulty.INSANE); break;}
            case 11: {addLevelsToList(list, i, res, DemonDifficulty.EXTREME); break;}
            default: {addLevelsToList(list, i, res); break;}
        }
    }

    private static void sortLevelList(List<GDLevel> list, int code) {
        switch (code)
        {
            case 1: { list.sort(descendingLikesComparator); break;}
            case 2: { list.sort(ascendingLikesComparator); break;}
            case 3: { list.sort(descendingDownloadsComparator); break;}
            case 4: { list.sort(ascendingDownloadsComparator); break;}
        }
    }
}
