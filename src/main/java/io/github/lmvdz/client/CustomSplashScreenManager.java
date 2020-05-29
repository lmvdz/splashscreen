package io.github.lmvdz.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import io.github.lmvdz.client.mixin.SplashScreenAccess;
import io.github.lmvdz.client.mixin.MinecraftClientAccess;
import io.github.lmvdz.client.mixin.MinecraftClientMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.SplashScreen;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public class CustomSplashScreenManager implements SplashScreenRenderCallback,
        SplashScreenCreatedCallback, MinecraftClientSetOverlayCallback {

    public static CustomSplashScreenManager INSTANCE;

    public static final HashMap<Identifier, CustomSplashScreen> splashScreens = new HashMap<>();
    public static final HashMap<Identifier, List<Identifier>> splashScreenLogos = new HashMap<>();

    public static boolean injectCustomSplashScreen = true;
    public static Identifier activeSplashScreen;
    public MinecraftClient client = MinecraftClient.getInstance();

    public CustomSplashScreenManager(Consumer<CustomSplashScreenManager> callback) {
        callback.accept(this);
    }

    public static void register(CustomSplashScreenManagerCallback objectToRegister, Identifier id,
            List<Identifier> logos) {
        splashScreens.put(id, null);
        splashScreenLogos.put(id, logos);
        CustomSplashScreenManagerCallback.EVENT.register(objectToRegister);
    }

    public static void register(CustomSplashScreenManagerCallback objectToRegister, Identifier id,
            int frames) {
        splashScreens.put(id, null);
        List<Identifier> logos = new ArrayList<>(frames);
        for (int x = 0; x < frames; x++) {
            logos.add(x,
                    new Identifier(id.getNamespace(),
                            id.getPath().replace("frame_0.png", "frame_" + x + ".png")
                                    .replace("splash_0.png", "splash_" + x + ".png")));
        }
        splashScreenLogos.put(id, logos);
        CustomSplashScreenManagerCallback.EVENT.register(objectToRegister);
    }

    public static void register(CustomSplashScreenManagerCallback objectToRegister, Identifier id) {
        splashScreens.put(id, null);
        splashScreenLogos.put(id, null);
        CustomSplashScreenManagerCallback.EVENT.register(objectToRegister);
    }

    @Override
    public void onRender(SplashScreen splashScreen, int mouseX, int mouseY, float delta,
            Consumer<Boolean> cancel) {
        if (activeSplashScreen != null) {
            CustomSplashScreen cSplashScreen = splashScreens.get(activeSplashScreen);
            if (injectCustomSplashScreen && client != null && cSplashScreen != null) {
                cancel.accept(true);
                if (client.overlay != cSplashScreen) {
                    client.overlay = cSplashScreen;
                }
                cSplashScreen.render(mouseX, mouseY, delta);
            }
        } else {
            computeCustomSplashScreensIfAbsent(splashScreen);
            cancel.accept(false);
        }
    }

    @Override
    public void onNewSplashScreen(SplashScreen splashScreen) {
        if (activeSplashScreen != null) {
            CustomSplashScreen activeCustomSplashScreen = splashScreens.get(activeSplashScreen);
            if (injectCustomSplashScreen && client != null) {
                if (activeCustomSplashScreen != null) {
                    Identifier key = activeCustomSplashScreen.defaultLogo;
                    List<Identifier> logos = splashScreenLogos.get(key);
                    splashScreens.put(key, new CustomSplashScreen(
                            ((SplashScreenAccess) (SplashScreen) splashScreen).getClient(),
                            ((SplashScreenAccess) (SplashScreen) splashScreen).getReloadMonitor(),
                            ((SplashScreenAccess) (SplashScreen) splashScreen)
                                    .getExceptionHandler(),
                            ((SplashScreenAccess) (SplashScreen) splashScreen).isReloading(), key,
                            logos));
                    activeCustomSplashScreen = splashScreens.get(key);
                    if (client.overlay != activeCustomSplashScreen) {
                        client.overlay = activeCustomSplashScreen;
                    }
                } else {
                    computeCustomSplashScreensIfAbsent(splashScreen);
                }
            }
        } else {
            computeCustomSplashScreensIfAbsent(splashScreen);
        }
    }

    public void computeCustomSplashScreensIfAbsent(SplashScreen splashScreen) {
        splashScreens.keySet().forEach(key -> splashScreens.computeIfAbsent(key,
                keyToCreate -> new CustomSplashScreen(
                        ((SplashScreenAccess) (SplashScreen) splashScreen).getClient(),
                        ((SplashScreenAccess) (SplashScreen) splashScreen).getReloadMonitor(),
                        ((SplashScreenAccess) (SplashScreen) splashScreen).getExceptionHandler(),
                        ((SplashScreenAccess) (SplashScreen) splashScreen).isReloading(),
                        keyToCreate, splashScreenLogos.get(keyToCreate))));
    }

    @Override
    public void afterOverlaySet(Overlay o, Consumer<Boolean> cancel) {

    }

}
