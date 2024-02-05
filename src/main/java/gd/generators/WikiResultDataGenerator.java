package gd.generators;

import gd.service.SaveToFileResultsService;
import gd.service.levelsProcessing.WikiLevelsProcessingService;
import jdash.common.DemonDifficulty;
import jdash.common.Difficulty;
import jdash.common.entity.GDLevel;

import java.text.NumberFormat;
import java.util.*;

public class WikiResultDataGenerator {

    private enum ListType {
        DOWNLOAD_LEVELS,
        DOWNLOAD_DEMONS,
        LIKED_LEVELS,
        LIKED_DEMONS
    }

    private static final int WIKITABLE_LIST_SIZE = 50;

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

    private static final String TOP_MEDIUM_SECTION = "\n</div>\n" +
            "|-|Топ по лайкам=\n" +
            "<div class=\"recordboxmedium\">\n";

    private static final String WIKITABLE_START = "{| class=\"wikitable\"\n" +
            "! Место\n" +
            "! Уровень\n" +
            "! Создатель\n" +
            "! [[Уровни сложности|Сложность]]\n" +
            "! Кол-во загрузок\n" +
            "! Кол-во лайков\n";
    private static final String WIKITABLE_END = "|}";
    private static final String WIKITABLE_NEWLINE = "|-\n";
    private static final String ARRAY_START = "{{#arraydefine:levels|TEST_VALUE_FOR_SHIFT,\n";
    private static final String ARRAY_END = "}}{{#vardefineecho:number|" +
            "{{#expr:{{#arraysearch:levels|{{{1}}}}}}}}}" +
            "<noinclude>{{doc}}[[Категория:Информационные шаблоны]]</noinclude>";

    /*Difficulties map for replace in text */
    private static final Map<Difficulty, String> difficultyStringMap = DataInitializer.initMapForTemplates();

    /* Demon difficulties map for replace in text */
    private static final Map<DemonDifficulty, String> demonDifficultyStringMap = DataInitializer.initMapForDemonTemplates();

    /* Levels map which have article on wiki but with different article title */
    private static final Map<String, String> LEVELS_WITH_DIFFERENT_NAME = DataInitializer.initLevelsMapWithDifferentName();

    /* List for creators nicknames which have article on wiki */
    private static final List<String> allowedCreatorsNames = DataInitializer.initCreatorsNamesList();

    /* Creators map which have article on wiki but with different article title */
    private static final Map<String, String> allowedCreatorsNamesWithReplacement = DataInitializer.initCreatorsMapForReplacement();

