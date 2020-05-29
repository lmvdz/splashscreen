package io.github.lmvdz.client;

import io.github.lmvdz.client.mixin.SoundManagerAccess;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.client.sound.TickableSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SplashScreenClientMod implements ClientModInitializer, IHasModId, IHasLogo,
        CustomSplashScreenManagerCallback, SoundManagerCreatedCallback {

    public static final Identifier MOJANG_SPLASH_SOUND_ID =
            new Identifier("splashscreen", "mojang_splash");

    public static Sound MOJANG_SPLASH_SOUND = new Sound("mojang_splash", 1.0F, 1.0F, 1,
            Sound.RegistrationType.FILE, false, false, 16);

    public static SoundEvent MOJANG_SPLASH_SOUND_EVENT = new SoundEvent(MOJANG_SPLASH_SOUND_ID);

    public static SoundManager SoundManagerInstance;

    @Override
    public void onInitializeClient() {

        SoundManagerCreatedCallback.EVENT.register(this);

        Registry.register(Registry.SOUND_EVENT, MOJANG_SPLASH_SOUND_ID, MOJANG_SPLASH_SOUND_EVENT);

        CustomSplashScreenManager.INSTANCE =
                new CustomSplashScreenManager((CustomSplashScreenManager manager) -> {
                    SplashScreenCreatedCallback.EVENT.register(manager);
                    SplashScreenRenderCallback.EVENT.register(manager);
                    CustomSplashScreenManager.register(this, getLogo(), 128);
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

    @Override
    public void customSplashScreenCreated(CustomSplashScreen customSplashScreen) {
        if (CustomSplashScreenManager.activeSplashScreen == null
                && customSplashScreen.defaultLogo.equals(getLogo())) {
            CustomSplashScreenManager.activeSplashScreen = customSplashScreen.defaultLogo;
        }
    }

    @Override
    public void soundManagerCreated(SoundManager soundManager) {
        SplashScreenClientMod.SoundManagerInstance = soundManager;
        SplashScreenClientMod.SoundManagerInstance.play(PositionedSoundInstance
                .master(SplashScreenClientMod.MOJANG_SPLASH_SOUND_EVENT, .5F));
    }
}
