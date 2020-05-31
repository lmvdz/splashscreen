package io.github.lmvdz.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import io.github.lmvdz.client.callback.SoundManagerCreatedCallback;
import io.github.lmvdz.client.callback.SplashScreenCreatedCallback;
import io.github.lmvdz.client.callback.SplashScreenRenderCallback;
import io.github.lmvdz.client.mixin.SoundManagerAccess;
import io.github.lmvdz.client.mixin.SoundSystemAccess;
import io.github.lmvdz.client.mixin.SplashScreenAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SplashScreen;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.util.Identifier;

public class CustomSplashScreenManager implements SplashScreenRenderCallback,
        SplashScreenCreatedCallback, SoundManagerCreatedCallback {

    public static final CustomSplashScreenManager INSTANCE = new CustomSplashScreenManager();
    static {
        SplashScreenCreatedCallback.EVENT.register(INSTANCE);
        SplashScreenRenderCallback.EVENT.register(INSTANCE);
        SoundManagerCreatedCallback.EVENT.register(INSTANCE);
    }
    public static SoundManager SoundManagerInstance;

    public static final HashMap<Identifier, CustomSplashScreen> splashScreens = new HashMap<>();
    public static final HashMap<Identifier, List<Identifier>> splashScreenLogos = new HashMap<>();
    public static final HashMap<Identifier, Sound> splashScreenSounds = new HashMap<>();

    public static boolean injectCustomSplashScreen = true;
    public static Identifier activeSplashScreenIdentifier;
    public MinecraftClient client = MinecraftClient.getInstance();
    
    public static void setActiveSplashScreen(Identifier id) {
        activeSplashScreenIdentifier = id;
    }

    public static void setActiveSplashScreen(CustomSplashScreen css) {
        activeSplashScreenIdentifier = css.defaultLogo;
    }

    public static void register(Identifier id, int numberOfLogoTextures, Sound s) {
        splashScreens.put(id, null);
        List<Identifier> logos = new ArrayList<>(numberOfLogoTextures);
        for (int x = 0; x < numberOfLogoTextures; x++) {
            logos.add(x,
                    new Identifier(id.getNamespace(),
                    id.getPath().replace("_0", "_" + x)));
        }
        splashScreenLogos.put(id, logos);
        splashScreenSounds.put(id, s);
        activeSplashScreenIdentifier = (activeSplashScreenIdentifier == null) ? id : activeSplashScreenIdentifier;        
    }

    @Override
    public void onRender(SplashScreen splashScreen, int mouseX, int mouseY, float delta,
            Consumer<Boolean> cancel) {
        if (activeSplashScreenIdentifier != null) {
            CustomSplashScreen activeCustomSplashScreen = splashScreens.get(activeSplashScreenIdentifier);
            if (injectCustomSplashScreen && client != null && activeCustomSplashScreen != null) {
                cancel.accept(true);
                if (activeCustomSplashScreen.client.overlay != activeCustomSplashScreen) {
                    activeCustomSplashScreen.client.overlay = activeCustomSplashScreen;
                }
                activeCustomSplashScreen.render(mouseX, mouseY, delta);
            }
        } else {
            computeCustomSplashScreensIfAbsent(splashScreen);
            cancel.accept(false);
        }
    }

    /**
     * When a new SplashScreen() is created.
     * Compute CustomSplashScreen
     */
    @Override
    public void onNewSplashScreen(SplashScreen splashScreen) {
        System.out.println("new splash screen");
        computeCustomSplashScreensIfAbsent(splashScreen);
        resetActiveCustomSplashScreen(splashScreen);
    }

    public void resetActiveCustomSplashScreen(SplashScreen supplier) {
        if (activeSplashScreenIdentifier != null) {
            CustomSplashScreen css = splashScreens.get(activeSplashScreenIdentifier);
            if (css != null) {
                css.reset(supplier);
                splashScreens.put(activeSplashScreenIdentifier, css);
            }
        }
    }

    public void computeCustomSplashScreensIfAbsent(SplashScreen splashScreen) {
        splashScreens.keySet().forEach(key -> splashScreens.computeIfAbsent(key,
                keyToCreate -> new CustomSplashScreen(
                        ((SplashScreenAccess) (SplashScreen) splashScreen).getClient(),
                        ((SplashScreenAccess) (SplashScreen) splashScreen).getReloadMonitor(),
                        ((SplashScreenAccess) (SplashScreen) splashScreen).getExceptionHandler(),
                        ((SplashScreenAccess) (SplashScreen) splashScreen).isReloading(),
                        keyToCreate, splashScreenLogos.get(keyToCreate), splashScreenSounds.get(keyToCreate))));
    }

    @Override
    public void soundManagerCreated(SoundManager soundManager) {
        SoundManagerInstance = soundManager;

        splashScreenSounds.keySet().forEach(soundId -> {
            splashScreenSounds.get(soundId).preload(((SoundManagerAccess) SoundManagerInstance).getSoundSystem());
        });

        ((SoundSystemAccess) ((SoundManagerAccess) SoundManagerInstance).getSoundSystem())
                .invokeStart();

    }

}
