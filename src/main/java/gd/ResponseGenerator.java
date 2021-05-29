package gd;

import gd.enums.DemonDifficulty;
import gd.enums.Difficulty;
import gd.enums.SortingCode;
import gd.model.GDLevel;
import gd.model.GDSong;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Class for generating level lists
 *
 * @author DoubleCookies
 */
public class ResponseGenerator {

    private static final int GD_PAGE_SIZE = 10;
    private static final int DIFFICULTIES_COUNT = 12;
    private static final int DEMONS_LIST_SIZE = 50;

    private static final String DIFFICULTIES_LIST_HEADER = "| Name | Creator | ID | Downloads | Likes |\n";
    private static final String LONGEST_DESCRIPTION_LIST_HEADER = "| Name | Creator | ID | Length | Description |\n";
    private static final String AUDIO_LIST_HEADER = "| ID | Author | Name | Count |\n";
    private static final String BIG_AUDIO_LIST_HEADER = "| ID | Author | Name | Count | Level IDs |\n";
    private static final String CREATORS_LIST_HEADER = "| Author | Count |\n";

    private static final String TWO_COLUMNS_MARKDOWN_DIVIDER = "|:---:|:---:|\n";
    private static final String FOUR_COLUMNS_MARKDOWN_DIVIDER = "|:---:|:---:|:---:|:---:|\n";
    private static final String FIVE_COLUMNS_MARKDOWN_DIVIDER = "|:---:|:---:|:---:|:---:|:---:|\n";

    private static final Logger logger = Logger.getLogger(ResponseGenerator.class);
    private static final Comparator<GDLevel> descendingLikesComparator = (o1, o2) -> (int) (o2.getLikes() - o1.getLikes());
    private static final Comparator<GDLevel> ascendingLikesComparator = (o1, o2) -> (int) (o1.getLikes() - o2.getLikes());
    private static final Comparator<GDLevel> descendingDownloadsComparator = (o1, o2) -> (int) (o2.getDownloads() - o1.getDownloads());
    private static final Comparator<GDLevel> ascendingDownloadsComparator = (o1, o2) -> (int) (o1.getDownloads() - o2.getDownloads());
    private static final Comparator<GDLevel> descriptionLengthComparator = (o1, o2) -> (int) (o2.getDescription().length() - o1.getDescription().length());

    private static List<GDLevel> levels;

    static String[] processLevels(SortingCode sortingCode) {
        processLevelsList(sortingCode);
        if (levels == null || levels.size() == 0)
            return null;
        return getLevelsInformation();
    }

    private static void processLevelsList(SortingCode sortingCode) {
        if (levels == null) {
            logger.info("Receiving featured levels list...");
            levels = getMostPopularFeatured(sortingCode);
        } else {
            logger.info("Filter epic levels...");
            levels.removeIf(item -> !item.isEpic());
        }
    }

    private static String[] getLevelsInformation() {
        logger.info("List received. Total " + levels.size() + " levels.");
        List<String> info = new ArrayList<>(generateListDiffs(levels));
        logger.info("Difficulties lists created.");
        info.add(generateListWithLongestDescription(levels));
        logger.info("Longest description list created.");
        ArrayList<String> musicInfo = generateMusicList(levels);
        info.add(musicInfo.get(0));
        logger.info("Music list created.");
        info.add(musicInfo.get(1));
        logger.info("Additional music list created.");
        info.add(generateBuildersList(levels));
        logger.info("Builders list created.");
        return info.toArray(new String[0]);
    }

    private static List<String> generateListDiffs(List<GDLevel> levels) {
        int length = DIFFICULTIES_COUNT;
        String[] stringArray = new String[length];
        StringBuilder[] builders = new StringBuilder[length];
        int[] counter = new int[length];
        for (int i = 0; i < length; i++) {
            builders[i] = new StringBuilder();
            builders[i].append(DIFFICULTIES_LIST_HEADER).append(FIVE_COLUMNS_MARKDOWN_DIVIDER);
        }
        for (GDLevel level : levels) {
            int i = returnLevelDifficultyNumber(level);
            builders[i].append(level.markdownString()).append("\n");
            counter[i]++;
            builders[length - 1].append(level.markdownString()).append("\n");
        }
        for (int i = 0; i < length - 1; i++) {
            builders[i].insert(0, "#### Total: " + counter[i] + " levels\n\n");
            stringArray[i] = builders[i].toString();
        }
        builders[length - 1].insert(0, "#### Total: " + IntStream.of(counter).sum() + " levels\n\n");
        stringArray[length - 1] = builders[length - 1].toString();

        return Arrays.asList(stringArray);
    }

    private static String generateListWithLongestDescription(List<GDLevel> levels) {
        int counter = 0;
        StringBuilder builder = new StringBuilder();
        builder.append(LONGEST_DESCRIPTION_LIST_HEADER).append(FIVE_COLUMNS_MARKDOWN_DIVIDER);
        sortLevelList(levels, SortingCode.LONGEST_DESCRIPTION);
        for (GDLevel level : levels) {
            builder.append(level.markdownWithDescriptionString()).append("\n");
            counter++;
        }
        builder.insert(0, "#### Total: " + IntStream.of(counter).sum() + " levels\n\n");
        return builder.toString();
    }

    static String generateTopDemonsList() {
        StringBuilder builder = new StringBuilder();
        List<GDLevel> list = getMostPopularDemons();
        if (list.size() == 0)
            return "";
        builder.append(DIFFICULTIES_LIST_HEADER).append(FIVE_COLUMNS_MARKDOWN_DIVIDER);
        for (GDLevel level : list) {
            builder.append(level.markdownString()).append("\n");
        }
        builder.insert(0, "#### Top-50 demons:\n\n");
        return builder.toString();
    }

