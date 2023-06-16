package gd;

import jdash.common.DemonDifficulty;
import jdash.common.Difficulty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {

    public static final int LIST_SIZE = 50;
    public static final String WIKITABLE_START = "{| class=\"wikitable\"\n" +
            "! Место\n" +
            "! Уровень\n" +
            "! Создатель\n" +
            "! [[Уровни сложности|Сложность]]\n" +
            "! Кол-во загрузок\n" +
            "! Кол-во лайков\n";
    public static final String WIKITABLE_END = "|}";
    public static final String WIKITABLE_NEWLINE = "|-\n";
    public static final String ARRAY_START = "{{#arraydefine:levels|TEST_VALUE_FOR_SHIFT,\n";
    public static final String ARRAY_END = "}}{{#vardefineecho:number|{{#expr:{{#arraysearch:levels|{{{1}}}}}}}}}<noinclude>{{doc}}[[Категория:Информационные шаблоны]]</noinclude>";


    //Difficulties map for replace in text
    public static final Map<Difficulty, String> difficultyStringMap = initMapForTemplates();

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

    //Demon difficulties map for replace in text
    public static final Map<DemonDifficulty, String> demonDifficultyStringMap = initMapForDemonTemplates();

    private static Map<DemonDifficulty, String> initMapForDemonTemplates() {
        Map<DemonDifficulty, String> map = new HashMap<>();
        map.put(DemonDifficulty.EASY, "лёгкий демон");
        map.put(DemonDifficulty.MEDIUM, "средний демон");
        map.put(DemonDifficulty.HARD, "демон");
        map.put(DemonDifficulty.INSANE, "безумный демон");
        map.put(DemonDifficulty.EXTREME, "экстремальный демон");
        return map;
    }

    /* Levels list which have article on wiki (with other article name) */
    public static final Map<String, String> LEVELS_WITH_DIFFERENT_NAME = initLevelsMapWithDifferentName();

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

    /* Creators list which have article on wiki */
    public static final List<String> allowedCreatorsNames = initCreatorsNamesList();

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

    /* Creators list which have article on wiki (with other article name) */
    public static final Map<String, String> allowedCreatorsNamesWithReplacement = initCreatorsMapForReplacement();

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

    public static final Map<String, String> specialCreatorsNamesForLevels = initSpecialCreatorsNamesForLevels();

    private static Map<String, String> initSpecialCreatorsNamesForLevels() {
        Map<String, String> map = new HashMap<>();
        map.put("Level Easy", "Cody");
        map.put("Sonic Wave", "Cyclic");
        return map;
    }
}
