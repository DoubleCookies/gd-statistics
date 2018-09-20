package gd;

import gd.enums.DemonDifficulty;
import gd.enums.Difficulty;
import gd.enums.LevelType;
import gd.model.GDLevel;
import gd.model.GDSong;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Class for generating level lists
 */
public class ResponseGenerator {

    private static Comparator<GDLevel> descendingLikesComparator = (o1, o2) -> (int) (o2.getLikes() - o1.getLikes());
    private static Comparator<GDLevel> ascendingLikesComparator = (o1, o2) -> (int) (o1.getLikes() - o2.getLikes());
    private static Comparator<GDLevel> descendingDownloadsComparator = (o1, o2) -> (int) (o2.getDownloads() - o1.getDownloads());
    private static Comparator<GDLevel> ascendingDownloadsComparator = (o1, o2) -> (int) (o1.getDownloads() - o2.getDownloads());
    private static Comparator<GDLevel> descriptionLengthComparator = (o1, o2) -> (int) (o2.getDescription().length() - o1.getDescription().length());

    static String[] generateList(int sortingCode, LevelType type) {
        int length = 12;
        String[] stringArray = new String[length];
        StringBuilder[] builders = new StringBuilder[length];
        int[] counter = new int[length];
        for(int i =0; i < length;i++)
        {
            builders[i] = new StringBuilder();
            builders[i].append("| Name | Creator | ID | Downloads | Likes |\n");
            builders[i].append("|:---:|:---:|:---:|:---:|:---:|\n");
        }
        List<GDLevel> list = getLevelsList(type);
        sortLevelList(list, sortingCode);
        for(GDLevel level : list)
        {
            int i = returnDiff(level);
            builders[i].append(level.markdownString() + "\n");
            counter[i]++;
            builders[length-1].append(level.markdownString() + "\n");
        }
        for(int i =0; i < length-1;i++)
        {
            builders[i].insert(0, "#### Total: " + counter[i] + " levels\n\n");
            stringArray[i] = builders[i].toString();
        }
        builders[length-1].insert(0, "#### Total: " + IntStream.of(counter).sum() + " levels\n\n");
        stringArray[length-1] = builders[length-1].toString();
        return stringArray;
    }

    static String[] generateListWithLongestDescr(LevelType type) {
        String[] stringArray = new String[1];
        int counter=0;
        StringBuilder builder = new StringBuilder();
        builder.append("| Name | Creator | ID | Description |\n");
        builder.append("|:---:|:---:|:---:|:---:|\n");
        List<GDLevel> list = getLevelsList(type);
        sortLevelList(list, 5);
        for(GDLevel level : list)
        {
            builder.append(level.markdownWithDescrString() + "\n");
            counter++;
        }
        builder.insert(0, "#### Total: " + IntStream.of(counter).sum() + " levels\n\n");
        stringArray[0] = builder.toString();
        return stringArray;
    }

    static String[] generateTopDemonsList() {
        String[] stringArray = new String[1];
        int counter=0;
        StringBuilder builder = new StringBuilder();
        builder.append("| Name | Creator | ID | Downloads | Likes |\n");
        builder.append("|:---:|:---:|:---:|:---:|:---:|\n");
        List<GDLevel> list = getMostPopularDemons();
        //sortLevelList(list, 5);
        for(GDLevel level : list)
        {
            if(counter < 50)
            {
                builder.append(level.markdownString() + "\n");
                counter++;
            }
        }
        builder.insert(0, "#### Total: " + IntStream.of(counter).sum() + " levels\n\n");
        stringArray[0] = builder.toString();
        return stringArray;
    }

    static String[] generateMusicList(LevelType type) {
        String[] stringArray = new String[1];
        int counter=0;
        StringBuilder builder = new StringBuilder();
        builder.append("| Name | Author | ID | Count |\n");
        builder.append("|:---:|:---:|:---:|:---:|\n");
        List<GDLevel> list = getLevelsList(type);
        HashMap<GDSong, Integer> audio = new HashMap<>();
        GDSong songId;
        for(GDLevel level : list)
        {
            songId = level.getGdSong();
            if(audio.containsKey(songId))
                audio.put(songId,  audio.get(songId) + 1);
            else
                audio.put(songId,  1);
            counter++;
        }
        Map<GDSong, Integer> result = audio.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        builder.insert(0, "#### Total: " + counter + " levels\n\n");
        List<GDSong> mapKeys = new ArrayList<>(result.keySet());
        List<Integer> mapValues = new ArrayList<>(result.values());
        for(int i =0; i < mapKeys.size(); i++)
            builder.append(mapKeys.get(i).toListString() + mapValues.get(i) + "\n");
        stringArray[0] = builder.toString();
        return stringArray;
    }

    static String[] generateBuildersList(LevelType type) {
        String[] stringArray = new String[1];
        int counter=0;
        StringBuilder builder = new StringBuilder();
        builder.append("| Author | Count |\n");
        builder.append("|:---:|:---:|\n");
        List<GDLevel> list = getLevelsList(type);
        HashMap<String, Integer> audio = new HashMap<>();
        for(GDLevel level : list)
        {
            if(audio.containsKey(level.getCreator()))
                audio.put(level.getCreator(),  audio.get(level.getCreator()) + 1);
            else
                audio.put(level.getCreator(),  1);
            counter++;
        }
        Map<String, Integer> result = audio.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        List<String> mapKeys = new ArrayList<>(result.keySet());
        List<Integer> mapValues = new ArrayList<>(result.values());
        for(int i =0; i < mapKeys.size(); i++)
            builder.append(mapKeys.get(i)+ " | " + mapValues.get(i) + "\n");
        stringArray[0] = builder.toString();
        return stringArray;
    }

    static List<GDLevel> getLevelsList(LevelType type) {
        switch (type) {
            case Featured: {return getMostPopularFeatured(0);}
            case Epic: {return getMostPopularEpics(0);}
            default: {return null;}
        }
    }

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

    private static List<GDLevel> getMostPopularDemons() {
        List<GDLevel> list = new ArrayList<>();

        int i = 0;
        int count = 0;
        try {
            while(count < 50) {
                String res = GDServer.fetchMostPopularLevels(i);
                for (int j = 0; j < 10; j++) {
                    GDLevel level = getLevel(j, res);
                    if (level != null && level.getDifficulty() == Difficulty.DEMON){
                        list.add(level);
                        count++;
                        System.out.println("Demon #" + count + " added");
                    }
                    if(count > 50)
                        break;
                }
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

    private static int returnDiff(GDLevel gdLevel)
    {
        // -1!; Difficulty 0 = NA, which is unused in featured and epic
        int code = Difficulty.valueOf(gdLevel.getDifficulty().toString()).ordinal() - 1;
        if(code == 6)
            code += DemonDifficulty.valueOf(gdLevel.getDemonDifficulty().toString()).ordinal();
        return code;
    }

    private static void sortLevelList(List<GDLevel> list, int code) {
        switch (code)
        {
            case 1: { list.sort(descendingLikesComparator); break;}
            case 2: { list.sort(ascendingLikesComparator); break;}
            case 3: { list.sort(descendingDownloadsComparator); break;}
            case 4: { list.sort(ascendingDownloadsComparator); break;}
            case 5: {list.sort(descriptionLengthComparator); break;}
        }
    }
}
