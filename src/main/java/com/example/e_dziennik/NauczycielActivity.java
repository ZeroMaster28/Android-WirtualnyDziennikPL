package com.example.e_dziennik;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.e_dziennik.Backend.FlowManager;
import com.example.e_dziennik.Backend.Persistence.Grade;
import com.example.e_dziennik.Backend.Persistence.Subject;
import com.example.e_dziennik.Backend.Persistence.User;

import java.util.Arrays;


public class NauczycielActivity extends AppCompatActivity {
    static final String WELCOME_TEXT = "Zalogowany:";
    static final String ACTIVITY_ROLE = "naucz.";
    static final String ID_PATTERN = "id-";

    FlowManager manager;

    //ogólne
    TextView textHeadline;

    //dodawanie ucznia
    Button buttonAddStudentSelectStudent;
    Button buttonAddStudentSelectSubject;
    Button buttonAssignStudent;
    TextView textStudentToAssign;
    TextView textSubjectToAssign;

    //dodawanie oceny
    Button buttonAddGradeSelectSubject;
    Button buttonAddGradeSelectStudent;
    Button buttonAddGradeSelectGrade;
    Button buttonAssignGrade;
    TextView textAddGradeStudent;
    TextView textAddGradeSubject;
    TextView textAddGradeValue;

