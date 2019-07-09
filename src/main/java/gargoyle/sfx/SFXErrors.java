package gargoyle.sfx;

import java.text.MessageFormat;
import java.util.ResourceBundle;

class SFXErrors {
    private static final String ERRORS = "errors";
    private static final ResourceBundle errors = ResourceBundle.getBundle(ERRORS);

    private static String error(String key) {
        return errors.getString(key);
    }

    private static String error(String key, Object... arguments) {
        return MessageFormat.format(errors.getString(key), arguments);
    }

    public static String initializationFailed() {
        return error("initialization_failed");
    }

    public static String stopFailed() {
        return error("stop_failed");
    }

    public static String primaryComponentMissing() {
        return error("primary_component_missing");
    }

    public static String startFailed() {
        return error("start_failed");
    }

    public static String cannotAttachResources(String baseName) {
        return error("cannot_attach_resources", baseName);
    }

    public static String cannotLoadComponent(String baseName) {
        return error("cannot_load_component", baseName);
    }

    public static String noStartComponent() {
        return error("no_start_component");
    }
}
