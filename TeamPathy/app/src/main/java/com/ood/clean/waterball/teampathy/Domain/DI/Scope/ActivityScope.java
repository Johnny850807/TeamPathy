package com.ood.clean.waterball.teampathy.Domain.DI.Scope;

/**
 * Created by User on 2017/7/17.
 */

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScope{}

