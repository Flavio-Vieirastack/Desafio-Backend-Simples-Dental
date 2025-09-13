package com.simplesdental.product.annotations;

import org.springframework.aot.hint.annotation.Reflective;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Reflective
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public @interface TransactionalReadOnly {
}