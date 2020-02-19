package com.example.e_dziennik.Backend.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "eDziennikDatabase";

    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /** Statyczne zapytania gdy baza jest pierwszy raz tworzona*/
    protected static class StaticQueries{
        private StaticQueries(){};
        static final String CREATE_USERS =
                "CREATE TABLE USERS(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "CODE VARCHAR(20) UNIQUE," +
                "NAME VARCHAR(20)," +
                "SECOND_NAME VARCHAR(20) NOT NULL," +
                "ROLE VARCHAR(20)," +
                "DESCRIPTION VARCHAR(100))";

        static final String CREATE_SUBJECTS =
                "CREATE TABLE SUBJECTS("+
                "ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "NAME VARCHAR(20) UNIQUE,"+
                "TEACHER_ID INTEGER,"+
                "FOREIGN KEY (TEACHER_ID) REFERENCES USERS(ID))";

        static final String CREATE_STUDENT_SUBJECT =
                "CREATE TABLE STUDENT_SUBJECT("+
                "ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "STUDENT_ID INTEGER,"+
                "SUBJECT_ID INTEGER,"+
                "FOREIGN KEY (STUDENT_ID) REFERENCES USERS(ID))";

        static final String CREATE_GRADES =
                "CREATE TABLE GRADES(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "STUDENT_SUBJECT INTEGER," +
                "TEACHER_ID INTEGER," +
                "ADDED VARCHAR(20) NOT NULL," +
                "MODIFIED VARCHAR(20)," +
                "GRADE VARCHAR(1) NOT NULL," +
                "DESCRIPTION VARCHAR(50)," +
                "FOREIGN KEY (STUDENT_SUBJECT) REFERENCES STUDENT_SUBJECT(ID)," +
                "FOREIGN KEY (TEACHER_ID) REFERENCES USERS(ID))";

        static final String[] CREATE_FABRIC_ACCOUNTS={
                "INSERT INTO USERS(SECOND_NAME,ROLE,CODE) "+
                        "VALUES(\'empty\',\'admin\',\'admin\')",
                "INSERT INTO USERS(SECOND_NAME,ROLE,CODE) "+
                        "VALUES(\'empty\',\'nauczyciel\',\'nauczyciel\')",
                "INSERT INTO USERS(SECOND_NAME,ROLE,CODE) "+
                        "VALUES(\'empty\',\'uczen\',\'uczen\')"};

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //tworzenie niezbędnych tabel
        db.execSQL(StaticQueries.CREATE_USERS);
        db.execSQL(StaticQueries.CREATE_SUBJECTS);
        db.execSQL(StaticQueries.CREATE_STUDENT_SUBJECT);
        db.execSQL(StaticQueries.CREATE_GRADES);

        //tworzenie kont fabrycznych
        db.execSQL(StaticQueries.CREATE_FABRIC_ACCOUNTS[0]);
        db.execSQL(StaticQueries.CREATE_FABRIC_ACCOUNTS[1]);
        db.execSQL(StaticQueries.CREATE_FABRIC_ACCOUNTS[2]);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS USERS");
        db.execSQL("DROP TABLE IF EXISTS SUBJECTS");
        onCreate(db);
    }


    protected boolean deleteFromTable(String tableName, String condition)
    {
        boolean deleteSuccessful = false;

        SQLiteDatabase db = this.getWritableDatabase();
        deleteSuccessful = db.delete(tableName, condition, null) > 0;
        db.close();

        return deleteSuccessful;
    }

    protected int getNumberOfRows(String tableName)
    {
       return getNumberOfRows(tableName, null);
    }

    protected int getNumberOfRows(String tableName, String condition)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = condition==null||"".equals(condition) ? "":" WHERE " + condition;
        String sql = "SELECT * FROM "+tableName + where;
        int recordCount = db.rawQuery(sql, null).getCount();
        db.close();

        return recordCount;
    }

}
