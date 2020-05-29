package io.github.lmvdz.client.mixin;

import java.util.Optional;
import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;

import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SplashScreen;
import net.minecraft.resource.ResourceReloadMonitor;
import net.minecraft.util.Identifier;


@Mixin(SplashScreen.class)
public interface SplashScreenAccess {
    @Accessor
    Identifier getLOGO();

    @Accessor
    MinecraftClient getClient();

    @Accessor
    ResourceReloadMonitor getReloadMonitor();

    @Accessor
    Consumer<Optional<Throwable>> getExceptionHandler();

    @Accessor
    boolean isReloading();

    @Accessor
    void setReloading(boolean reloading);

    @Accessor
    float getProgress();

    @Accessor
    void setProgress(float progress);

    @Accessor
    long getApplyCompleteTime();

    @Accessor
    void setApplyCompleteTime(long time);

    @Accessor
    long getPrepareCompleteTime();

    @Accessor
    void setPrepareCompleteTime(long time);

    @Invoker
    void invokeRenderProgressBar(int minX, int minY, int maxX, int maxY, float progress);
}