    //okna dialogowe
    InfoDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nauczyciel);
        manager = FlowManager.getInstance();

        //dodawanie ucznia
        buttonAddStudentSelectStudent = (Button)findViewById(R.id.teacher_button_addstudent_select_student);
        buttonAddStudentSelectSubject = (Button)findViewById(R.id.teacher_button_addstudent_select_subject);
        buttonAssignStudent = (Button)findViewById(R.id.teacher_button_assign_student);
        textStudentToAssign = (TextView) findViewById(R.id.teacher_output_student_to_assign);
        textSubjectToAssign = (TextView) findViewById(R.id.teacher_output_subject_to_assign);

        //dodawanie oceny
        buttonAddGradeSelectSubject = (Button)findViewById(R.id.teacher_button_addgrade_select_subject);
        buttonAddGradeSelectStudent = (Button)findViewById(R.id.teacher_button_addgrade_select_student);
        buttonAddGradeSelectGrade = (Button)findViewById(R.id.teacher_button_addgrade_select_grade);
        buttonAssignGrade = (Button)findViewById(R.id.teacher_button_assign_grade);
        textAddGradeStudent = (TextView) findViewById(R.id.teacher_output_student_grade);
        textAddGradeSubject = (TextView) findViewById(R.id.teacher_output_subject_grade);
        textAddGradeValue = (TextView) findViewById(R.id.teacher_output_grade_value);

        //nagłówek
        textHeadline = (TextView) findViewById(R.id.teacher_headline);

        //dialog
        dialog = new InfoDialog();

        User loggedUser = manager.getUserByCode(User.getLoggedCode());
        String headlineText = WELCOME_TEXT+" "+loggedUser.getName()+" ("+ACTIVITY_ROLE+")";
        textHeadline.setText(headlineText);

        //dodawanie ucznia
        buttonAddStudentSelectStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User[] students = manager.getStudents();
                String[] options = new String[students.length];
                for(int i=0; i< students.length; i++)
                    options[i] = students[i].getSecondName()+" "+ID_PATTERN+students[i].getId();
                Arrays.sort(options);
                OptionsDialog selectStudent = new OptionsDialog("Uczeń:", options
                        ,textStudentToAssign);
                selectStudent.enableFilter();
                selectStudent.show(getSupportFragmentManager(), "select student to assign" +
                        " to subject dialog");
            }
        });
        buttonAddStudentSelectSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    User teacher = manager.getCurrentUser();
                    Subject[] subjects = manager.getSubjectsForTeacher(teacher.getId());
                    String[] options = new String[subjects.length];

                    for (int i = 0; i < subjects.length; i++)
                        options[i] = subjects[i].getName();

                    OptionsDialog selectStudent = new OptionsDialog("Przedmiot:", options
                            ,textSubjectToAssign);
                    selectStudent.show(getSupportFragmentManager(), "select student to assign" +
                            " to subject dialog");
                }catch(Exception ex)
                {
                    dialog.setBodyText("Brak przedmiotów");
                    dialog.show(getSupportFragmentManager(), "assign account error dialog");
                }
            }
        });
        buttonAssignStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String studentText = textStudentToAssign.getText()
                        .toString().replace(ID_PATTERN,"");
                String subjectText = textSubjectToAssign.getText().toString();
                if(studentText.equals("")||subjectText.equals(""))
                {
                    dialog.setBodyText("Empty credentials");
                    dialog.show(getSupportFragmentManager(), "create empty assign error dialog");
                    return;
                }
                Subject toAssign = manager.getSubjectByName(subjectText);
                int studentId = Integer.valueOf(studentText);
                String result = manager
                        .assignStudentToSubject(studentId, toAssign.getId())? "Przypisano":"Błąd";
                dialog.setBodyText(result);
                dialog.show(getSupportFragmentManager(), "assign student to subject result dialog");
            }
        });
        //dodawanie oceny
        buttonAddGradeSelectSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    User teacher = manager.getCurrentUser();
                    Subject[] subjects = manager.getSubjectsForTeacher(teacher.getId());
                    String[] options = new String[subjects.length];

                    for (int i = 0; i < subjects.length; i++)
                        options[i] = subjects[i].getName();

                    OptionsDialog selectStudent = new OptionsDialog("Przedmiot:", options
                            ,textAddGradeSubject);
                    selectStudent.show(getSupportFragmentManager(), "select student to assign" +
                            " to subject dialog");
                }catch(Exception ex)
                {
                    dialog.setBodyText("Brak przedmiotów");
                    dialog.show(getSupportFragmentManager(), "create account error dialog");
                }
            }
        });
        buttonAddGradeSelectStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = textAddGradeSubject.getText().toString();
                if(subject.equals("")){
                    dialog.setBodyText("Nie wybrano przedmiotu!");
                    dialog.show(getSupportFragmentManager(), "add grade error dialog");
                    return;
                }
                try {
                    Subject subjectInstance = manager.getSubjectByName(subject);
                    User[] students = manager.getStudentsForSubject(subjectInstance.getId());
                    String[] options = new String[students.length];
                    for (int i = 0; i < students.length; i++)
                        options[i] = students[i].getSecondName() +" "+ ID_PATTERN + students[i].getId();
                    Arrays.sort(options);
                    OptionsDialog selectStudent = new OptionsDialog("Uczeń:", options
                            , textAddGradeStudent);
                    selectStudent.enableFilter();
                    selectStudent.show(getSupportFragmentManager(), "select student to assign" +
                            " to subject dialog");
                }catch(Exception ex)
                {
                    dialog.setBodyText("Brak zapisanych uczniów!");
                    dialog.show(getSupportFragmentManager(), "add grade error dialog");
                }
            }
        });
        buttonAddGradeSelectGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] grades = new String[Grade.AVAILABLE_GRADES.size()];
                int i=0;
                for(String gradeValue: Grade.AVAILABLE_GRADES) {
                    grades[i] = gradeValue;
                    i++;
                }
                Arrays.sort(grades);
                OptionsDialog gradeOptions = new OptionsDialog("Ocena:", grades, textAddGradeValue);
                gradeOptions.show(getSupportFragmentManager(), "select grade value to add");
            }
        });
        buttonAssignGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int studentId = Integer.valueOf(textAddGradeStudent.getText().toString()
                            .replace(ID_PATTERN, ""));
                    int subjectId = manager.getSubjectByName(textAddGradeSubject
                            .getText().toString()).getId();
                    int teacherId = manager.getCurrentUser().getId();
                    manager.addGrade(studentId, teacherId, subjectId, textAddGradeValue.getText().toString());
                    dialog.setBodyText("Dodano ocenę!");
                    dialog.show(getSupportFragmentManager(), "add grade fine dialog");
                }catch (Exception ex)
                {
                    dialog.setStandardError();
                    dialog.show(getSupportFragmentManager(), "add grade error dialog");
                }
            }
        });
    }
}
