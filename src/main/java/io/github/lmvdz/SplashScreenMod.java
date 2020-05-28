package io.github.lmvdz;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class SplashScreenMod implements ModInitializer, IHasModId, IHasLogo, CustomSplashScreenManagerCallback {

    public static CustomSplashScreen myCustomSplashScreen;

    @Override
    public void onInitialize() {
        System.out.println("initialized");
        CustomSplashScreenManager.INSTANCE.register(getLogo(), 128);
    }

    @Override
    public String getModId() {
        return "splashscreenshader";
    }

    @Override
    public Identifier getLogo() {
        return new Identifier(getModId(), "textures/gui/splash/mojang/frame_0.png");
    }

    @Override
    public void customSplashScreenCreated(CustomSplashScreen customSplashScreen) {
        if (myCustomSplashScreen == null && customSplashScreen.getLogo() == getLogo()) {
            myCustomSplashScreen = customSplashScreen;
            CustomSplashScreenManager.INSTANCE.activeSplashScreen = myCustomSplashScreen.getLogo();
        }
    }
}
