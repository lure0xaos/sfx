package gargoyle.sfx;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SFXAlertsTest extends SFXTest {

    private SFXAlerts alerts;

    @BeforeEach
    void setUp() {
        alerts = new SFXAlerts();
    }

    @Test
    void alert() {
        testFX(() -> alerts.alert(createStage(), "msg"));
    }

    @Test
    void confirm() {
    }

    @Test
    void prompt() {
    }

    @Test
    void error() {
    }
}