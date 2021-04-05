package br.com.zup.conf.excecoes

import io.micronaut.aop.Around

@Around
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class ErrorHandler()
