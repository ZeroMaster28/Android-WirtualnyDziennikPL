package com.example.e_dziennik.Backend.Database.TableControllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.e_dziennik.Backend.Database.DbHandler;
import com.example.e_dziennik.Backend.Database.QueryBuilder;
import com.example.e_dziennik.Backend.Persistence.User;

/**Kontroler do wykonywania działań na tabeli <code>USERS</code>*
 * @author Michał
 */
public class TableUsers extends DbHandler {
    public TableUsers(Context context) {
        super(context);
    }

    public boolean createRow(User user) {

        ContentValues values = new ContentValues();

        values.put(User.COL_NAME, user.getName());
        values.put(User.COL_ROLE, user.getRole());
        values.put(User.COL_SEC_NAME, user.getSecondName());
        values.put(User.COL_USER_ID, user.getCode());
        values.put(User.COL_DESC, user.getDescription());

        SQLiteDatabase db = this.getWritableDatabase();

        boolean createSuccessful = db.insert(User.TABLE_NAME, null, values) > 0;
        db.close();

        return createSuccessful;
    }

    public boolean deleteRow(String whereCondition)
    {
        return super.deleteFromTable(User.TABLE_NAME, whereCondition);
    }

    public User[] getRow(String whereCondition)
    {
        int numOfResults = getNumberOfRows(User.TABLE_NAME, whereCondition);
        User[] users = new User[numOfResults];

        SQLiteDatabase db = getWritableDatabase();
        String query = QueryBuilder.buildSelectQuery(User.TABLE_NAME, whereCondition, "*");
        Cursor cursor = db.rawQuery(query, null);

        int i = 0;
        if(cursor.moveToFirst()) {
            do {
                users[i] = new User();
                users[i].setId(cursor.getInt(cursor.getColumnIndex(User.COL_ID)));
                users[i].setDescription(cursor.getString(cursor.getColumnIndex(User.COL_DESC)));
                users[i].setName(cursor.getString(cursor.getColumnIndex(User.COL_NAME)));
                users[i].setCode(cursor.getString(cursor.getColumnIndex(User.COL_USER_ID)));
                users[i].setRole(cursor.getString(cursor.getColumnIndex(User.COL_ROLE)));
                users[i].setSecondName(cursor.getString(cursor.getColumnIndex(User.COL_SEC_NAME)));
                i++;
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return users;
    }

    public int getNumberOfRows()
    {
        return super.getNumberOfRows(User.TABLE_NAME);
    }
}
