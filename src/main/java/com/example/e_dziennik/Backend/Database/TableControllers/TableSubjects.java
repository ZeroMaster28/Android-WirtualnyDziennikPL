package com.example.e_dziennik.Backend.Database.TableControllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.e_dziennik.Backend.Database.DbHandler;
import com.example.e_dziennik.Backend.Database.QueryBuilder;
import com.example.e_dziennik.Backend.Persistence.Subject;

/**Kontroler do wykonywania działań na tabeli <code>USERS</code>*
 * @author Michał
 */
public class TableSubjects extends DbHandler {
    public TableSubjects(Context context) {
        super(context);
    }
    public boolean createRow(Subject subject) {

        ContentValues values = new ContentValues();

        values.put(Subject.COL_NAME, subject.getName());
        values.put(Subject.COL_TEACHER, subject.getTeacher()+"");

        SQLiteDatabase db = this.getWritableDatabase();
        boolean createSuccessful = db.insert(Subject.TABLE_NAME, null, values) > 0;
        db.close();

        return createSuccessful;
    }

    public Subject[] getRow(String whereCondition)
    {

        int numberOfResults = getNumberOfRows(Subject.TABLE_NAME, whereCondition);
        Subject[] subjects = new Subject[numberOfResults];

        SQLiteDatabase db = getWritableDatabase();
        String query = QueryBuilder.buildSelectQuery(Subject.TABLE_NAME, whereCondition, "*");
        Cursor cursor = db.rawQuery(query, null);

        int i = 0;
        if(cursor.moveToFirst()) {
            do {
                subjects[i] = new Subject();
                subjects[i].setId(cursor.getInt(cursor.getColumnIndex(Subject.COL_ID)));
                subjects[i].setTeacher(cursor.getInt(cursor.getColumnIndex(Subject.COL_TEACHER)));
                subjects[i].setName(cursor.getString(cursor.getColumnIndex(Subject.COL_NAME)));
                i++;
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return subjects;
    }

    public boolean deleteRow(String whereCondition)
    {
        return super.deleteFromTable(Subject.TABLE_NAME,whereCondition);
    }

    public int getNumberOfRows()
    {
        return super.getNumberOfRows(Subject.TABLE_NAME);
    }


    public boolean updateRow(Subject subject) {

        ContentValues values = new ContentValues();

        values.put(Subject.COL_NAME, subject.getName());
        values.put(Subject.COL_TEACHER, subject.getTeacher());

        String where = "id = ?";

        String[] whereArgs = { Integer.toString(subject.getId()) };

        SQLiteDatabase db = this.getWritableDatabase();

        boolean updateSuccessful = db.update(Subject.TABLE_NAME, values, where, whereArgs) > 0;
        db.close();

        return updateSuccessful;
    }
}
