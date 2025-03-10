package org.tessa.prelaunch;
import org.tessa.prelaunch.api.QuickPlay;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TessaPreLaunch implements PreLaunchEntrypoint {

    public static final Logger logger = LoggerFactory.getLogger(TessaPreLaunch.class);

    @Override
    public void onPreLaunch() {
        // insert your prelaunch logic here
        QuickPlay quickPlay = QuickPlay.play().setServerAddress("deathcats.org");

    }

}
