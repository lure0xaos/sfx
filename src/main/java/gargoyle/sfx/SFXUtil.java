package gargoyle.sfx;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class SFXUtil {
    private SFXUtil() {
    }

    public static Scene sceneFor(Parent node) {
        Scene scene = node.getScene();
        return scene == null ? new Scene(node) : scene;
    }

    public static Stage stageFor(Node node) {
        Scene scene = node.getScene();
        return scene == null ? null : (Stage) scene.getWindow();
    }
}
