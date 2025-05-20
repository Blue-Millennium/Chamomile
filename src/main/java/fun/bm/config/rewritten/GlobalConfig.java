package fun.bm.config.rewritten;

public @interface GlobalConfig {
    String name();

    String comment() default "";
}
