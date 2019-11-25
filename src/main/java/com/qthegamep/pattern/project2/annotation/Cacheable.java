package com.qthegamep.pattern.project2.annotation;

import com.qthegamep.pattern.project2.model.HashAlgorithm;
import com.qthegamep.pattern.project2.model.KeyAlgorithm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Cacheable {

    KeyAlgorithm keyAlgorithm() default KeyAlgorithm.FULL_SIGNATURE_WITH_FULL_ARGUMENTS;

    HashAlgorithm hashAlgorithm() default HashAlgorithm.MD5;
}
