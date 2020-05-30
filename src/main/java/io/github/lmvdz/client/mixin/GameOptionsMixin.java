package io.github.lmvdz.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import io.github.lmvdz.client.GameOptionsInitCallback;
import net.minecraft.client.options.GameOptions;

@Mixin(GameOptions.class)
public class GameOptionsMixin {
    @Inject(at = @At("RETURN"), method = "<init>*")
    public void onInitGameOptions(CallbackInfo ci) {
        GameOptionsInitCallback.EVENT.invoker().onInit((GameOptions) (Object) this);
    }
}
