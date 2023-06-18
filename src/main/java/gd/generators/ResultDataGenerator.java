package gd.generators;

import gd.SortingCode;
import gd.service.levelsProcessing.LevelsProcessingService;
import gd.service.SaveResultsService;
import jdash.common.entity.GDLevel;
import jdash.common.entity.GDSong;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Class for forming data tables based on levels
 */
public class ResultDataGenerator {
    private static final int DIFFICULTIES_COUNT = 12;
    private static final String DIFFICULTIES_LIST_HEADER = "| Name | Creator | ID | Downloads | Likes |\n";
    private static final String LONGEST_DESCRIPTION_LIST_HEADER = "| Name | Creator | ID | Length | Description |\n";
    private static final String AUDIO_LIST_HEADER = "| ID | Author | Name | Count |\n";
    private static final String BIG_AUDIO_LIST_HEADER = "| ID | Author | Name | Count | Level IDs |\n";
    private static final String CREATORS_LIST_HEADER = "| Author | Count |\n";
    private static final String TWO_COLUMNS_MARKDOWN_DIVIDER = "|:---:|:---:|\n";
    private static final String FOUR_COLUMNS_MARKDOWN_DIVIDER = "|:---:|:---:|:---:|:---:|\n";
    private static final String FIVE_COLUMNS_MARKDOWN_DIVIDER = "|:---:|:---:|:---:|:---:|:---:|\n";
    private static final HashMap<GDSong, ArrayList<Long>> audioLevelIds = new HashMap<>();

    private static final Logger logger = LogManager.getLogger(ResultDataGenerator.class);

    public static void processAllLevels(SortingCode sortingCode) {
        processLevelsForType(sortingCode, "featured");
        processLevelsForType(sortingCode, "epic");
        processTopDemonsList();
    }

    private static void processLevelsForType(SortingCode sortingCode, String levelsType) {
        String[] res;
        if (levelsType.equals("featured"))
            res = getFeaturedLevelsData();
        else
            res = getEpicLevelsData();
        if (res == null) {
            logger.warn(levelsType + " levels list is empty! No changes were made.");
            return;
        }
        processLevelsData(sortingCode, levelsType, res);
    }

    private static void processTopDemonsList() {
        String res = getTopDemonLevelsData();
        if (res.isEmpty())
            return;
        SaveResultsService.writeToFileDemonsList(res.getBytes());
        logger.info("Top-50 demon list finished");
    }

    private static void processLevelsData(SortingCode sortingCode, String levelsType, String[] res) {
        String capitalizedLevelsType = StringUtils.capitalize(levelsType);
        for (int j = 0; j < 11; j++) {
            String prefix = SaveResultsService.difficultyFolderMap.get(j + 1) + " " + levelsType;
            SaveResultsService.writeToFileRegularLists(sortingCode, prefix, j + 1, res[j].getBytes());
        }
        SaveResultsService.writeToFileRegularLists(sortingCode, capitalizedLevelsType, 0, res[11].getBytes());
        SaveResultsService.writeToFileRegularLists(SortingCode.LONGEST_DESCRIPTION, capitalizedLevelsType, 0, res[12].getBytes());
        SaveResultsService.writeToFileRegularLists(SortingCode.DEFAULT, capitalizedLevelsType + " audio info", 0, res[13].getBytes());
        SaveResultsService.writeToFileRegularLists(SortingCode.DEFAULT, capitalizedLevelsType + " audio info expanded", 0, res[14].getBytes());
        SaveResultsService.writeToFileRegularLists(SortingCode.DEFAULT, capitalizedLevelsType + " builders info", 0, res[15].getBytes());
        logger.info("All " + levelsType + " lists are finished");
    }

    public static String[] getFeaturedLevelsData() {
        return processLevels(LevelsProcessingService.getLevels());
    }

    public static String[] getEpicLevelsData() {
        return processLevels(LevelsProcessingService.getEpicLevels());
    }

    public static String getTopDemonLevelsData() {
        return ResultDataGenerator.processTopDemons(LevelsProcessingService.getPopularDemonsList());
    }

