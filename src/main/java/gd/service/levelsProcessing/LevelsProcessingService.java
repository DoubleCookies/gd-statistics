package gd.service.levelsProcessing;

import gd.SortingCode;
import gd.generators.ResultDataGenerator;
import jdash.client.exception.GDClientException;
import jdash.common.LevelBrowseMode;
import jdash.common.entity.GDLevel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    private static List<GDLevel> levels;
    private static List<GDLevel> epicLevels;

    private static List<GDLevel> popularDemonsList;

    public static List<GDLevel> getLevels() {
        return levels;
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

    public static void processFeaturedLevels(SortingCode sortingCode) {
        logger.info("Receiving featured levels list");
        levels = getFeaturedLevels();
        sortLevelList(levels, sortingCode);
    }

    public static void processEpicLevels(SortingCode sortingCode) {
        logger.info("Filter epic levels");
        epicLevels = levels.stream().filter(GDLevel::isEpic).collect(Collectors.toList());
        sortLevelList(epicLevels, sortingCode);
    }

    public static void processMostPopularDemons() {
        List<GDLevel> list = new ArrayList<>();
        if (!levels.isEmpty()) {
            levels.sort(descendingDownloadsComparator);
            list = levels.stream().filter(GDLevel::isDemon).limit(DEMONS_LIST_SIZE).collect(Collectors.toList());
        }
        popularDemonsList = list;
    }

    private static List<GDLevel> getFeaturedLevels() {
        List<GDLevel> list = new ArrayList<>();
        int currentPage = 0;
        try {
            while (true) {
                Thread.sleep(1100);
                List<GDLevel> levels = client.browseLevels(LevelBrowseMode.FEATURED,null, null, currentPage)
                        .collectList().block();
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


}
