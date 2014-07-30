package com.smartboxtv.movistartv.services;

/**
 * Created by Esteban- on 18-04-14.
 */
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DataMember {
    String member ();
}
