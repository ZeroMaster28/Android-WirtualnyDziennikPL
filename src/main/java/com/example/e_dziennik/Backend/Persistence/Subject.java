package com.example.e_dziennik.Backend.Persistence;

/**Obiekty odpowiadające wpisom w bazie dla przedmiotów*/
public class Subject {
    public static final String TABLE_NAME = "SUBJECTS";

    public static final String COL_ID = "ID";
    private int id;

    public static final String COL_NAME = "NAME";
    private String name;

    public static final String COL_TEACHER = "TEACHER_ID";
    private int teacher;

    public Subject(){};

    public Subject(int id, String name, int teacher) {
        this.id = id;
        this.name = name;
        this.teacher = teacher;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTeacher() {
        return teacher;
    }

    public void setTeacher(int teacher) {
        this.teacher = teacher;
    }
}
