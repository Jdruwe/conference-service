module be.xplore.conference.rest.api {
    requires be.xplore.conference.domain;

    requires spring.boot;
    requires spring.web;
    requires spring.context;
    requires jackson.annotations;
    requires spring.tx;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires modelmapper;
    requires lombok;
    requires java.annotation;
    requires spring.beans;
    requires spring.security.web;
    requires tomcat.embed.core;
    requires spring.security.core;
    requires spring.core;
    requires slf4j.api;
    requires jjwt;
    requires spring.webmvc;
    requires spring.security.config;
}
