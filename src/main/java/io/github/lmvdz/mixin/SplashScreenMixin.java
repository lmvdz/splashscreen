package io.github.lmvdz.mixin;

import java.util.Optional;
import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.lmvdz.SplashScreenInitCallback;

import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SplashScreen;
import net.minecraft.resource.ResourceReloadMonitor;
import net.minecraft.util.Identifier;


@Mixin(SplashScreen.class)
public class SplashScreenMixin {

    public interface Access {
        @Accessor
        Identifier getLOGO();

        @Accessor
        void setLOGO(Identifier id);

        @Accessor
        MinecraftClient getClient();

        @Accessor
        ResourceReloadMonitor getReloadMonitor();

        @Accessor
        Consumer<Optional<Throwable>> getExceptionHandler();

        @Accessor
        boolean isReloading();

        @Accessor void setReloading(boolean reloading);

        @Accessor
        float getProgress();

        @Accessor void setProgress(float progress);

        @Accessor
        long getApplyCompleteTime();

        @Accessor void setApplyCompleteTime(long time);
        
        @Accessor
        long getPrepareCompleteTime();

        @Accessor void setPrepareCompleteTime(long time);

        @Invoker
        void renderProgressBar(int minX, int minY, int maxX, int maxY, float progress);
    }

    @Inject(at = @At(value="RETURN"), method = "<init>*")
    public void splash(MinecraftClient client, ResourceReloadMonitor monitor, Consumer<Optional<Throwable>> exceptionHandler, boolean reloading, CallbackInfo ci) {
        SplashScreenInitCallback.EVENT.invoker().splash(((SplashScreen)(Object) this), client);
    }
}