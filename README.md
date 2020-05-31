# Splash Screen Mod - Fabric

```java
// Use CustomSplashScreenManager.register to register your CustomSplashScreen.
// CustomSplashScreenManager.register(Identifier id, int numberOfFrames, Sound s);
// CustomSplashScreenManager.register(Identifier id, int numberOfFrames, null);
// CustomSplashScreenManager.register(Identifier id, int numberOfFrames);
CustomSplashScreenManager.register(
    // create an identifier for the first image.
    // currently there must only be one "_0" in the path part of the identifier.
    // as all following images are computed based on this.
    new Identifier(
        "splashscreen",
        "textures/gui/splash/mojang/mp4/splash_0.png"
    ),
    // the number of frames that the splash screen can go through, this means that there are 0-250 inclusive frames named splash_0, splash_1 and so forth.
    251,
    // add a sound that will be played on the first render of the splash screen
    // **WARNING** : minecraft reloads assets and has to recreate the sound engine, so its quite possible that the sound will be delayed and choppy
    new Sound(
        "splashscreen:mojang_splash",
        1.0F,
        1.0F,
        1,
        Sound.RegistrationType.FILE,
        true,
        true,
        16
    )
);
```
