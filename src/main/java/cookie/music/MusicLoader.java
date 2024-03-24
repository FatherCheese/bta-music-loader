package cookie.music;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MusicLoader implements ModInitializer {
    public static final String MOD_ID = "music";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("MusicLoader has been initialized.");
    }
}
