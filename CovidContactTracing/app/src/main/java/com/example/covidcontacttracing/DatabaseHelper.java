package com.example.covidcontacttracing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import java.io.FileInputStream;
import java.io.IOException;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "nocovid.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DBNAME, null, DATABASE_VERSION);
        //SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE User(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT, " +
                "name TEXT, " +
                "password TEXT, " +
                "registeredDate DATE DEFAULT CURRENT_DATE, " +
                "profilePic BLOB DEFAULT NULL, " +
                "profileCover BLOB DEFAULT NULL, " +
                "gender TEXT DEFAULT NULL, " +
                "tel TEXT DEFAULT NULL, " +
                "dob DATE DEFAULT NULL, " +
                "state TEXT DEFAULT NULL)");
        db.execSQL("CREATE TABLE History(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "location TEXT, " +
                "date TEXT, " +
                "time TEXT, " +
                "risk TEXT, " +
                "address TEXT, " +
                "tel TEXT, " +
                "img TEXT, " +
                "uid INTEGER)");

        fillUserTable(db);
        fillHistoryTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS User");
        db.execSQL("DROP TABLE IF EXISTS History");
        onCreate(db);
    }

    private void fillUserTable(SQLiteDatabase db){
        addUser(db, "bookman@xmu.edu.my", "Bookman", "bookman");
    }

    private void fillHistoryTable(SQLiteDatabase db){
        addHistory(db, "Check-in at Xiamen University Malaysia", "April 27, 2021", "3:30 PM", "Low Risk", "Jalan Sunsuria, Bandar Sunsuria, 43900 Sepang, Selangor", "03-8800 6800", "map_xmum", 1);
    }

    // used to hard code some existing user in database
    private void addUser(SQLiteDatabase db, String email, String name, String password){
        ContentValues cv = new ContentValues();
        cv.put("email", email);
        cv.put("name", name);
        cv.put("password", password);
        db.insert("User", null, cv);
    }

    //insert User data when register - return id to create shared preference
    public int addUser(String email, String name, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("email", email);
        cv.put("name", name);
        cv.put("password", password);
        long result = db.insert("User", null, cv);

        return (int) result;
    }

    //Get Information about the User
    //Because when create the User, we let the registeredDate to be default
    //But in sqlite, it will automatically convert to UTC
    //Hence when retrieving registeredDate, I convert back to localtime
    public Cursor getUserData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT name, email, password, gender, tel, dob, state, date(registeredDate, 'localtime') as registeredDate FROM User WHERE id = ?", new String[] {id});
        return res;
    }

    //Used when User edit profile
    public boolean updateUser(String id, String name, String email, String tel, String dob, String gender, String state){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("email", email);
        cv.put("tel", tel);
        cv.put("dob", dob);
        cv.put("gender", gender);
        cv.put("state", state);
        long result = db.update("User", cv, "id = ?", new String[] {id});

        return result != -1;
    }

    //Check if email has existed
    public Boolean checkEmailUniqueness(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE email = ?",new String[]{email});

        //true if duplicate id
        return cursor.getCount() > 0;
    }

    //Check if username has existed
    public Boolean checkNameUniqueness(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE name = ?",new String[]{name});

        //true if duplicate id
        return cursor.getCount() > 0;
    }

    //To check password & return userId that logged in
    public int checkCredential(String name, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM User WHERE name = ? AND password = ? ",new String[]{name, password});
        if(cursor.getCount() > 0)
        {
            cursor.moveToNext();
            return cursor.getInt(cursor.getColumnIndex("id"));
        }
        else
        {
            return 0;
        }
    }

    // Used when user change password
    public boolean updatePassword(String id, String pw){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("password", pw);
        long result = db.update("User", cv, "id = ?", new String[] {id});

        return result != -1;
    }

    // used to hard code some check-in history in database
    private void addHistory(SQLiteDatabase db, String location, String date, String time, String risk, String address, String tel, String img, int uid){
        ContentValues cv = new ContentValues();
        cv.put("location", location);
        cv.put("date", date);
        cv.put("time", time);
        cv.put("risk", risk);
        cv.put("address", address);
        cv.put("tel", tel);
        cv.put("img", img);
        cv.put("uid", uid);
        db.insert("History", null, cv);
    }

    // add history when scan qr code
    public boolean addHistory(String location, String date, String time, String risk, String address, String tel, String img, int uid){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("location", location);
        cv.put("date", date);
        cv.put("time", time);
        cv.put("risk", risk);
        cv.put("address", address);
        cv.put("tel", tel);
        cv.put("img", img);
        cv.put("uid", uid);
        long result = db.insert("History", null, cv);

        return result != -1;
    }

    // retrieve a set of history for a specific user
    public Cursor getHistoryData(String uid){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT location, date, time, risk, address, tel, img FROM History WHERE uid = ? ORDER BY id DESC", new String[] {uid});
        return res;
    }

    //Update User's profile picture
    public Boolean updateProfilePic(String x, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            FileInputStream fs = new FileInputStream(x);
            byte[] imgByte = new byte[fs.available()];
            fs.read(imgByte);
            ContentValues cv = new ContentValues();
            cv.put("profilePic", imgByte);
            db.update("User", cv, "id = ?", new String[] {id});
            fs.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Retrieve profile image
    public Bitmap getProfilePic(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Bitmap bt = null;
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE id = ?", new String[] {id});
        if(cursor.moveToNext()) {
            byte[] img = cursor.getBlob(cursor.getColumnIndex("profilePic"));
            if (img != null)
                bt = BitmapFactory.decodeByteArray(img, 0, img.length);
        }
        return bt;
    }

    //Update User's profile cover
    public Boolean updateProfileCover(String x, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            FileInputStream fs = new FileInputStream(x);
            byte[] imgByte = new byte[fs.available()];
            fs.read(imgByte);
            ContentValues cv = new ContentValues();
            cv.put("profileCover", imgByte);
            db.update("User", cv, "id = ?", new String[] {id});
            fs.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Retrieve profile cover
    public Bitmap getProfileCover(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Bitmap bt = null;
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE id = ?", new String[] {id});
        if(cursor.moveToNext()) {
            byte[] img = cursor.getBlob(cursor.getColumnIndex("profileCover"));
            if (img != null)
                bt = BitmapFactory.decodeByteArray(img, 0, img.length);
        }
        return bt;
    }
}
