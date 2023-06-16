package gd.generators;

import gd.ListType;
import gd.SortingCode;
import gd.service.SaveResultsService;
import gd.service.WikiLevelsProcessingService;
import jdash.common.Difficulty;
import jdash.common.entity.GDLevel;

import java.text.NumberFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static gd.Constants.*;

public class WikiResultDataGenerator {

    private static final String TOP_LEVELS_PAGE_START = "{{Фан-статья}}\n" +
            "{{Связанный шаблон|[[Шаблон:Топ 50 популярных уровней|данном шаблоне]]}}\n" +
            "\n" +
            "В представленных ниже списках находятся 50 самых популярных уровней по количеству загрузок и по количеству лайков.\n" +
            "\n" +
            "<tabber>Топ по загрузкам=\n" +
            "<div class=\"recordboxmedium\">\n";

    private static final String TOP_LEVELS_PAGE_FINISH = "\n</div>\n" +
            "</tabber>\n" +
            "\n" +
            "== Интересные факты ==\n" +
            "* В обоих топах нет ни одного уровня со сложностью {{Средний демон}} и {{Безумный демон}}.\n" +
            "* [[Bloodbath]] — единственный {{Экстремальный демон}} в данных топах.\n" +
            "* [[Phantom]], [[Dinosaur]] и [[Shock]] — единственные [[Зал славы|эпические]] уровни в данных топах, причём [[Phantom]] и [[Dinosaur]] есть в обоих топах, а [[Shock]] есть только в топе по лайкам.\n" +
            "* [[Fernanfloo 3]] — единственный неоценённый уровень в данных топах." +
            "[[Категория:Топы]]\n" +
            "[[Категория:Пользовательские уровни]]\n" +
            "[[Категория:Уровни]]";

    private static final String TOP_DEMONS_PAGE_START = "{{Фан-статья}}\n" +
            "{{Связанный шаблон|[[Шаблон:Топ 50 популярных демонов|данном шаблоне]]}}\n" +
            "\n" +
            "В представленных ниже списках находятся 50 самых популярных демонов по количеству загрузок и по количеству лайков.\n\n" +
            "<tabber>Топ по загрузкам=\n" +
            "<div class=\"recordboxmedium\">\n";


    private static final String TOP_DEMONS_PAGE_FINISH = "\n</div>\n" +
            "</tabber>\n" +
            "\n" +
            "== Интересные факты ==\n" +
            "* [[Extinction]], [[FREEDOM]] и [[Bloodlust]] — единственные [[Зал славы|эпические]] уровни в топе.\n" +
            "* [[Theory of Skrillex]], [[Demon mixed]], [[Ultra Paracosm]], [[Sonic Wave]], [[invisible clubstep]] и [[Blue Hell]] — единственные демоны из топа, не имеющие [[Featured-уровни|Featured]].\n" +
            "* В данном топе присутствуют демоны всех сложностей.\n" +
            "[[Категория:Демоны]]\n" +
            "[[Категория:Пользовательские уровни]]\n" +
            "[[Категория:Уровни]]\n" +
            "[[Категория:Топы]]";

    private static final String TOP_MEDIUM_SECTION =  "\n</div>\n" +
            "|-|Топ по лайкам=\n" +
            "<div class=\"recordboxmedium\">\n";

    static String mostDownloadedLevels;
    static String mostLikedLevels;
    static String mostDownloadedLevelsForDemons;
    static String mostLikedLevelsForDemons;

    public static void processListsResults() {
        generateTablesForLists();
        generateCopyTextForWiki();
    }

    private static void generateCopyTextForWiki() {
        generateMostDownloadedAndLikedCopyText();
        generateMostDownloadedDemonsCopyText();
    }

    private static void generateTablesForLists() {
        generateMostDownloadedLevels();
        generateMostDownloadedLevelsForDemons();
        generateMostLikedLevels();
        generateMostLikedLevelsForDemons();
        generateMostDownloadedLevelsSmall();
        generateMostDownloadedLevelsSmallForDemons();
    }

    private static void generateMostDownloadedLevels() {
        mostDownloadedLevels = createTableForLevels(ListType.DOWNLOAD_LEVELS);
        SaveResultsService.writeToFileWikiLists("Most downloaded", mostDownloadedLevels.getBytes());
    }

    private static void generateMostDownloadedLevelsForDemons() {
        mostDownloadedLevelsForDemons = createTableForLevels(ListType.DOWNLOAD_DEMONS);
        SaveResultsService.writeToFileWikiLists("Most downloaded for demons", mostDownloadedLevelsForDemons.getBytes());
    }

    private static void generateMostLikedLevels() {
        mostLikedLevels = createTableForLevels(ListType.LIKED_LEVELS);
        SaveResultsService.writeToFileWikiLists("Most liked", mostLikedLevels.getBytes());
    }

    private static void generateMostLikedLevelsForDemons() {
        mostLikedLevelsForDemons = createTableForLevels(ListType.LIKED_DEMONS);
        SaveResultsService.writeToFileWikiLists("Most liked for demons", mostLikedLevelsForDemons.getBytes());
    }

    private static void generateMostDownloadedLevelsSmall() {
        String res = createShortListForDownloadedLevels(ListType.DOWNLOAD_LEVELS);
        SaveResultsService.writeToFileWikiLists("Most downloaded small", res.getBytes());
    }

