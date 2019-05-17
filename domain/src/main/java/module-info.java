module be.xplore.conference.domain {
    exports be.xplore.conference.model;
    exports be.xplore.conference.exception;

    requires lombok;
    requires java.persistence;
    requires org.hibernate.commons.annotations;
}
