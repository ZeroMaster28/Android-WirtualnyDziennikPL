package com.example.e_dziennik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.e_dziennik.Backend.FlowManager;
import com.example.e_dziennik.Backend.Persistence.User;


public class LoginActivity extends AppCompatActivity {
    FlowManager manager;

    private Button zaloguj;
    private EditText password;
    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        manager = FlowManager.getInstance();
        password = (EditText)findViewById(R.id.etpassword);
        zaloguj = (Button)findViewById(R.id.btnzaloguj);
        info = (TextView)findViewById(R.id.tvinfo);
        zaloguj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String secretCode = password.getText().toString();
                    String userRole = manager.getRoleForUser(secretCode);
                    User.logIn(secretCode);
                    if (userRole.equals("uczen")) {
                        Intent uczen = new Intent(LoginActivity.this, UczenActivity.class);
                        startActivity(uczen);

                    }
                    if (userRole.equals("nauczyciel")) {
                        Intent nauczyciel = new Intent(LoginActivity.this, NauczycielActivity.class);
                        startActivity(nauczyciel);

                    }
                    if (userRole.equals("admin")) {
                        Intent admin = new Intent(LoginActivity.this, AdminActivity.class);
                        startActivity(admin);

                    }
                }catch(Exception ex)
                {
                    InfoDialog badLogin = new InfoDialog();
                    badLogin.setBodyText(ex.getMessage());
                    badLogin.show(getSupportFragmentManager(), "login error dialog");
                }
            }
        });

    }
}
