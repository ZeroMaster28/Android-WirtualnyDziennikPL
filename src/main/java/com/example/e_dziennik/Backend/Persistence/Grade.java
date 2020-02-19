package com.example.e_dziennik.Backend.Persistence;


import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**Obiekty ocen odpowiadające wpisom w bazie*/
public class Grade {
    public static final String TABLE_NAME = "GRADES";

    public static String COL_ID = "ID";
    private int id;

    public static String COL_ADDED= "ADDED";
    private String added;

    public static String COL_MODIFIED = "MODIFIED";
    private String modified;

    public static String COL_DESC = "DESCRIPTION";
    private String description;

    public static String COL_SUBJ= "STUDENT_SUBJECT";
    private int studentSubject;

    public static String COL_TEACHER = "TEACHER_ID";
    private int teacherId;

    public static String COL_GRADE = "GRADE";
    private String grade;

    public static final List<String> AVAILABLE_GRADES = Arrays.asList("1","2","3","4","5","6");

    public Grade() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdded() {
        return added;
    }

    public void setAdded(String added) {
        this.added = added;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStudentSubject() {
        return studentSubject;
    }

    public void setStudentSubject(int studentSubject) {
        this.studentSubject = studentSubject;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public static boolean validateGrade(Grade grade)
    {
        if(grade.getAdded()==null) return false;
        if(grade.getGrade()==null || !validateGradeValue(grade.getGrade()))
            return false;
        return true;
    }

    public static boolean validateGradeValue(String gradeValue)
    {
        return AVAILABLE_GRADES.contains(gradeValue);
    }

}
