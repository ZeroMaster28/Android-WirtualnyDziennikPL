package com.example.e_dziennik.Backend.Database.TableControllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.e_dziennik.Backend.Database.DbHandler;
import com.example.e_dziennik.Backend.Database.QueryBuilder;
import com.example.e_dziennik.Backend.Persistence.Grade;

/**Kontroler do wykonywania działań na tabeli <code>GRADES</code>*
 * @author Michał
 */
public class TableGrades extends DbHandler {
    public TableGrades(Context context) {
        super(context);
    }

    public boolean createRow(Grade grade) {

        ContentValues values = new ContentValues();

        values.put(Grade.COL_ADDED, grade.getAdded().toString());
        values.put(Grade.COL_DESC, grade.getDescription());
        values.put(Grade.COL_GRADE, grade.getGrade());
        values.put(Grade.COL_MODIFIED, grade.getModified().toString());
        values.put(Grade.COL_SUBJ, grade.getStudentSubject());
        values.put(Grade.COL_TEACHER, grade.getTeacherId());


        SQLiteDatabase db = this.getWritableDatabase();

        boolean createSuccessful = db.insert(Grade.TABLE_NAME, null, values) > 0;
        db.close();

        return createSuccessful;
    }

    public boolean deleteRow(String whereCondition)
    {
        return super.deleteFromTable(Grade.TABLE_NAME, whereCondition);
    }

    public Grade[] getRow(String whereCondition)
    {
        int numOfResults = getNumberOfRows(Grade.TABLE_NAME, whereCondition);
        Grade[] grades = new Grade[numOfResults];

        SQLiteDatabase db = getWritableDatabase();
        String query = QueryBuilder.buildSelectQuery(Grade.TABLE_NAME, whereCondition, "*");
        Cursor cursor = db.rawQuery(query, null);

        int i = 0;
        if(cursor.moveToFirst()) {
            do {
                grades[i] = new Grade();
                grades[i].setId(cursor.getInt(cursor.getColumnIndex(Grade.COL_ID)));
                grades[i].setDescription(cursor.getString(cursor.getColumnIndex(Grade.COL_DESC)));
                grades[i].setAdded(cursor.getString(cursor.getColumnIndex(Grade.COL_ADDED)));
                grades[i].setModified(cursor.getString(cursor.getColumnIndex(Grade.COL_MODIFIED)));
                grades[i].setGrade(cursor.getString(cursor.getColumnIndex(Grade.COL_GRADE)));
                grades[i].setTeacherId(cursor.getInt(cursor.getColumnIndex(Grade.COL_TEACHER)));
                grades[i].setStudentSubject(cursor.getInt(cursor.getColumnIndex(Grade.COL_SUBJ)));
                i++;
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return grades;
    }

    public int getNumberOfRows()
    {
        return super.getNumberOfRows(Grade.TABLE_NAME);
    }

    public boolean updateRow(Grade grade) {

        ContentValues values = new ContentValues();

        values.put(Grade.COL_ID, grade.getId());
        values.put(Grade.COL_TEACHER, grade.getTeacherId());
        values.put(Grade.COL_SUBJ, grade.getStudentSubject());
        values.put(Grade.COL_ADDED, grade.getAdded());
        values.put(Grade.COL_MODIFIED, grade.getModified());
        values.put(Grade.COL_GRADE, grade.getGrade());
        values.put(Grade.COL_DESC, grade.getDescription());

        String where = "id = ?";

        String[] whereArgs = { Integer.toString(grade.getId()) };

        SQLiteDatabase db = this.getWritableDatabase();

        boolean updateSuccessful = db.update(Grade.TABLE_NAME, values, where, whereArgs) > 0;
        db.close();

        return updateSuccessful;
    }
}