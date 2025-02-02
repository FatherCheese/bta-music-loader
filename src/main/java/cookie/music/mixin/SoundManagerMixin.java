package cookie.music.mixin;

import cookie.music.IMusicLoader;
import cookie.music.MusicLoader;
import cookie.music.UnzipUtility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.SoundPool;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.*;
import java.nio.file.Files;
import java.util.zip.ZipFile;

@Mixin(value = SoundManager.class, remap = false)
public abstract class SoundManagerMixin implements IMusicLoader {
	@Unique
	private boolean customResources;

	@Shadow
	@Final
	private SoundPool soundPoolMusic;

	@Shadow
	private Minecraft mc;

	@Shadow
	public abstract void playRandomMusicIfReady();

	@Shadow
	private int ticksBeforeMusic;

	@Unique
	public void bta_music_loader$loadTexturePackAudio(SoundPool soundPool) {
		mc = Minecraft.getMinecraft(Minecraft.class);

		File temp = new File(mc.getMinecraftDir() + "/temp/");
		UnzipUtility.deleteFolder(temp);

		String musicLoc = "/music/";
		try (InputStream stream = mc.texturePackList.getResourceAsStream(musicLoc)) {
			MusicLoader.LOGGER.info("Music folder found, gathering files and attempting to clear the music sound pool...");



			File music;
			if (mc.texturePackList.getHighestPriorityPack().fileName.endsWith(".zip")) {
				temp.mkdirs();
				UnzipUtility.unzip(mc.getMinecraftDir() + "/texturepacks/" + mc.texturePackList.getHighestPriorityPack().fileName, temp.getPath());
				music = new File(temp, musicLoc);
			} else {
				music = new File(mc.getMinecraftDir() + "/texturepacks/" + mc.texturePackList.getHighestPriorityPack().fileName + "/" + musicLoc);
			}

			File[] list = music.listFiles();

			if (list != null && list.length > 0) {
				customResources = true;
                for (File file : list) {
                    if (file.isFile()) {
						MusicLoader.LOGGER.info("Added {} to music sound pool.", file.getName());
                        String s = file.getPath().substring(music.getPath().length() + 1).replace('\\', '/');
                        soundPool.addSound(s, file);
                    }
                }
			} else {
				MusicLoader.LOGGER.error("Texture pack music folder is empty!");
				customResources = false;
			}
		} catch (Exception ignored) {
			MusicLoader.LOGGER.info("No music folder found, ignoring.");
		}
	}

	@Inject(method = "loadSoundSettings", at = @At(value = "TAIL"))
	private void music_loadTextureMusicFolder(GameSettings gamesettings, CallbackInfo ci) {
		if (customResources) {
			((SoundPoolAccessor) soundPoolMusic).getSoundPoolEntries().clear();
			bta_music_loader$loadTexturePackAudio(soundPoolMusic);

			MusicLoader.LOGGER.info("Music Sound pool replaced by texture pack.");
		}
	}
}
