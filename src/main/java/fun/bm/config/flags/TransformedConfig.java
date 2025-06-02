package fun.bm.config.flags;

import fun.bm.config.DefaultTransformLogic;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
@Repeatable(TransformedConfig.List.class)
public @interface TransformedConfig {
    String name();

    String[] category() default "";

    boolean transform() default true;

    Class<? extends DefaultTransformLogic>[] transformLogic() default {DefaultTransformLogic.class};

    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        TransformedConfig[] value();
    }
}
