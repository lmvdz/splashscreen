package io.github.lmvdz.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import io.github.lmvdz.client.callback.SplashScreenCreatedCallback;
import io.github.lmvdz.client.callback.SplashScreenRenderCallback;
import net.minecraft.client.gui.screen.SplashScreen;


@Mixin(SplashScreen.class)
public class SplashScreenMixin {

    @Inject(at = @At("HEAD"), cancellable = true, method = "render")
    public void onRender(int mouseX, int mouseY, float delta, CallbackInfo ci) {
        SplashScreenRenderCallback.EVENT.invoker().onRender((SplashScreen) (Object) this, mouseX,
                mouseY, delta, (Boolean cancel) -> {
                    if (cancel) {
                        ci.cancel();
                    }
                });
    }

    @Inject(at = @At("RETURN"), method = "<init>*")
    public void onNewSplashScreen(CallbackInfo ci) {
        SplashScreenCreatedCallback.EVENT.invoker().onNewSplashScreen((SplashScreen) (Object) this);
    }

}
