package com.example.e_dziennik.Backend.Database.TableControllers;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.e_dziennik.Backend.Database.DbHandler;
import com.example.e_dziennik.Backend.Database.QueryBuilder;
import com.example.e_dziennik.Backend.Persistence.StudentSubject;

/**Kontroler do wykonywania działań na tabeli <code>STUDENT_SUBJECT</code>*
 * @author Michał
 */
public class TableStudentSubject extends DbHandler {
    public TableStudentSubject(Context context) {
        super(context);
    }
    public boolean createRow(StudentSubject studentSubject) {

        ContentValues values = new ContentValues();

        values.put(StudentSubject.COL_STUDENT, studentSubject.getStudentId());
        values.put(StudentSubject.COL_SUBJECT, studentSubject.getSubjectId());

        SQLiteDatabase db = this.getWritableDatabase();
        boolean createSuccessful = db.insert(StudentSubject.TABLE_NAME, null, values) > 0;
        db.close();

        return createSuccessful;
    }

    public StudentSubject[] getRow(String whereCondition)
    {

        int numberOfResults = getNumberOfRows(StudentSubject.TABLE_NAME, whereCondition);
        StudentSubject[] pairs = new StudentSubject[numberOfResults];

        SQLiteDatabase db = getWritableDatabase();
        String query = QueryBuilder.buildSelectQuery(StudentSubject.TABLE_NAME, whereCondition, "*");
        Cursor cursor = db.rawQuery(query, null);

        int i = 0;
        if(cursor.moveToFirst()) {
            do {
                pairs[i] = new StudentSubject();
                pairs[i].setId(cursor.getInt(cursor.getColumnIndex(StudentSubject.COL_ID)));
                pairs[i].setStudentId(cursor.getInt(cursor.getColumnIndex(StudentSubject.COL_STUDENT)));
                pairs[i].setSubjectId(cursor.getInt(cursor.getColumnIndex(StudentSubject.COL_SUBJECT)));
                i++;
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return pairs;
    }

    public boolean deleteRow(String whereCondition)
    {
        return super.deleteFromTable(StudentSubject.TABLE_NAME,whereCondition);
    }

    public int getNumberOfRows()
    {
        return super.getNumberOfRows(StudentSubject.TABLE_NAME);
    }


    public boolean updateRow(StudentSubject subject) {

        ContentValues values = new ContentValues();

        values.put(StudentSubject.COL_STUDENT, subject.getStudentId());
        values.put(StudentSubject.COL_SUBJECT, subject.getSubjectId());

        String where = "id = ?";

        String[] whereArgs = { Integer.toString(subject.getId()) };

        SQLiteDatabase db = this.getWritableDatabase();

        boolean updateSuccessful = db.update(StudentSubject.TABLE_NAME, values, where, whereArgs) > 0;
        db.close();

        return updateSuccessful;
    }
}