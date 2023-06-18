package gd.service.levelsProcessing;

import jdash.client.GDClient;
import jdash.common.LevelBrowseMode;
import jdash.common.entity.GDLevel;

import java.util.List;

public abstract class AbstractLevelsProcessingService {

    static final GDClient client = GDClient.create();

    public static List<GDLevel> getGdLevelsPage(LevelBrowseMode levelBrowseMode, int page) throws InterruptedException {
        Thread.sleep(1100);
        return client.browseLevels(levelBrowseMode,null, null, page)
                .collectList().block();
    }
}
