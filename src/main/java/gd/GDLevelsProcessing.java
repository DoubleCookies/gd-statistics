package gd;

import jdash.client.GDClient;
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
public class GDLevelsProcessing {

    private static final Logger logger = LogManager.getLogger(GDLevelsProcessing.class);
    private static final GDClient client = GDClient.create();
    private static final int DEMONS_LIST_SIZE = 50;

    private static final Comparator<GDLevel> defaultDescendingIdComparator = (o1, o2) -> (int) (o2.id() - o1.id());
    private static final Comparator<GDLevel> descendingLikesComparator = (o1, o2) -> (int) (o2.likes() - o1.likes());
    private static final Comparator<GDLevel> ascendingLikesComparator = (o1, o2) -> (int) (o1.likes() - o2.likes());
    private static final Comparator<GDLevel> descendingDownloadsComparator = (o1, o2) -> (int) (o2.downloads() - o1.downloads());
    private static final Comparator<GDLevel> ascendingDownloadsComparator = (o1, o2) -> (int) (o1.downloads() - o2.downloads());
    private static final Comparator<GDLevel> descriptionLengthComparator = (o1, o2) -> (int) (o2.description().length() - o1.description().length());

    public static List<GDLevel> levels;
    public static List<GDLevel> epicLevels;

    public static List<GDLevel> processFeaturedLevels(SortingCode sortingCode) {
        logger.info("Receiving featured levels list");
        levels = getFeaturedLevels();
        sortLevelList(levels, sortingCode);
        return levels;
    }

    public static List<GDLevel> processEpicLevels(SortingCode sortingCode) {
        logger.info("Filter epic levels");
        epicLevels = levels.stream().filter(GDLevel::isEpic).collect(Collectors.toList());
        sortLevelList(epicLevels, sortingCode);
        return epicLevels;
    }

    public static List<GDLevel> getMostPopularDemons() {
        List<GDLevel> list = new ArrayList<>();
        if (!levels.isEmpty()) {
            levels.sort(descendingDownloadsComparator);
            list = levels.stream().filter(GDLevel::isDemon).limit(DEMONS_LIST_SIZE).collect(Collectors.toList());
        }
        return list;
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
            logger.error("Exception during getting data: " + e + "\r\n" + e.getCause().getMessage());
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
