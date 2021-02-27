package DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Entity.User;

public class UsersDataSource implements Serializable {
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {
            MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_EMAIL,
            MySQLiteHelper.COLUMN_PASSWD
    };

    public UsersDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
    public void close() {
        dbHelper.close();
    }

    public void createUser(User user) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_EMAIL, user.getEmail());
        values.put(MySQLiteHelper.COLUMN_PASSWD, user.getPasswd());

        database.insert(MySQLiteHelper.TABLE_USERS, null, values);

    }

    public boolean checkUser(String email) {
        // array of columns to fetch
//        String[] columns = {
//                COLUMN_USER_ID
//        };
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // selection criteria
        String selection = MySQLiteHelper.COLUMN_EMAIL + " = ?";
        // selection argument
        String[] selectionArgs = {email};

        Cursor cursor = db.query(MySQLiteHelper.TABLE_USERS, //Table to query
                allColumns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    public void deleteUser(User user) {
        long id = user.getId();
//        System.out.println("direction_service deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_USERS,
                MySQLiteHelper.COLUMN_ID + " = " + id,
                null);
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_USERS,
                allColumns,
                null,
                null,
                null,
                null,
                null);

        // On place le curseur au début en vérifiant qu’il contient des résultats.
        cursor.moveToFirst();
        //Déplace le curseur à la première position pour lire les données de la première ligne.
        while (!cursor.isAfterLast()) {
            User user = cursorToUser(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return users;
    }
    private User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getLong(0));
        user.setEmail(cursor.getString(1));
        user.setPasswd(cursor.getString(2));
        return user;
    }
}