    /* Map for levels where creator should be replaced manually */
    private static final Map<String, String> specialCreatorsNamesForLevels = DataInitializer.initSpecialCreatorsNamesForLevels();

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
        SaveToFileResultsService.writeToFileWikiLists("Most downloaded", mostDownloadedLevels.getBytes());
    }

    private static void generateMostDownloadedLevelsForDemons() {
        mostDownloadedLevelsForDemons = createTableForLevels(ListType.DOWNLOAD_DEMONS);
        SaveToFileResultsService.writeToFileWikiLists("Most downloaded for demons", mostDownloadedLevelsForDemons.getBytes());
    }

    private static void generateMostLikedLevels() {
        mostLikedLevels = createTableForLevels(ListType.LIKED_LEVELS);
        SaveToFileResultsService.writeToFileWikiLists("Most liked", mostLikedLevels.getBytes());
    }

    private static void generateMostLikedLevelsForDemons() {
        mostLikedLevelsForDemons = createTableForLevels(ListType.LIKED_DEMONS);
        SaveToFileResultsService.writeToFileWikiLists("Most liked for demons", mostLikedLevelsForDemons.getBytes());
    }

    private static void generateMostDownloadedLevelsSmall() {
        String res = createShortListForDownloadedLevels(ListType.DOWNLOAD_LEVELS);
        SaveToFileResultsService.writeToFileWikiLists("Most downloaded small", res.getBytes());
    }

    private static void generateMostDownloadedLevelsSmallForDemons() {
        String res = createShortListForDownloadedLevels(ListType.DOWNLOAD_DEMONS);
        SaveToFileResultsService.writeToFileWikiLists("Most downloaded small for demons", res.getBytes());
    }

    private static void generateMostDownloadedAndLikedCopyText() {
        String result = TOP_LEVELS_PAGE_START + mostDownloadedLevels
                + TOP_MEDIUM_SECTION
                + mostLikedLevels + TOP_LEVELS_PAGE_FINISH;
        SaveToFileResultsService.writeToFileWikiLists("Copy text", result.getBytes());
    }

    private static void generateMostDownloadedDemonsCopyText() {
        String result = TOP_DEMONS_PAGE_START + mostDownloadedLevelsForDemons
                + TOP_MEDIUM_SECTION
                + mostLikedLevelsForDemons + TOP_DEMONS_PAGE_FINISH;
        SaveToFileResultsService.writeToFileWikiLists("Copy text for demons", result.getBytes());
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
            builder.append(smallWikiString(level)).append(counter == WIKITABLE_LIST_SIZE ? "\n" : ",\n");
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

    private static class DataInitializer {

        private static Map<Difficulty, String> initMapForTemplates() {
            Map<Difficulty, String> map = new HashMap<>();
            map.put(Difficulty.AUTO, "авто");
            map.put(Difficulty.EASY, "лёгкий");
            map.put(Difficulty.NORMAL, "нормальный");
            map.put(Difficulty.HARD, "сложный");
            map.put(Difficulty.HARDER, "очень сложный");
            map.put(Difficulty.INSANE, "безумный");
            return map;
        }

        private static Map<DemonDifficulty, String> initMapForDemonTemplates() {
            Map<DemonDifficulty, String> map = new HashMap<>();
            map.put(DemonDifficulty.EASY, "лёгкий демон");
            map.put(DemonDifficulty.MEDIUM, "средний демон");
            map.put(DemonDifficulty.HARD, "демон");
            map.put(DemonDifficulty.INSANE, "безумный демон");
            map.put(DemonDifficulty.EXTREME, "экстремальный демон");
            return map;
        }

        private static Map<String, String> initLevelsMapWithDifferentName() {
            Map<String, String> map = new HashMap<>();
            map.put("Dreamland", "Dreamland (Scanbrux)");
            map.put("auto play area", "Auto play area");
            map.put("flappy hexagon", "Flappy Hexagon");
            map.put("THE LIGHTNING ROAD", "The Lightning Road");
            map.put("Clutterfunk v2", "Clutterfunk v2 (Neptune)");
            map.put("lucid dream", "Lucid Dream");
            map.put("auto cycles", "Auto Cycles");
            map.put("DARKNESS", "Darkness");
            map.put("simple cloud", "Simple Cloud");
            map.put("ThE WorLd", "The World");
            map.put("Promises", "Promises (Adiale)");
            map.put("endless", "Endless (FlappySheepy)");
            map.put("moon adventure", "Moon Adventure");
            map.put("time pressure", "Time pressure");
            map.put("Theory of SkriLLex", "Theory of Skrillex");
            map.put("Jawbreaker", "Jawbreaker (ZenthicAlpha)");
            return map;
        }

        private static List<String> initCreatorsNamesList() {
            List<String> names = new ArrayList<>();
            names.add("JerkRat");
            names.add("Jax");
            names.add("Scanbrux");
            names.add("Adiale");
            names.add("Creator Cloud");
            names.add("ZenthicAlpha");
            names.add("Berkoo");
            names.add("Riot");
            names.add("Mixroid");
            names.add("TrueNature");
            names.add("Zobros");
            names.add("MrCheeseTigrr");
            names.add("SirHadoken");
            names.add("Mitchell");
            names.add("FunnyGame");
            names.add("Serponge");
            names.add("TamaN");
            names.add("Jeyzor");
            names.add("Glittershroom43");
            names.add("Lyod");
            names.add("Rob Buck");
            names.add("TriAxis");
            names.add("Ggb0y");
            names.add("Caustic");
            names.add("Experience D");
            names.add("Jerry Bronze V");
            names.add("Rek3dge");
            names.add("WOOGI1411");
            names.add("TheRealSalad");
            names.add("Minesap");
            names.add("ChaSe");
            names.add("MasK463");
            names.add("M2coL");
            names.add("Dhafin");
            names.add("Knobbelboy");
            names.add("Glittershroom");
            names.add("FlappySheepy");
            names.add("KrmaL");
            return names;
        }

        private static Map<String, String> initCreatorsMapForReplacement() {
            Map<String, String> map = new HashMap<>();
            map.put("DiMaViKuLov26", "DimaVikulov26");
            map.put("DORABAE", "Dorabae");
            map.put("danolex", "Danolex");
            map.put("alkali", "Alkali");
            map.put("TheRealDarnoc", "Darnoc");
            map.put("IIINePtunEIII", "Neptune");
            map.put("timeless real", "Timeless real");
            map.put("noobas", "Noobas");
            map.put("haoN", "HaoN");
            map.put("TrusTa", "TrusTa (игрок)|TrusTa");
            map.put("lSunix", "Cyclic");
            map.put("Aeon Air", "AeonAir");
            map.put("Roli GD ", "Rolipso|Roli GD");
            map.put("zellink", "ZelLink");
            map.put("SUOMI", "Suomi");
            map.put("x8Px", "X8Px|x8Px");
            map.put("xtobe5", "Xtobe5|xtobe5");
            map.put("Spu7Nix", "Spu7nix");
            map.put("f3lixsram", "F3lixsram|f3lixsram");
            return map;
        }

        private static Map<String, String> initSpecialCreatorsNamesForLevels() {
            Map<String, String> map = new HashMap<>();
            map.put("Level Easy", "Cody");
            map.put("Sonic Wave", "Cyclic");
            return map;
        }
    }
}
