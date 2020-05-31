package io.github.lmvdz.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.sound.Sound;
import net.minecraft.util.Identifier;

public class SplashScreenClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // CustomSplashScreenManager.register(
        // new Identifier("splashscreen", "textures/gui/splash/mojang/mp4/splash_0.png"), 251,
        // new Sound("splashscreen:mojang_splash", 1.0F, 1.0F, 1, Sound.RegistrationType.FILE,
        // true, true, 16));
        CustomSplashScreenManager.register(
                new Identifier("splashscreen", "textures/gui/splash/mojang/mp4/splash_0.png"), 251);

        System.out.println("[SplashScreenMod] Initialized.");
    }
}
