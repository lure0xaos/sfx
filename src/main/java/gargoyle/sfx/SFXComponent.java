package gargoyle.sfx;

import javafx.scene.Parent;

public final class SFXComponent<C, V extends Parent> {
    private final C controller;
    private final V view;

    public SFXComponent(C controller, V view) {
        this.controller = controller;
        this.view = view;
    }

    public C getController() {
        return controller;
    }

    public V getView() {
        return view;
    }
}
