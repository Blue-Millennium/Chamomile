package fun.bm.config.rewritten;

import java.lang.annotation.Repeatable;

@Repeatable(MovedConfig.List.class)
public @interface MovedConfig {
    String name();

    String[] category() default "";

    boolean transform() default true;

    Class<? extends DefaultTransformLogic>[] transformLogic() default {DefaultTransformLogic.class};

    @interface List {
        MovedConfig[] value();
    }
}
