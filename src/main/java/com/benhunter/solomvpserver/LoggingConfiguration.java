package com.benhunter.solomvpserver;

import org.slf4j.*;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.*;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Field;

import static java.util.Optional.*;

@Configuration
public class LoggingConfiguration {
//    @Bean
//    @Scope("prototype")
//    public Logger logger(final InjectionPoint ip) {
//        return LoggerFactory.getLogger(of(ip.getMethodParameter())
//                .<Class>map(MethodParameter::getContainingClass)
//                .orElseGet( () ->
//                        ofNullable(ip.getField())
//                                .map(Field::getDeclaringClass)
//                                .orElseThrow (IllegalArgumentException::new)
//                )
//        );
//    }
//}

    @Bean
    @Scope("prototype") // means create new instance on every injection
    public Logger logger(final InjectionPoint ip) {
        final Class lClass;
        if (null != ip.getMethodParameter()) {
            lClass = ip.getMethodParameter().getContainingClass();
        } else {
            lClass = ip.getField().getDeclaringClass();
        }
        return LoggerFactory.getLogger(lClass);
    }
}