package cookie.music.mixin;

import cookie.music.IMusicLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.sound.SoundPool;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = RenderEngine.class, remap = false)
public abstract class RenderEngineMixin {
	@Unique
	private final SoundPool soundPoolMusic = new SoundPool();

	@Shadow
	@Final
	public Minecraft mc;

	@Shadow
	private GameSettings gameSettings;

	@Inject(method = "initDynamicTextures", at = @At(value = "TAIL"))
	private void musicpacks_reloadSounds(List<Throwable> errors, CallbackInfo ci) {
		if (mc.sndManager instanceof IMusicLoader) {
			((IMusicLoader) mc.sndManager).bta_music_loader$loadTexturePackAudio(soundPoolMusic);
			mc.sndManager.loadSoundSettings(gameSettings);
		}
	}
}
