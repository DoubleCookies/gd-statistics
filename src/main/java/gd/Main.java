package gd;

import gd.service.levelsProcessing.LevelsProcessingService;
import gd.service.levelsProcessing.WikiLevelsProcessingService;

public class Main {

    public static void main(String[] args) {
        processLevelsForWiki();
        processAllLevels();

    }
    private static void processLevelsForWiki() {
        WikiLevelsProcessingService.processWikiLevels();
    }

    private static void processAllLevels() {
        LevelsProcessingService.processAllLevels(SortingCode.DEFAULT);
    }
}
