package cookie.music.mixin;

import net.minecraft.client.sound.SoundPool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(value = SoundPool.class, remap = false)
public interface SoundPoolAccessor {

	@Accessor("allSoundPoolEntries")
	List getSoundPoolEntries();
}
