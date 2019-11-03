package co.streamx.fluent.mongo;

import java.util.Date;
import java.util.List;

import lombok.Data;

public interface TutorialTypes {
    @Data
    class Restaurant {
        private String name;
        private int stars;
        private List<String> categories;
        private Contact contact;
        private Date lastModified;

        private List<Integer> results;
    }

    @Data
    class Contact {
        private String phone;
    }
}
