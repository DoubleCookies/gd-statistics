package gd;

import jdash.common.entity.GDLevel;

import java.util.List;

/**
 * Main class for levels data processing
 */
public class ResponseGenerator {
    static String[] processFeaturedLevels(SortingCode sortingCode) {
        List<GDLevel> featuredLevels = GDLevelsProcessing.processFeaturedLevels(sortingCode);
        return processLevels(featuredLevels);
    }

    static String[] processEpicLevels(SortingCode sortingCode) {
        List<GDLevel> epicLevels = GDLevelsProcessing.processEpicLevels(sortingCode);
        return processLevels(epicLevels);
    }

    static String generateTopDemonsList() {
        List<GDLevel> popularDemonsList = GDLevelsProcessing.getMostPopularDemons();
        return ResultDataGenerator.processTopDemons(popularDemonsList);
    }

    private static String[] processLevels(List<GDLevel> levels) {
        if (isLevelsListEmpty(levels))
            return null;
        return ResultDataGenerator.getLevelsInformation(levels);
    }

    private static boolean isLevelsListEmpty(List<GDLevel> levels) {
        return levels == null || levels.size() == 0;
    }
}
