package io.github.lmvdz.client;

import java.util.concurrent.CompletableFuture;
import io.github.lmvdz.client.mixin.SoundManagerAccess;
import io.github.lmvdz.client.mixin.SoundSystemAccess;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.AudioStream;
import net.minecraft.client.sound.Channel;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundEngine;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.client.sound.TickableSoundInstance;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.resource.ResourceType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SplashScreenClientMod implements ClientModInitializer, IHasModId, IHasLogo,
        CustomSplashScreenManagerCallback, SoundManagerCreatedCallback {


    public static Sound MOJANG_SPLASH_SOUND = new Sound("splashscreen:mojang_splash", 1.0F, 1.0F, 1,
            Sound.RegistrationType.FILE, true, true, 16);

    public static SoundManager SoundManagerInstance;


    @Override
    public void onInitializeClient() {

        SoundManagerCreatedCallback.EVENT.register(this);

        // GameOptionsInitCallback.EVENT.register(this);

        // Registry.register(Registry.SOUND_EVENT, MOJANG_SPLASH_SOUND_ID,
        // MOJANG_SPLASH_SOUND_EVENT);

        CustomSplashScreenManager.INSTANCE =
                new CustomSplashScreenManager((CustomSplashScreenManager manager) -> {
                    SplashScreenCreatedCallback.EVENT.register(manager);
                    SplashScreenRenderCallback.EVENT.register(manager);
                    CustomSplashScreenManager.register(this, getLogo(), getNumberOfFrames());
                });
        System.out.println("[SplashScreen] Initialized.");
    }

    @Override
    public String getModId() {
        return "splashscreen";
    }

    @Override
    public Identifier getLogo() {
        return new Identifier(getModId(), "textures/gui/splash/mojang/mp4/splash_0.png");
    }

    public int getNumberOfFrames() {
        return 251;
    }

    @Override
    public void customSplashScreenCreated(CustomSplashScreen customSplashScreen) {
        if (CustomSplashScreenManager.activeSplashScreen == null
                && customSplashScreen.defaultLogo.equals(getLogo())) {
            CustomSplashScreenManager.activeSplashScreen = customSplashScreen.defaultLogo;
        }
    }

    @Override
    public void soundManagerCreated(SoundManager soundManager) {
        SoundManagerInstance = soundManager;
        MOJANG_SPLASH_SOUND.preload(((SoundManagerAccess) SoundManagerInstance).getSoundSystem());
        ((SoundSystemAccess) ((SoundManagerAccess) SoundManagerInstance).getSoundSystem())
                .invokeStart();

    }
}
