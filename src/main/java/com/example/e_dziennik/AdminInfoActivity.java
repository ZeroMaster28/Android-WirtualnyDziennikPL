package com.example.e_dziennik;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_dziennik.Backend.FlowManager;

public class AdminInfoActivity extends AppCompatActivity {

    TextView subjectsTableInfo;
    TextView subjectsTableView;
    TextView usersTableInfo;
    TextView usersTableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_info);

        FlowManager manager = FlowManager.getInstance();
        subjectsTableInfo = (TextView) findViewById(R.id.admin_info_subjectstable_legends);
        subjectsTableView = (TextView) findViewById(R.id.admin_info_subjectstable_rows);
        usersTableInfo = (TextView) findViewById(R.id.admin_info_userstable_legends);
        usersTableView = (TextView) findViewById(R.id.admin_info_userstable_rows);

        subjectsTableInfo.setText(manager.getTableSubjectsLegend());
        subjectsTableView.setText(manager.drawTableSubjects());

        usersTableInfo.setText(manager.getTableUsersLegend());
        usersTableView.setText(FlowManager.getInstance().drawTableUsers());


    }
}
