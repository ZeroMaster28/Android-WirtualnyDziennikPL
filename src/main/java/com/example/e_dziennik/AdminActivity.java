package com.example.e_dziennik;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.e_dziennik.Backend.FlowManager;
import com.example.e_dziennik.Backend.Persistence.Subject;
import com.example.e_dziennik.Backend.Persistence.User;

import java.util.Arrays;


public class AdminActivity extends AppCompatActivity {

    static final String WELCOME_TEXT = "Zalogowany:";
    static final String ACTIVITY_ROLE = "admin";

    FlowManager manager;

    //ogólne
    TextView textHeadline;

    //zarządzanie kontami
    Button buttonCreateAccount;
    Button buttonRemoveAccount;
    EditText textName;
    EditText textSecondName;
    Button buttonSelectRole;
    TextView textCodeResult;
    EditText textCodeToDelete;
    TextView textSelectedRole;

    //zarządzanie przedmiotami
    Button buttonCreateSubject;
    Button buttonRemoveSubject;
    Button buttonAssignTeacher;
    EditText textSubjectName;
    TextView textTeacherToAssign;
    Button buttonSelectTeacher;
    Button findSubject;

    //db info
    Button buttonDbInfo;

    //dialogs
    InfoDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        manager = FlowManager.getInstance();

        dialog = new InfoDialog();
        textHeadline = (TextView) findViewById(R.id.admin_headline);
        buttonCreateAccount = (Button) findViewById(R.id.admin_button_create);
        textName = (EditText) findViewById(R.id.admin_input_name);
        textSecondName = (EditText) findViewById(R.id.admin_input_sec_name);
        textCodeResult = (TextView) findViewById(R.id.admin_output_code);
        buttonRemoveAccount = (Button) findViewById(R.id.admin_button_remove_account);
        buttonCreateSubject = (Button) findViewById(R.id.admin_button_create_subject);
        buttonRemoveSubject = (Button) findViewById(R.id.admin_button_remove_subject);
        textSubjectName = (EditText) findViewById(R.id.admin_subject_input);
        buttonAssignTeacher = (Button) findViewById(R.id.admin_button_assign_teacher);
        textCodeToDelete = (EditText) findViewById(R.id.admin_code_to_remove);
        textTeacherToAssign = (TextView) findViewById(R.id.admin_output_teacher_to_assign);
        buttonDbInfo = (Button) findViewById(R.id.admin_button_dbinfo);
        buttonSelectRole = (Button) findViewById(R.id.admin_button_select_role);
        textSelectedRole = (TextView) findViewById(R.id.admin_selected_role);
        buttonSelectTeacher = (Button) findViewById(R.id.admin_button_select_teacher);
        findSubject = (Button) findViewById(R.id.admin_button_find_subject);

        User loggedUser = manager.getUserByCode(User.getLoggedCode());
        String headlineText = WELCOME_TEXT+" "+loggedUser.getName()+" "+ACTIVITY_ROLE;
        textHeadline.setText(headlineText);

        findSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Subject[] subjects = manager.getSubjects();
                String[] subjectNames = new String[subjects.length];
                for(int i=0; i<subjectNames.length; i++)
                    subjectNames[i] = subjects[i].getName();
                Arrays.sort(subjectNames);
                OptionsDialog selectSubject = new OptionsDialog("Przedmiot:", subjectNames,
                        textSubjectName);
                selectSubject.show(getSupportFragmentManager(), "select subject dialog");
            }
        });
        buttonSelectTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User[] teachers = manager.getTeachers();
                String[] options = new String[teachers.length];
                for(int i=0; i< teachers.length; i++)
                    options[i] = teachers[i].getSecondName()+"  "+teachers[i].getCode();
                Arrays.sort(options);
                OptionsDialog selectTeacher = new OptionsDialog("Nauczyciel:", options
                        ,textTeacherToAssign);
                selectTeacher.enableFilter();
                selectTeacher.show(getSupportFragmentManager(), "select teacher to assign dialog");
            }
        });
        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String code = manager.createAccount(textName.getText().toString(),
                            textSecondName.getText().toString(),
                            textSelectedRole.getText().toString());
                    textCodeResult.setText(code);
                    textName.setText("");
                    textSecondName.setText("");
                }catch (Exception ex)
                {
                    dialog.setBodyText(ex.getMessage());
                    dialog.show(getSupportFragmentManager(), "create account error dialog");
                }
            }
        });
        buttonRemoveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(manager.removeAccount(textCodeToDelete.getText().toString())) {
                    dialog.setBodyText("Usunięto");
                    textCodeToDelete.setText("");
                }else{
                    dialog.setStandardError();
                }
                dialog.show(getSupportFragmentManager(), "remove account dialog");
            }
        });
        buttonCreateSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textSubjectName.getText().toString().equals(""))
                    dialog.setBodyText("Pusta nazwa!");
                else if(manager.createSubject(textSubjectName.getText().toString()))
                {
                    textSubjectName.setText("");
                    dialog.setBodyText("Stworzono");
                }else{
                    dialog.setStandardError();
                }
                dialog.show(getSupportFragmentManager(), "create subject dialog");
            }
        });
        buttonRemoveSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(manager.removeSubject(textSubjectName.getText().toString())){
                   textSubjectName.setText("");
                   dialog.setBodyText("Usunięto");
               }else{
                   dialog.setStandardError();
               }
               dialog.show(getSupportFragmentManager(), "remove subject dialog");
            }
        });
        buttonAssignTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String teacherCode = textTeacherToAssign.getText().toString();
                    int teacherId = manager.getUserByCode(teacherCode).getId();
                    if(manager.assignTeacherToSubject(teacherId, textSubjectName.getText().toString()))
                    {
                        dialog.setBodyText("Przypisano");
                        textTeacherToAssign.setText("");
                        textSubjectName.setText("");
                    }else dialog.setBodyText("Błąd przypisania");
                }catch(Exception ex)
                {
                   dialog.setBodyText("Error");
                }finally {
                    dialog.show(getSupportFragmentManager(), "assign subject dialog");
                }
            }
        });
        buttonDbInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dbInfo = new Intent(AdminActivity.this, AdminInfoActivity.class);
                startActivity(dbInfo);
            }
        });
        buttonSelectRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OptionsDialog selectRole = new OptionsDialog("Rola:", User.ROLES, textSelectedRole);
                selectRole.show(getSupportFragmentManager(), "select role dialog");
            }
        });

    }
}
