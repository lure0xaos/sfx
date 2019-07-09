package gargoyle.sfx;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;

import javax.swing.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public abstract class SFXTest {
    @BeforeAll
    public static void initFX() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(() -> {
            new JFXPanel();
            latch.countDown();
        });
        if (!latch.await(10L, TimeUnit.SECONDS))
            throw new ExceptionInInitializerError();
    }


    @SuppressWarnings("unchecked")
    protected final <V> V testFX(Callable<V> task) {
        final CountDownLatch latch = new CountDownLatch(1);
        final Object[] holder = new Object[2];
        Platform.runLater(() -> {
            try {
                holder[0] = task.call();
            } catch (Throwable e) {
                holder[1] = e;
            } finally {
                latch.countDown();
            }
        });
        try {
            if (!latch.await(10L, TimeUnit.SECONDS))
                throw new InterruptedException();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Object exception = holder[1];
        if (exception != null) {
            if (exception instanceof RuntimeException)
                throw (RuntimeException) exception;
            else
                throw new RuntimeException((Throwable) exception);
        }
        return (V) holder[0];
    }

    protected final void testFX(Runnable task) {
        testFX((Callable<Void>) () -> {
            task.run();
            return null;
        });
    }

    protected final Stage createStage() {
        Scene scene = new Scene(new Pane());
        Stage stage = new Stage();
        stage.setScene(scene);
        return stage;
    }
}