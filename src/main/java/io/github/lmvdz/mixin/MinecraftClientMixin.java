package io.github.lmvdz.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.lmvdz.MinecraftClientSetOverlayCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(at = @At(value = "RETURN"), method = "setOverlay")
    public void onSetOverlay(Overlay o, CallbackInfo ci) {
        MinecraftClientSetOverlayCallback.EVENT.invoker().afterOverlaySet();
    }
}