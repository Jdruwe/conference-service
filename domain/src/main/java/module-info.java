module be.xplore.conference.domain {
    exports be.xplore.conference.model;
    exports be.xplore.conference.exception;
    exports be.xplore.conference.model.auth;
    exports be.xplore.conference.model.schedule;
    exports be.xplore.conference.service;
    exports be.xplore.conference.property;
    exports be.xplore.conference.listener;

    requires lombok;
    requires java.persistence;
    requires java.validation;
    requires spring.context;
    requires spring.tx;
    requires spring.security.core;
    requires spring.data.jpa;
    requires spring.boot;
    requires java.annotation;
}
