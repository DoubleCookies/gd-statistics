package gd.service.levelsProcessing;

import gd.SortingCode;
import gd.generators.ResultDataGenerator;
import jdash.client.exception.GDClientException;
import jdash.common.LevelBrowseMode;
import jdash.common.entity.GDLevel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

import static gd.SortingCode.*;

/**
 * Class for getting levels from database
 */
public class LevelsProcessingService extends AbstractLevelsProcessingService {

    private static final Logger logger = LogManager.getLogger(LevelsProcessingService.class);
    private static final int DEMONS_LIST_SIZE = 50;

    private static final Comparator<GDLevel> defaultDescendingIdComparator = Comparator.comparingLong(GDLevel::id).reversed();
    private static final Comparator<GDLevel> descendingLikesComparator = Comparator.comparingInt(GDLevel::likes).reversed();
    private static final Comparator<GDLevel> ascendingLikesComparator = Comparator.comparingInt(GDLevel::likes);
    private static final Comparator<GDLevel> descendingDownloadsComparator = Comparator.comparingInt(GDLevel::downloads).reversed();
    private static final Comparator<GDLevel> ascendingDownloadsComparator = Comparator.comparingInt(GDLevel::downloads);
    private static final Comparator<GDLevel> descriptionLengthComparator = (o1, o2) -> o2.description().length() - o1.description().length();
    private static final Map<SortingCode, Comparator<GDLevel>> levelsComparatorMap = initComparatorsMap();

    private static Map<SortingCode, Comparator<GDLevel>> initComparatorsMap() {
        Map<SortingCode, Comparator<GDLevel>> comparatorMap = new HashMap<>();
        comparatorMap.put(DESCENDING_LIKES, descendingLikesComparator);
        comparatorMap.put(ASCENDING_LIKES, ascendingLikesComparator);
        comparatorMap.put(DESCENDING_DOWNLOADS, descendingDownloadsComparator);
        comparatorMap.put(ASCENDING_DOWNLOADS, ascendingDownloadsComparator);
        comparatorMap.put(LONGEST_DESCRIPTION, descriptionLengthComparator);
        comparatorMap.put(DEFAULT, defaultDescendingIdComparator);
        return comparatorMap;
    }


    private static List<GDLevel> featuredLevels;
    private static List<GDLevel> epicLevels;

    private static List<GDLevel> popularDemonsList;

    public static List<GDLevel> getFeaturedLevels() {
        return featuredLevels;
    }

    public static List<GDLevel> getEpicLevels() {
        return epicLevels;
    }

    public static List<GDLevel> getPopularDemonsList() {
        return popularDemonsList;
    }

    public static void processAllLevels(SortingCode sortingCode) {
        processFeaturedLevels(sortingCode);
        processEpicLevels(sortingCode);
        processMostPopularDemons();

        ResultDataGenerator.processAllLevels(sortingCode);
    }

    private static void processFeaturedLevels(SortingCode sortingCode) {
        logger.info("Receiving featured levels list");
        featuredLevels = getFeaturedLevelsPage();
        sortLevelList(featuredLevels, sortingCode);
    }

    private static void processEpicLevels(SortingCode sortingCode) {
        logger.info("Filtering featured levels to get only epic levels list");
        epicLevels = featuredLevels.stream().filter(GDLevel::isEpic).collect(Collectors.toList());
        sortLevelList(epicLevels, sortingCode);
    }

    private static void processMostPopularDemons() {
        List<GDLevel> list = new ArrayList<>();
        if (!featuredLevels.isEmpty()) {
            featuredLevels.sort(descendingDownloadsComparator);
            list = featuredLevels.stream().filter(GDLevel::isDemon).limit(DEMONS_LIST_SIZE).collect(Collectors.toList());
        }
        popularDemonsList = list;
    }

    private static List<GDLevel> getFeaturedLevelsPage() {
        List<GDLevel> list = new ArrayList<>();
        int currentPage = 0;
        try {
            while (true) {
                List<GDLevel> levels = getGdLevelsPage(LevelBrowseMode.FEATURED, currentPage);
                if (levels != null)
                    list.addAll(levels);
                else
                    break;
                if (currentPage % 10 == 0) {
                    logger.info("Processing page " + currentPage);
                }
                currentPage++;
            }
        } catch (GDClientException | InterruptedException e) {
            logger.error("Exception during getting data: " + e + "; message: " + e.getCause().getMessage());
        }
        return list;
    }

    public static void sortLevelList(List<GDLevel> list, SortingCode sortingCode) {
        Comparator<GDLevel> comparator = levelsComparatorMap.get(sortingCode);
        list.sort(comparator);
    }
}
