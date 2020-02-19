package com.example.e_dziennik.Backend.Persistence;

/**Obiekty odpowiadające wpisom w bazie dla przedmiotów
 * łączonych z użytkownikami*/
public class StudentSubject {
    public static final String TABLE_NAME = "STUDENT_SUBJECT";

    public static final String COL_ID = "ID";
    private int id;

    public static final String COL_STUDENT = "STUDENT_ID";
    private int studentId;

    public static final String COL_SUBJECT = "SUBJECT_ID";
    private int subjectId;

    public StudentSubject() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }
}

