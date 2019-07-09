package gargoyle.sfx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class SFXLoader {
    private static final Logger log = getLogger(SFXLoader.class);
    private ApplicationContext applicationContext;

    public SFXLoader(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public <C, V extends Parent> SFXComponent<C, V> loadComponent(String baseName, Class<C> controllerClass) {
        return loadComponent(baseName, applicationContext.getBean(controllerClass));
    }

    public <C, V extends Parent> SFXComponent<C, V> loadComponent(String baseName) {
        return loadComponent0(baseName, null);
    }

    public <C, V extends Parent> SFXComponent<C, V> loadComponent(String baseName, C controller) {
        return loadComponent0(baseName, controller);
    }

    private <C, V extends Parent> SFXComponent<C, V> loadComponent0(String baseName, C controller) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setClassLoader(Objects.requireNonNull(applicationContext.getClassLoader()));
            fxmlLoader.setControllerFactory(param -> applicationContext.getBean(param));
            fxmlLoader.setLocation(load(baseName, SFXConstants.FXML).getURL());
            if (controller != null) {
                fxmlLoader.setController(controller);
            }
            try {
                fxmlLoader.setResources(ResourceBundle.getBundle(baseName));
            } catch (MissingResourceException e) {
                log.info(SFXErrors.cannotAttachResources(baseName));
            }
            Parent root = fxmlLoader.load();
            find(baseName, SFXConstants.CSS).ifPresent(resource -> {
                try {
                    root.getStylesheets().add(resource.getURL().toExternalForm());
                } catch (IOException e) {
                    log.info("cannot attach stylesheet for " + baseName);
                }
            });
            return new SFXComponent<>(fxmlLoader.getController(), fxmlLoader.getRoot());
        } catch (IOException e) {
            throw new SFXException(SFXErrors.cannotLoadComponent(baseName), e);
        }
    }

    public Optional<Resource> find(String baseName, String suffix) {
        Locale locale = LocaleContextHolder.getLocale();
        ResourceBundle.Control control = ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_DEFAULT);
        List<Locale> candidateLocales = control.getCandidateLocales(baseName, locale);
        for (Locale specificLocale : candidateLocales) {
            String bundleName = control.toBundleName(baseName, specificLocale);
            String resourceName = control.toResourceName(bundleName, suffix);
            Resource resource = applicationContext.getResource(resourceName);
            if (resource.exists()) {
                return Optional.of(resource);
            }
        }
        return Optional.empty();
    }

    public Resource load(String baseName, String suffix) throws IOException {
        return find(baseName, suffix).orElseThrow(() -> new FileNotFoundException(baseName));
    }
}
