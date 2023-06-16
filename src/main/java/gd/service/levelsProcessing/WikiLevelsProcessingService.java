package gd.service.levelsProcessing;

import gd.generators.WikiResultDataGenerator;
import jdash.common.LevelBrowseMode;
import jdash.common.entity.GDLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WikiLevelsProcessingService extends AbstractLevelsProcessingService {

    private static final int LIST_SIZE = 50;
    private static final int GD_PAGE_SIZE = 10;


    private static List<GDLevel> mostDownloadedLevels;
    private static List<GDLevel> mostDownloadedDemons;

    private static List<GDLevel> mostLikedLevels;
    private static List<GDLevel> mostLikedDemons;

    public static void processWikiLevels() {
        List<GDLevel> downloadedLevels = getListForMostDownloadedLevels();
        List<GDLevel> likedLevels = getListForMostLikedLevels();

        mostDownloadedLevels = downloadedLevels.subList(0, LIST_SIZE);
        mostDownloadedDemons = downloadedLevels.stream().filter(GDLevel::isDemon).collect(Collectors.toList());
        mostLikedLevels = likedLevels.subList(0, LIST_SIZE);
        mostLikedDemons = likedLevels.stream().filter(GDLevel::isDemon).collect(Collectors.toList());

        WikiResultDataGenerator.processListsResults();
    }

    public static List<GDLevel> getListForMostDownloadedLevels() {
        return getListForType(LevelBrowseMode.MOST_DOWNLOADED);
    }

    public static List<GDLevel> getListForMostLikedLevels() {
        return getListForType(LevelBrowseMode.MOST_LIKED);
    }

    private static List<GDLevel> getListForType(LevelBrowseMode levelBrowseMode) {
        System.out.println("Generating list for this type: " + levelBrowseMode.name());
        return fillListWithLevels(levelBrowseMode);
    }

    public static List<GDLevel> fillListWithLevels(LevelBrowseMode levelBrowseMode) {
        List<GDLevel> list = new ArrayList<>();
        int demonsCount = 0;
        int i = 0;
        try {
            while (demonsCount < LIST_SIZE) {
                Thread.sleep(1100);
                List<GDLevel> levels = client.browseLevels(levelBrowseMode,null, null, i)
                        .collectList().block();
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
                    i++;
                } else {
                    throw new Exception("Can't load levels!");
                }
            }
        } catch (Exception e) {
            System.out.println("Limit reached for " + levelBrowseMode.name() + " levels. Message: " + e.getMessage());
        }
        return list;
    }

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
}
