package fun.bm.config.rewritten;

public @interface ConfigInfo {
    String name();

    String comment() default "";
}
