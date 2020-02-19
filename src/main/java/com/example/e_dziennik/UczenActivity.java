package com.example.e_dziennik;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.e_dziennik.Backend.FlowManager;
import com.example.e_dziennik.Backend.Persistence.Grade;
import com.example.e_dziennik.Backend.Persistence.Subject;
import com.example.e_dziennik.Backend.Persistence.User;


public class UczenActivity extends AppCompatActivity {
    static final String WELCOME_TEXT = "Zalogowany:";
    static final String ACTIVITY_ROLE = "uczeń";

    FlowManager manager;

    //ogólne
    TextView textHeadline;

    //oceny
    TextView textSubject;
    TextView textGrades;
    Button buttonSelectSubject;
    Button buttonViewGrade;

    //okienko dialogowe
    InfoDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uczen);
        manager = FlowManager.getInstance();

        buttonSelectSubject = (Button) findViewById(R.id.student_button_select_subject);
        buttonViewGrade = (Button) findViewById(R.id.student_button_view_grade);
        textSubject = (TextView) findViewById(R.id.student_output_subject);
        textGrades = (TextView) findViewById(R.id.student_output_grades);

        textHeadline = (TextView) findViewById(R.id.student_headline);

        User loggedUser = manager.getUserByCode(User.getLoggedCode());
        String headlineText = WELCOME_TEXT+" "+loggedUser.getName()+" ("+ACTIVITY_ROLE+")";
        textHeadline.setText(headlineText);
        dialog = new InfoDialog();

        buttonSelectSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    User student = manager.getCurrentUser();
                    Subject[] subjects = manager.getSubjectsForStudent(student.getId());

                    String[] options = new String[subjects.length];
                    for (int i = 0; i < subjects.length; i++)
                        options[i] = subjects[i].getName();

                    OptionsDialog selectStudent = new OptionsDialog("Przedmiot:", options
                            ,textSubject);
                    selectStudent.show(getSupportFragmentManager(), "select student to assign" +
                            " to subject dialog");
                }catch(Exception ex)
                {
                    dialog.setBodyText("Brak przedmiotów");
                    dialog.show(getSupportFragmentManager(), "select subject error dialog");
                }
            }
        });
        buttonViewGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subjectName = textSubject.getText().toString();
                if(subjectName.equals("")){
                    dialog.setBodyText("Nie wybrano przedmiotu!");
                    dialog.show(getSupportFragmentManager(), "show grades error dialog");
                    return;
                }
                try {
                    User student = manager.getCurrentUser();
                    Subject subject = manager.getSubjectByName(subjectName);
                    Grade[] grades = manager.getGradesForStudent(student.getId(), subject.getId());
                    textGrades.setText(manager.drawGrades(grades));
                }catch(Exception ex)
                {
                    dialog.setBodyText("Brak ocen!");
                    dialog.show(getSupportFragmentManager(), "show grades error dialog");
                }
            }
        });

    }
}
