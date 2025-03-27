package com.strikezone.strikezone_backend.global.config.replica;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReadOnlyConnection {
}
