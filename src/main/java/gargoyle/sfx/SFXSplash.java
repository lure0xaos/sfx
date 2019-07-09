package gargoyle.sfx;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Objects;

public final class SFXSplash {
    private final JWindow window;

    private SFXSplash(URL location) {
        window = new JWindow();
        Container pane = window.getContentPane();
        pane.setLayout(new BorderLayout());
        JLabel label = new JLabel(new ImageIcon(location));
        window.setBackground(new Color(0, 0, 0, 1));
        pane.add(label, BorderLayout.CENTER);
        window.setAlwaysOnTop(true);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.toFront();
    }

    public static SFXSplash start(URL location) {
        return new SFXSplash(Objects.requireNonNull(location));
    }

    public void close() {
        if (window.isVisible()) {
            window.dispose();
        }
    }
}
