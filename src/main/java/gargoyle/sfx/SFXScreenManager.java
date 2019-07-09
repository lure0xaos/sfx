package gargoyle.sfx;

import javafx.stage.Stage;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SFXScreenManager {

    private final SFXLoader loader;
    private final Map<Stage, SFXScreens> screensMap = new HashMap<>();

    public SFXScreenManager(SFXLoader loader) {
        this.loader = loader;
    }

    public SFXScreens forStage(Stage stage) {
        return screensMap.computeIfAbsent(stage, (stage1) -> new SFXScreens(loader, stage1));
    }

    public SFXScreens forStage(Stage stage, SFXComponent startComponent) {
        SFXScreens screens = forStage(stage);
        String name = "start";
        screens.addScreen(name, startComponent);
        screens.toScreen(name);
        return screens;
    }
}
