package io.github.lmvdz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.github.lmvdz.mixin.SplashScreenMixin.Access;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SplashScreen;
import net.minecraft.util.Identifier;

public class CustomSplashScreenManager implements SplashScreenInitCallback, MinecraftClientSetOverlayCallback {

    public static CustomSplashScreenManager INSTANCE = new CustomSplashScreenManager();

    public static final HashMap<Identifier, CustomSplashScreen> splashScreens = new HashMap<>();
    public static final HashMap<Identifier, List<Identifier>> splashScreenLogos = new HashMap<>();
    
    public boolean injectCustomSplashScreen = true;
    public Identifier activeSplashScreen;
    public MinecraftClient client;

    @Override
    public void splash(SplashScreen splashScreen, MinecraftClient client) {
        if (this.client == null) {
            this.client = client;
        }
        System.out.println("splash");
        splashScreens.keySet().forEach(key -> splashScreens.computeIfAbsent(key, keyToCreate -> new CustomSplashScreen(client, ((Access)splashScreen).getReloadMonitor(), ((Access)splashScreen).getExceptionHandler(), ((Access)splashScreen).isReloading(), keyToCreate, splashScreenLogos.get(keyToCreate))));
    }

    public void register(Identifier id, List<Identifier> logos) {
        splashScreens.put(id, null);
        splashScreenLogos.put(id, logos);
    }

    public void register(Identifier id, int frames) {
        splashScreens.put(id, null);
        List<Identifier> logos = new ArrayList<>(frames);
        for (int x = 0; x < frames; x++) {
            logos.add(x, new Identifier(id.getNamespace(), id.getPath().replace("frame_0", "frame_"+x)));
        }
        splashScreenLogos.put(id, logos);
    }

    public static void register(Identifier id) {
        splashScreens.put(id, null);
        splashScreenLogos.put(id, null);
    }

    @Override
    public void afterOverlaySet() {
        System.out.println("after overlay set");
        if (injectCustomSplashScreen && client != null) {
            client.setOverlay(splashScreens.get(activeSplashScreen));
        }
    }
    
}