package gd;

import jdash.client.GDClient;
import jdash.client.exception.GDClientException;
import jdash.common.LevelBrowseMode;
import jdash.common.entity.GDLevel;
import jdash.common.entity.GDSong;
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
    private static final Comparator<GDLevel> defaultDescendingIdComparator = (o1, o2) -> (int) (o2.id() - o1.id());
    private static final Comparator<GDLevel> descendingLikesComparator = (o1, o2) -> (int) (o2.likes() - o1.likes());
    private static final Comparator<GDLevel> ascendingLikesComparator = (o1, o2) -> (int) (o1.likes() - o2.likes());
    private static final Comparator<GDLevel> descendingDownloadsComparator = (o1, o2) -> (int) (o2.downloads() - o1.downloads());
    private static final Comparator<GDLevel> ascendingDownloadsComparator = (o1, o2) -> (int) (o1.downloads() - o2.downloads());
    private static final Comparator<GDLevel> descriptionLengthComparator = (o1, o2) -> (int) (o2.description().length() - o1.description().length());

    private static List<GDLevel> levels;
    private static List<GDLevel> epicLevels;
    private static final HashMap<GDSong, ArrayList<Long>> audioLevelIds = new HashMap<>();
    private static final GDClient client = GDClient.create();

    static String[] processFeaturedLevels(SortingCode sortingCode) {
        if (isLevelsListEmpty(sortingCode))
            return null;
        return getFeaturedLevelsInformation();
    }

    static String[] processEpicLevels(SortingCode sortingCode) {
        if (isLevelsListEmpty(sortingCode))
            return null;
        return getEpicLevelsInformation();
    }

    private static boolean isLevelsListEmpty(SortingCode sortingCode) {
        processLevelsList(sortingCode);
        return levels == null || levels.size() == 0;
    }

    private static void processLevelsList(SortingCode sortingCode) {
        if (levels == null) {
            logger.info("Receiving featured levels list...");
            levels = getFeaturedLevels(sortingCode);
        } else {
            logger.info("Filter epic levels...");
            epicLevels = levels.stream().filter(GDLevel::isEpic).collect(Collectors.toList());
        }
    }

    private static List<GDLevel> getFeaturedLevels(SortingCode sortingCode) {
        List<GDLevel> list = new ArrayList<>();
        int currentPage = 0;
        try {
            while (true) {
                List<GDLevel> levels = client.browseLevels(LevelBrowseMode.FEATURED,null, null, currentPage)
                        .collectList().block();
                if (levels != null)
                    list.addAll(levels);
                else
                    break;
                if (currentPage % 100 == 0)
                    logger.info("Processing page " + currentPage);
                currentPage++;
            }
        } catch (GDClientException e) {
            logger.error("Exception during getting data: " + e + "\r\n" + e.getCause().getMessage());
        }
        sortLevelList(list, sortingCode);
        return list;
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
            default: {
                list.sort(defaultDescendingIdComparator);
                break;
            }
        }
    }

    private static String[] getFeaturedLevelsInformation() {
        return getLevelsInformation(levels);
    }

    private static String[] getEpicLevelsInformation() {
        return getLevelsInformation(epicLevels);
    }

    private static String[] getLevelsInformation(List<GDLevel> levels) {
        logger.info("List received. Total " + levels.size() + " levels.");
        List<String> info = new ArrayList<>(generateListForDifficulties(levels));
        logger.info("Difficulties lists created.");
        info.add(generateListWithLongestDescription(levels));
        logger.info("Longest description list created.");
        ArrayList<String> musicInfo = generateMusicList();
        info.add(musicInfo.get(0));
        logger.info("Music list created.");
        info.add(musicInfo.get(1));
        logger.info("Additional music list created.");
        info.add(generateBuildersList());
        logger.info("Builders list created.");
        return info.toArray(new String[0]);
    }

    private static List<String> generateListForDifficulties(List<GDLevel> levels) {
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
            if (i < 0) {
                logger.warn("NA level in featured: " + level.id());
            }
            builders[i].append(levelMarkdownString(level)).append("\n");
            counter[i]++;
            builders[length - 1].append(levelMarkdownString(level)).append("\n");
        }
        for (int i = 0; i < length - 1; i++) {
            builders[i].insert(0, "#### Total: " + counter[i] + " levels\n\n");
            stringArray[i] = builders[i].toString();
        }
        builders[length - 1].insert(0, "#### Total: " + IntStream.of(counter).sum() + " levels\n\n");
        stringArray[length - 1] = builders[length - 1].toString();

        return Arrays.asList(stringArray);
    }

    private static int returnLevelDifficultyNumber(GDLevel gdLevel) {
        if (gdLevel.isAuto())
            return 0;
        int code = gdLevel.difficulty().ordinal() - 1;
        if (gdLevel.isDemon())
            code = 6 + gdLevel.demonDifficulty().ordinal();
        return code;
    }

    private static String generateListWithLongestDescription(List<GDLevel> levels) {
        int counter = 0;
        StringBuilder builder = new StringBuilder();
        builder.append(LONGEST_DESCRIPTION_LIST_HEADER).append(FIVE_COLUMNS_MARKDOWN_DIVIDER);
        sortLevelList(levels, SortingCode.LONGEST_DESCRIPTION);
        for (GDLevel level : levels) {
            builder.append(levelMarkdownWithDescriptionString(level)).append("\n");
            counter++;
        }
        builder.insert(0, "#### Total: " + IntStream.of(counter).sum() + " levels\n\n");
        return builder.toString();
    }

    private static ArrayList<String> generateMusicList() {
        StringBuilder simpleBuilder = new StringBuilder();
        simpleBuilder.append(AUDIO_LIST_HEADER).append(FOUR_COLUMNS_MARKDOWN_DIVIDER);
        StringBuilder additionalBuilder = new StringBuilder();
        additionalBuilder.append(BIG_AUDIO_LIST_HEADER).append(FIVE_COLUMNS_MARKDOWN_DIVIDER);

        Map<GDSong, ArrayList<Long>> result = getMapForSongsInLevels();

        List<GDSong> mapKeys = new ArrayList<>(result.keySet());
        List<ArrayList<Long>> mapValues = new ArrayList<>(result.values());
        for (int i = 0; i < mapKeys.size(); i++) {
            simpleBuilder.append(songMarkdownString(mapKeys.get(i))).append(" | ").append(mapValues.get(i).size()).append("\n");
            List<Long> levelIds = audioLevelIds.get(mapKeys.get(i));
            String levelIdsString = levelIds.stream().map(String::valueOf)
                    .collect(Collectors.joining("; "));
            additionalBuilder.append(songMarkdownString(mapKeys.get(i))).append(" | ").append(mapValues.get(i).size()).append(" | ").append(levelIdsString).append("\n");
        }

        ArrayList<String> data = new ArrayList<>();
        data.add(simpleBuilder.toString());
        data.add(additionalBuilder.toString());
        return data;
    }

    private static Map<GDSong, ArrayList<Long>> getMapForSongsInLevels() {
        Optional<GDSong> song;
        for (GDLevel level : levels) {
            song = level.song();
            long levelId = level.id();
            if (!song.isPresent()) {
                logger.warn("Null GDSong object for level " + level.id());
            } else {
                ArrayList<Long> arrayListData = audioLevelIds.get(song.get());
                if (arrayListData == null || arrayListData.isEmpty())
                    arrayListData = new ArrayList<>();
                arrayListData.add(levelId);
                audioLevelIds.put(song.get(), arrayListData);
            }
        }
        return audioLevelIds.entrySet().stream()
                .sorted(Map.Entry.comparingByValue((o1, o2) -> (int) (o2.size() - o1.size())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    private static String generateBuildersList() {
        StringBuilder builder = new StringBuilder();
        builder.append(CREATORS_LIST_HEADER).append(TWO_COLUMNS_MARKDOWN_DIVIDER);
        HashMap<String, Integer> buildersMap = getMapForBuilders();

        List<String> mapKeys = new ArrayList<>(buildersMap.keySet());
        List<Integer> mapValues = new ArrayList<>(buildersMap.values());
        for (int i = 0; i < mapKeys.size(); i++)
            builder.append(mapKeys.get(i)).append(" | ").append(mapValues.get(i)).append("\n");
        return builder.toString();
    }

    private static HashMap<String, Integer> getMapForBuilders() {
        HashMap<String, Integer> buildersMap = new HashMap<>();
        for (GDLevel level : levels) {
            if (level.creatorName().isPresent()) {
                if (buildersMap.containsKey(level.creatorName().get()))
                    buildersMap.put(level.creatorName().get(), buildersMap.get(level.creatorName().get()) + 1);
                else
                    buildersMap.put(level.creatorName().get(), 1);
            }
        }
        return buildersMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    private static String levelMarkdownString(GDLevel level) {
        String creator = level.creatorName().isPresent() ? level.creatorName().get() : "-";
        return "| " + level.name() + " | " + creator + " | " + level.id() + " | " + level.downloads() + " | " + level.likes();
    }

    private static String levelMarkdownWithDescriptionString(GDLevel level) {
        String replacedDescription = level.description().replace("|", "&#124;");
        String creator = level.creatorName().isPresent() ? level.creatorName().get() : "-";
        return "| " + level.name() + " | " + creator + " | " + level.id() + " | " + level.description().length() + " | " + replacedDescription;
    }

    private static String songMarkdownString(GDSong song) {
        return song.id() + " | " + song.artist() + " | " + song.title();
    }

    static String generateTopDemonsList() {
        StringBuilder builder = new StringBuilder();
        List<GDLevel> list = getMostPopularDemons();
        if (list.size() == 0)
            return "";
        builder.append(DIFFICULTIES_LIST_HEADER).append(FIVE_COLUMNS_MARKDOWN_DIVIDER);
        for (GDLevel level : list) {
            builder.append(levelMarkdownString(level)).append("\n");
        }
        builder.insert(0, "#### Top-50 demons:\n\n");
        return builder.toString();
    }

    private static List<GDLevel> getMostPopularDemons() {
        List<GDLevel> list = new ArrayList<>();
        if (!levels.isEmpty()) {
            levels.sort(descendingDownloadsComparator);
            list = levels.stream().filter(GDLevel::isDemon)
                    .limit(DEMONS_LIST_SIZE).collect(Collectors.toList());
        }
        return list;
    }
}
