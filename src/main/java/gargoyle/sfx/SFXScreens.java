package gargoyle.sfx;

import javafx.stage.Stage;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

public class SFXScreens {
    private static final Logger log = getLogger(SFXScreens.class);
    private final Map<String, SFXComponent> screens;
    private final SFXLoader loader;
    private final Stage stage;

    private String currentName;

    public SFXScreens(SFXLoader loader, Stage stage) {
        this.stage = stage;
        screens = new HashMap<>();
        this.loader = loader;
    }

    public String getCurrentName() {
        return currentName;
    }

    public SFXComponent getCurrentComponent() {
        return screens.get(currentName);
    }

    public void addScreen(String name, SFXComponent component) {
        if (!screens.containsKey(name)) {
            screens.put(name, component);
        }
    }

    public <C> void addScreen(String baseName, Class<C> controllerClass) {
        addScreen(baseName, loader.loadComponent(baseName, controllerClass));
    }

    public void addScreen(String baseName) {
        addScreen(baseName, loader.loadComponent(baseName));
    }

    public <C> void addScreen(String baseName, C controller) {
        addScreen(baseName, loader.loadComponent(baseName, controller));
    }

    public void toScreen(String name) {
        if (!Objects.equals(currentName, name) && screens.containsKey(name)) {
            SFXComponent currentComponent = getCurrentComponent();
            if (currentComponent != null) {
                Object controller = currentComponent.getController();
                if (controller instanceof SFXScreen) {
                    ((SFXScreen) controller).onHide();
                }
            }
            SFXComponent component = screens.get(name);
            currentName = name;
            stage.setScene(SFXUtil.sceneFor(component.getView()));
            Object controller = component.getController();

            if (controller != null) {
                try {
                    Method initializer = controller.getClass().getDeclaredMethod("initialize");
                    try {
                        initializer.invoke(controller);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.error("exception in initializer", e);
                    }
                } catch (NoSuchMethodException ignored) {
                }
            }
            if (controller instanceof SFXScreen) {
                ((SFXScreen) controller).onShow();
            }
        }
    }
}