    private static void generateMostDownloadedLevelsSmallForDemons() {
        String res = createShortListForDownloadedLevels(ListType.DOWNLOAD_DEMONS);
        SaveResultsService.writeToFileWikiLists("Most downloaded small for demons", res.getBytes());
    }

    private static void generateMostDownloadedAndLikedCopyText() {
        String result = TOP_LEVELS_PAGE_START + mostDownloadedLevels
                + TOP_MEDIUM_SECTION
                + mostLikedLevels + TOP_LEVELS_PAGE_FINISH;
        SaveResultsService.writeToFileWikiLists("Copy text", result.getBytes());
    }

    private static void generateMostDownloadedDemonsCopyText() {
        String result = TOP_DEMONS_PAGE_START + mostDownloadedLevelsForDemons
                + TOP_MEDIUM_SECTION
                + mostLikedLevelsForDemons + TOP_DEMONS_PAGE_FINISH;
        SaveResultsService.writeToFileWikiLists("Copy text for demons", result.getBytes());
    }

    private static final NumberFormat numberFormatter = NumberFormat.getNumberInstance();

    static String createShortListForDownloadedLevels(ListType type) {
        int counter = 0;
        StringBuilder builder = new StringBuilder();
        builder.append(ARRAY_START);
        List<GDLevel> levelsForList = type == ListType.DOWNLOAD_LEVELS
                ? WikiLevelsProcessingService.getMostDownloadedLevels() : WikiLevelsProcessingService.getMostDownloadedDemons();
        for (GDLevel level : levelsForList) {
            counter++;
            builder.append(smallWikiString(level)).append(counter == LIST_SIZE ? "\n" : ",\n");
        }
        builder.append(ARRAY_END);
        return builder.toString();
    }

    static String createTableForLevels(ListType listType) {
        int counter = 0;
        StringBuilder builder = new StringBuilder();
        builder.append(WIKITABLE_START);
        List<GDLevel> levelsForList = selectLevelsList(listType);
        for (GDLevel level : levelsForList) {
            builder.append(WIKITABLE_NEWLINE).append(wikiString(level, counter)).append("\n");
            counter++;
        }
        builder.append(WIKITABLE_END);
        return builder.toString();
    }

    private static List<GDLevel> selectLevelsList(ListType listType) {
        switch (listType) {
            case DOWNLOAD_DEMONS: {
                return WikiLevelsProcessingService.getMostDownloadedDemons();
            }
            case LIKED_LEVELS: {
                return WikiLevelsProcessingService.getMostLikedLevels();
            }
            case LIKED_DEMONS: {
                return WikiLevelsProcessingService.getMostLikedDemons();
            }
            default: {
                return WikiLevelsProcessingService.getMostDownloadedLevels();
            }
        }
    }

    private static String smallWikiString(GDLevel level) {
        String data = LEVELS_WITH_DIFFERENT_NAME.containsKey(level.name())
                ? LEVELS_WITH_DIFFERENT_NAME.get(level.name()).trim() : level.name().trim();
        data = data.substring(0, 1).toUpperCase() + data.substring(1);
        return data;
    }

    /**
     * Generate string for pages
     *
     * @param count number of row in table
     * @return string with information about level in MediaWiki table row format
     */
    private static String wikiString(GDLevel level, int count) {
        String levelName = LEVELS_WITH_DIFFERENT_NAME.containsKey(level.name())
                ? LEVELS_WITH_DIFFERENT_NAME.get(level.name()).trim() : level.name().trim();
        String prefix = getPrefix(level);
        String difficultyOutput = getDifficultyOutput(level);
        String creatorName = getCreatorName(level, levelName);
        return "! " + (count + 1) + "\n" + "| [[" + levelName + "]]\n| "
                + creatorName + "\n| <center>{{" + prefix + difficultyOutput
                + "}}</center>\n| " + numberFormatter.format(level.downloads())
                + "\n| " + numberFormatter.format(level.likes());
    }

    private static String getPrefix(GDLevel level) {
        if (level.isEpic())
            return "Эпический ";
        if (level.featuredScore() > 0)
            return "Featured ";
        return "";
    }

    private static String getDifficultyOutput(GDLevel level) {
        if (level.isAuto()) {
            return difficultyStringMap.get(Difficulty.AUTO);
        } else {
            return level.isDemon()
                    ? demonDifficultyStringMap.get(level.demonDifficulty())
                    : difficultyStringMap.get(level.difficulty());
        }
    }

    private static String getCreatorName(GDLevel level, String levelName) {
        String creatorOutput;
        Optional<String> creator = level.creatorName();
        if (creator.isPresent()) {
            String creatorName = creator.get();
            if (allowedCreatorsNames.contains(creatorName)) {
                creatorOutput = "[[" + creatorName + "]]";
            } else if (allowedCreatorsNamesWithReplacement.containsKey(creatorName)) {
                creatorOutput = "[[" + allowedCreatorsNamesWithReplacement.get(creatorName) + "]]";
            } else if (specialCreatorsNamesForLevels.containsKey(levelName)) {
                creatorOutput = "[[" + specialCreatorsNamesForLevels.get(levelName) + "]]";
            } else {
                creatorOutput = creatorName;
            }
        } else {
            creatorOutput = "—";
        }


        return creatorOutput;
    }
}
