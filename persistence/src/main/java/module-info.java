module be.xplore.conference.persistence {
    exports be.xplore.conference.persistence;
    requires be.xplore.conference.domain;

    requires spring.tx;
    requires spring.data.jpa;
    requires spring.context;
    requires java.annotation;
}
