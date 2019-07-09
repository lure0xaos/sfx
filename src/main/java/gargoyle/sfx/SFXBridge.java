package gargoyle.sfx;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static gargoyle.sfx.SFXConstants.*;
import static org.slf4j.LoggerFactory.getLogger;

public final class SFXBridge extends Application {
    private static final Logger log = getLogger(SFXBridge.class);
    private static SFXSplash splash;
    private SFXApplication application;
    private ConfigurableApplicationContext applicationContext;
    private Class<? extends SFXApplication> appClass;

    static void run(Class<? extends SFXApplication> appClass, String[] args) {
        splash = SFXSplash.start(SFXRunner.class.getResource(System.getProperty(SFX_SPLASH, SPLASH)));
        String[] newArgs = new String[args.length + 1];
        newArgs[0] = appClass.getName();
        System.arraycopy(args, 0, newArgs, 1, args.length);
        launch(SFXBridge.class, newArgs);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init() {
        try {
            List<String> parameters = getParameters().getRaw();
            String appClassName = parameters.get(0);
            List<String> paramsList = parameters.subList(1, parameters.size());
            appClass = (Class<? extends SFXApplication>) Class.forName(appClassName);
            applicationContext = new SpringApplicationBuilder()
                    .web(WebApplicationType.NONE)
                    .sources(appClass)
                    .run(paramsList.toArray(new String[0]));
            application = applicationContext.getBean(appClass);
            application.sfxInit(this, applicationContext);
            application.onInit();
        } catch (Exception e) {
            splash.close();
            throw new SFXException(SFXErrors.initializationFailed(), e);
        }
    }

    @Override
    public void stop() {
        try {
            application.onStop();
        } catch (Exception e) {
            throw new SFXException(SFXErrors.stopFailed(), e);
        } finally {
            splash.close();
            applicationContext.close();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            application.sfxStart(primaryStage);
            SFXComponent startComponent = Objects.requireNonNull(application.onStart(), SFXErrors.primaryComponentMissing());
            String applicationName = getProperty(APPLICATION_NAME, DEFAULT_APPLICATION_NAME);
            primaryStage.setTitle(getProperty(APPLICATION_NAME, appClass.getSimpleName()));
            applicationContext.getBean(SFXScreenManager.class).forStage(primaryStage, startComponent);
            application.find(applicationName, PNG)
                    .or(() -> application.find(applicationName, GIF))
                    .or(() -> application.find(applicationName, JPG))
                    .ifPresent(resource -> {
                        try {
                            primaryStage.getIcons().add(new Image(resource.getURL().toExternalForm()));
                        } catch (IOException e) {
                            log.info("no icon " + applicationName + "." + PNG);
                        }
                    });
            primaryStage.centerOnScreen();
            primaryStage.show();
        } catch (Exception e) {
            applicationContext.close();
            throw new SFXException(SFXErrors.startFailed(), e);
        } finally {
            splash.close();
        }
    }

    private String getProperty(String key, String defaultValue) {
        return applicationContext.getEnvironment().getProperty(key, defaultValue);
    }
}
