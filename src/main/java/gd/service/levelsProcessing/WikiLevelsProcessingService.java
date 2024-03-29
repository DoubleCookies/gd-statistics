package gd.service.levelsProcessing;

import gd.generators.WikiResultDataGenerator;
import jdash.common.LevelBrowseMode;
import jdash.common.entity.GDLevel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WikiLevelsProcessingService extends AbstractLevelsProcessingService {

    private static final Logger logger = LogManager.getLogger(WikiLevelsProcessingService.class);
    private static final int LIST_SIZE = 50;
    private static final int GD_PAGE_SIZE = 10;

    private static List<GDLevel> mostDownloadedLevels;
    private static List<GDLevel> mostDownloadedDemons;
    private static List<GDLevel> mostLikedLevels;
    private static List<GDLevel> mostLikedDemons;

    public static List<GDLevel> getMostDownloadedLevels() {
        return mostDownloadedLevels;
    }

    public static List<GDLevel> getMostDownloadedDemons() {
        return mostDownloadedDemons;
    }

    public static List<GDLevel> getMostLikedLevels() {
        return mostLikedLevels;
    }

    public static List<GDLevel> getMostLikedDemons() {
        return mostLikedDemons;
    }

    public static void processWikiLevels() {
        List<GDLevel> downloadedLevels = getListForMostDownloadedLevels();
        List<GDLevel> likedLevels = getListForMostLikedLevels();

        storeLevelsInfo(downloadedLevels, likedLevels);

        WikiResultDataGenerator.processListsResults();
    }

    private static List<GDLevel> getListForMostDownloadedLevels() {
        return getListForType(LevelBrowseMode.MOST_DOWNLOADED);
    }

    private static List<GDLevel> getListForMostLikedLevels() {
        return getListForType(LevelBrowseMode.MOST_LIKED);
    }

    private static List<GDLevel> getListForType(LevelBrowseMode levelBrowseMode) {
        System.out.println("Generating list for this type: " + levelBrowseMode.name());
        return fillListWithLevels(levelBrowseMode);
    }

    private static List<GDLevel> fillListWithLevels(LevelBrowseMode levelBrowseMode) {
        List<GDLevel> list = new ArrayList<>();
        int demonsCount = 0;
        int currentPage = 0;
        try {
            while (demonsCount < LIST_SIZE) {
                List<GDLevel> levels = getGdLevelsPage(levelBrowseMode, currentPage);
                if (levels != null) {
                    for (int j = 0; j < GD_PAGE_SIZE; j++) {
                        GDLevel level = levels.get(j);
                        list.add(level);
                        if (level.isDemon()) {
                            demonsCount++;
                            if (demonsCount % 10 == 0 && demonsCount > 0)
                                System.out.println("Found " + demonsCount + " demon levels.");
                        }
                        if (demonsCount >= LIST_SIZE)
                            break;
                    }
                    currentPage++;
                } else {
                    throw new Exception("Can't load levels!");
                }
            }
        } catch (Exception e) {
            logger.error("Limit reached for " + levelBrowseMode.name() + " levels. Message: " + e.getMessage());
        }
        return list;
    }

    private static void storeLevelsInfo(List<GDLevel> downloadedLevels, List<GDLevel> likedLevels) {
        mostDownloadedLevels = downloadedLevels.subList(0, LIST_SIZE);
        mostDownloadedDemons = downloadedLevels.stream().filter(GDLevel::isDemon).collect(Collectors.toList());
        mostLikedLevels = likedLevels.subList(0, LIST_SIZE);
        mostLikedDemons = likedLevels.stream().filter(GDLevel::isDemon).collect(Collectors.toList());
    }

}
