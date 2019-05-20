module be.xplore.conference.consumer {
    requires be.xplore.conference.domain;

    requires spring.boot;
    requires spring.context;
    requires slf4j.api;
    requires java.annotation;
    requires lombok;
    requires com.fasterxml.jackson.databind;
    requires spring.web;
}
