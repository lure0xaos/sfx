package gargoyle.sfx;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Controller
public @interface SFXController {
    @AliasFor(annotation = Component.class)
    String value() default "";
}