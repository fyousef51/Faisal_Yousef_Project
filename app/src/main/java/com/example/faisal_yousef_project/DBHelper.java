package com.example.faisal_yousef_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="Users.db";
    public static  final String TABLE_NAME="User";

    public static final String COLUMN_FNAME="FName";
    public static final String COLUMN_LNAME="LName";
    public static final String COLUMN_PHONE="PHONE";
    public static final String COLUMN_EMAIL="EMAIL";
    public static final String COLUMN_UID="UID";

    private SQLiteDatabase db;
    public DBHelper(@Nullable Context context ) {
        super(context, DATABASE_NAME, null, 1);
        db=getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                        + "(" + COLUMN_FNAME + " TEXT NOT NULL,"
                        + COLUMN_LNAME + " TEXT NOT NULL,"
                        + COLUMN_PHONE + " TEXT NOT NULL,"
                        + COLUMN_EMAIL + " TEXT NOT NULL,"
                        + COLUMN_UID +" INTEGER PRIMARY KEY)");
    }

    public int insert(String fname,String lname,String phone,String email,int uid){
        ContentValues values = new ContentValues();

        values.put(COLUMN_FNAME, fname);
        values.put(COLUMN_LNAME, lname);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_UID,uid);

        return (int) db.insert(TABLE_NAME, null, values);

    }
    public int insert(User user){
        return insert(user.firstName,user.lastName,user.phoneNumber,user.emailAddress,user.userId);
    }



    public Cursor selectAll(){
        Cursor c= db.rawQuery("SELECT * FROM "+ TABLE_NAME, null);

        if (c.moveToFirst()){
            return c;
        }else{
            return null;
        }
    }

    public Cursor selectByUID(int uid){
        String [] args={uid+""};
        Cursor c=db.rawQuery("SELECT * FROM "+ TABLE_NAME+" WHERE "+COLUMN_UID+"=?",args);
        if (c != null)
            c.moveToFirst();
        if (!c.moveToFirst()){
            return null;
        }
        return c;
    }


    public int  deleteByUID(int uid){
        String [] args={uid+""};
        return db.delete(TABLE_NAME, COLUMN_UID+" = ?", args);
    }

    /* Every time the dB is updated (or upgraded) */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }



    public int updateUser(String fname,String lname,String phone,String email,int uid){
        ContentValues values = new ContentValues();

        values.put(COLUMN_FNAME, fname);
        values.put(COLUMN_LNAME, lname);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_UID,uid);
        String [] args={uid+""};
        return db.update(TABLE_NAME,values,COLUMN_UID+"=?",args);

    }

}
