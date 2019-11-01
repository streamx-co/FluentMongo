package co.streamx.fluent.mongo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public interface BasicTypes {
    @Data
    public class Person {

        private Long id;
        private String name;
        private int born;

        private List<Movie> actedMovies = new ArrayList<>();

        private List<Movie> directedMovies = new ArrayList<>();

        private List<Movie> wroteMovies = new ArrayList<>();
    }

    @Data
    public class Movie {

        private Long id;
        private String title;
        private int released;
        private String tagline;

        private List<Role> roles;

        private Person director;
    }

    @Setter
    @Getter
    public class Role extends Entity {

        private Long id;
        private List<String> roles = new ArrayList<>();

        private Person person;

        private Movie movie;
    }

    @Setter
    @Getter
    class Department extends Entity {
        private String name;

        private Set<Subject> subjects;
    }

    @Setter
    @Getter
    class Subject extends Entity {
        private String name;

        private Department department;

        private Set<Teacher> teachers;

        private Set<Course> courses;
    }

    @Setter
    @Getter
    class Teacher extends Entity {
        private String name;

        private Set<Course> courses;

        private Set<Subject> subjects;
    }

    @Setter
    @Getter
    class Course extends Entity {
        private String name;

        private Subject subject;

        private Teacher teacher;

        private Set<Enrollment> enrollments = new HashSet<>();
    }

    @Setter
    @Getter
    class Student extends Entity {
        private String name;

        private Set<Enrollment> enrollments;

        private Set<StudyBuddy> studyBuddies;
    }

    @Setter
    @Getter
    class Enrollment extends Entity {

        private Student student;

        private Course course;

        private Date enrolledDate;

    }

    @Setter
    @Getter
    class StudyBuddy extends Entity {
        private Set<Student> students;
    }

    @Setter
    @Getter
    abstract class Entity {

        private Long id;

    }
}
