package io.github.lmvdz.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import io.github.lmvdz.client.callback.SoundManagerCreatedCallback;
import net.minecraft.client.sound.SoundManager;

@Mixin(SoundManager.class)
public class SoundManagerMixin {
    @Inject(at = @At("RETURN"), method = "<init>*")
    public void onSoundManagerCreated(CallbackInfo ci) {
        SoundManagerCreatedCallback.EVENT.invoker()
                .soundManagerCreated((SoundManager) (Object) this);
    }
}
