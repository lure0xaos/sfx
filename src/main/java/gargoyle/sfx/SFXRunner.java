package gargoyle.sfx;

public final class SFXRunner {
    private SFXRunner() {
    }

    public static void run(Class<? extends SFXApplication> appClass, String[] args) {
        SFXBridge.run(appClass, args);
    }

}