    private static String[] processLevels(List<GDLevel> levels) {
        if (isLevelsListEmpty(levels))
            return null;
        return ResultDataGenerator.getLevelsInformation(levels);
    }

    private static boolean isLevelsListEmpty(List<GDLevel> levels) {
        return levels == null || levels.size() == 0;
    }

    public static String[] getLevelsInformation(List<GDLevel> levels) {
        audioLevelIds.clear();
        logger.info("List received. Total " + levels.size() + " levels.");
        List<String> info = new ArrayList<>(generateListForDifficulties(levels));
        info.add(generateListWithLongestDescription(levels));
        ArrayList<String> musicInfo = generateMusicList(levels);
        info.add(getBasicMusicInfo(musicInfo));
        info.add(getExtendedMusicInfo(musicInfo));
        info.add(generateBuildersList(levels));
        return info.toArray(new String[0]);
    }

    private static String getBasicMusicInfo(ArrayList<String> musicInfo) {
        return musicInfo.size() > 0 ? musicInfo.get(0) : "";
    }

    private static String getExtendedMusicInfo(ArrayList<String> musicInfo) {
        return musicInfo.size() > 1 ? musicInfo.get(1) : "";
    }

    private static List<String> generateListForDifficulties(List<GDLevel> levels) {
        int length = DIFFICULTIES_COUNT;

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

        String[] stringArray = new String[length];
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
        if (gdLevel.isDemon())
            return (6 + gdLevel.demonDifficulty().ordinal());
        else
            return gdLevel.difficulty().ordinal() - 1;
    }

    private static String generateListWithLongestDescription(List<GDLevel> levels) {
        int counter = 0;
        StringBuilder builder = new StringBuilder();
        builder.append(LONGEST_DESCRIPTION_LIST_HEADER).append(FIVE_COLUMNS_MARKDOWN_DIVIDER);
        LevelsProcessingService.sortLevelList(levels, SortingCode.LONGEST_DESCRIPTION);
        for (GDLevel level : levels) {
            builder.append(levelMarkdownWithDescriptionString(level)).append("\n");
            counter++;
        }
        builder.insert(0, "#### Total: " + IntStream.of(counter).sum() + " levels\n\n");
        return builder.toString();
    }

    private static ArrayList<String> generateMusicList(List<GDLevel> levels) {
        StringBuilder simpleBuilder = new StringBuilder();
        simpleBuilder.append(AUDIO_LIST_HEADER).append(FOUR_COLUMNS_MARKDOWN_DIVIDER);
        StringBuilder additionalBuilder = new StringBuilder();
        additionalBuilder.append(BIG_AUDIO_LIST_HEADER).append(FIVE_COLUMNS_MARKDOWN_DIVIDER);

        Map<GDSong, ArrayList<Long>> result = getMapForSongsInLevels(levels);

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

    private static Map<GDSong, ArrayList<Long>> getMapForSongsInLevels(List<GDLevel> levels) {
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

    private static String generateBuildersList(List<GDLevel> levels) {
        StringBuilder builder = new StringBuilder();
        builder.append(CREATORS_LIST_HEADER).append(TWO_COLUMNS_MARKDOWN_DIVIDER);
        HashMap<String, Integer> buildersMap = getMapForBuilders(levels);
        List<String> mapKeys = new ArrayList<>(buildersMap.keySet());
        List<Integer> mapValues = new ArrayList<>(buildersMap.values());
        for (int i = 0; i < mapKeys.size(); i++)
            builder.append(mapKeys.get(i)).append(" | ").append(mapValues.get(i)).append("\n");
        return builder.toString();
    }

    private static HashMap<String, Integer> getMapForBuilders(List<GDLevel> levels) {
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


    public static String processTopDemons(List<GDLevel> list) {
        StringBuilder builder = new StringBuilder();
        if (list.size() == 0)
            return "";
        builder.append(DIFFICULTIES_LIST_HEADER).append(FIVE_COLUMNS_MARKDOWN_DIVIDER);
        for (GDLevel level : list) {
            builder.append(levelMarkdownString(level)).append("\n");
        }
        builder.insert(0, "#### Top-50 demons:\n\n");
        return builder.toString();
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
}
