package gargoyle.sfx;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootApplication
public abstract class SFXApplication {
    private Stage stage;
    private Application fxApp;
    private ApplicationContext applicationContext;
    private SFXLoader loader;
    private SFXAlerts sfxAlerts;

    void sfxInit(Application fxApp, ApplicationContext applicationContext) {
        this.fxApp = fxApp;
        this.applicationContext = applicationContext;
        loader = applicationContext.getBean(SFXLoader.class);
        sfxAlerts = new SFXAlerts();
    }

    protected final Stage getStage() {
        return stage;
    }

    void sfxStart(Stage stage) {
        this.stage = stage;
    }

    @SuppressWarnings("RedundantThrows")
    public void onInit() throws Exception {
        //
    }

    @SuppressWarnings("RedundantThrows")
    public void onStop() throws Exception {
        //
    }

    @SuppressWarnings("RedundantThrows")
    public SFXComponent onStart() throws Exception {
        throw new SFXException(SFXErrors.noStartComponent());
    }

    protected final String getProperty(String key, String defaultValue) {
        return applicationContext.getEnvironment().getProperty(key, defaultValue);
    }

    public final <C, V extends Parent> SFXComponent<C, V> loadComponent(String baseName, Class<C> controllerClass) {
        return loader.loadComponent(baseName, controllerClass);
    }

    public final <C, V extends Parent> SFXComponent<C, V> loadComponent(String baseName) {
        return loader.loadComponent(baseName);
    }

    public final <C, V extends Parent> SFXComponent<C, V> loadComponent(String baseName, C controller) {
        return loader.loadComponent(baseName, controller);
    }

    public final Optional<Resource> find(String baseName, String suffix) {
        return loader.find(baseName, suffix);
    }

    public final Resource load(String baseName, String suffix) throws IOException {
        return loader.load(baseName, suffix);
    }

    public final void alert(String message) {
        sfxAlerts.alert(stage, message);
    }

    public final boolean confirm(String message) {
        return sfxAlerts.confirm(stage, message);
    }

    public final String prompt(String message, String defaultAnswer) {
        return sfxAlerts.prompt(stage, message, defaultAnswer);
    }

    public final void error(Exception e) {
        sfxAlerts.error(stage, e);
    }

    public final String getCodeBase() {
        return fxApp.getHostServices().getCodeBase();
    }

    public final String getDocumentBase() {
        return fxApp.getHostServices().getDocumentBase();
    }

    public final String resolveURI(String base, String rel) {
        return fxApp.getHostServices().resolveURI(base, rel);
    }

    public final void showDocument(String uri) {
        fxApp.getHostServices().showDocument(uri);
    }

    public final List<String> getRawParameters() {
        return fxApp.getParameters().getRaw();
    }

    public final List<String> getUnnamedParameters() {
        return fxApp.getParameters().getUnnamed();
    }

    public final Map<String, String> getNamedParameters() {
        return fxApp.getParameters().getNamed();
    }
}
