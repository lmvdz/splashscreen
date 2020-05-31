package io.github.lmvdz.client.mixin;

import java.util.Optional;
import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SplashScreen;
import net.minecraft.resource.ResourceReloadMonitor;


@Mixin(SplashScreen.class)
public interface SplashScreenAccess {

    @Accessor
    MinecraftClient getClient();

    @Accessor
    ResourceReloadMonitor getReloadMonitor();

    @Accessor
    Consumer<Optional<Throwable>> getExceptionHandler();

    @Accessor
    boolean isReloading();
}
