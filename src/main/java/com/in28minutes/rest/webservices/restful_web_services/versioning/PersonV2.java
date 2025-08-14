package com.in28minutes.rest.webservices.restful_web_services.versioning;

public class PersonV2 {

    @Override
    public String toString() {
        return "PersonV2{" +
                "name=" + name +
                '}';
    }

    private Name name;

    public Name getName() {
        return name;
    }

    public PersonV2(Name name) {
        super();
        this.name = name;
    }


}