    private static ArrayList<String> generateMusicList(List<GDLevel> levels) {
        int counter = 0;
        StringBuilder simpleBuilder = new StringBuilder();
        simpleBuilder.append(AUDIO_LIST_HEADER).append(FOUR_COLUMNS_MARKDOWN_DIVIDER);
        StringBuilder additionalBuilder = new StringBuilder();
        additionalBuilder.append(BIG_AUDIO_LIST_HEADER).append(FIVE_COLUMNS_MARKDOWN_DIVIDER);
        HashMap<GDSong, Integer> audio = new HashMap<>();
        HashMap<GDSong, ArrayList<Long>> audioLevelIds = new HashMap<>();
        GDSong songId;
        for (GDLevel level : levels) {
            songId = level.getGdSong();
            long levelId = level.getId();
            if (songId == null) {
                logger.warn("Null GDSong object for level " + level.getId());
            } else {
                ArrayList<Long> arrayListData = audioLevelIds.get(songId);
                if (audio.containsKey(songId)) {
                    audio.put(songId, audio.get(songId) + 1);
                } else {
                    audio.put(songId, 1);
                    arrayListData = new ArrayList<>();
                }
                arrayListData.add(levelId);
                audioLevelIds.put(songId, arrayListData);
                counter++;
            }
        }
        Map<GDSong, Integer> result = audio.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        simpleBuilder.insert(0, "#### Total: " + counter + " levels\n\n");
        List<GDSong> mapKeys = new ArrayList<>(result.keySet());
        List<Integer> mapValues = new ArrayList<>(result.values());
        for (int i = 0; i < mapKeys.size(); i++) {
            simpleBuilder.append(mapKeys.get(i).toString()).append(mapValues.get(i)).append("\n");
            List<Long> levelIds = audioLevelIds.get(mapKeys.get(i));
            String levelIdsString = levelIds.stream().map(String::valueOf)
                    .collect(Collectors.joining("; "));
            additionalBuilder.append(mapKeys.get(i).toString()).append(mapValues.get(i)).append(" | ").append(levelIdsString).append("\n");
        }

        ArrayList<String> data = new ArrayList<>();
        data.add(simpleBuilder.toString());
        data.add(additionalBuilder.toString());
        return data;
    }

    private static String generateBuildersList(List<GDLevel> levels) {
        StringBuilder builder = new StringBuilder();
        builder.append(CREATORS_LIST_HEADER).append(TWO_COLUMNS_MARKDOWN_DIVIDER);
        HashMap<String, Integer> audio = new HashMap<>();
        for (GDLevel level : levels) {
            if (audio.containsKey(level.getCreator()))
                audio.put(level.getCreator(), audio.get(level.getCreator()) + 1);
            else
                audio.put(level.getCreator(), 1);
        }
        Map<String, Integer> result = audio.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        List<String> mapKeys = new ArrayList<>(result.keySet());
        List<Integer> mapValues = new ArrayList<>(result.values());
        for (int i = 0; i < mapKeys.size(); i++)
            builder.append(mapKeys.get(i)).append(" | ").append(mapValues.get(i)).append("\n");
        return builder.toString();
    }

    private static List<GDLevel> getMostPopularDemons() {
        List<GDLevel> list = new ArrayList<>();
        if (!levels.isEmpty()) {
            list = levels.stream().filter(level -> level.getDifficulty() == Difficulty.DEMON)
                    .limit(DEMONS_LIST_SIZE).collect(Collectors.toList());
        }
        return list;
    }

    private static List<GDLevel> getMostPopularFeatured(SortingCode sortingCode) {
        List<GDLevel> list = new ArrayList<>();
        int levelsPage = 0;
        boolean receivingLevels = true;
        try {
            while (receivingLevels) {
                String res = GDServer.fetchRecentFeaturedLevels(levelsPage);
                if (res.equals("-1")) {
                    logger.warn("-1 was returned; list is finished");
                    receivingLevels = false;
                    continue;
                }
                addLevelsToList(list, res);
                levelsPage++;
            }
        } catch (Exception e) {
            logger.error("Exception during connecting: " + e);
            e.printStackTrace();
        }
        sortLevelList(list, sortingCode);
        return list;
    }

    private static void addLevelsToList(List<GDLevel> list, String res) {
        try {
            for (int j = 0; j < GD_PAGE_SIZE; j++) {
                GDLevel level = getLevel(j, res);
                if (level != null)
                    list.add(level);
            }
        } catch (Exception e) {
            logger.error("Exception while getting level: " + e);
        }
    }

    private static GDLevel getLevel(int j, String res) {
        return GDLevelFactory.buildGDLevelSearchedByFilter(res, j, false);
    }

    private static int returnLevelDifficultyNumber(GDLevel gdLevel) {
        int code = gdLevel.getDifficulty().ordinal();
        if (gdLevel.getDifficulty() == Difficulty.DEMON)
            code += gdLevel.getDemonDifficulty().ordinal();
        return code;
    }

    private static void sortLevelList(List<GDLevel> list, SortingCode sortingCode) {
        switch (sortingCode) {
            case DESCENDING_LIKES: {
                list.sort(descendingLikesComparator);
                break;
            }
            case ASCENDING_LIKES: {
                list.sort(ascendingLikesComparator);
                break;
            }
            case DESCENDING_DOWNLOADS: {
                list.sort(descendingDownloadsComparator);
                break;
            }
            case ASCENDING_DOWNLOADS: {
                list.sort(ascendingDownloadsComparator);
                break;
            }
            case LONGEST_DESCRIPTION: {
                list.sort(descriptionLengthComparator);
                break;
            }
        }
    }
}
