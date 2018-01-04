package com.AttendanceSimplified.vaibhav.dtuattendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class downloaddata extends SQLiteOpenHelper {
    public static final String COL_1 = "slot_id";
    public static final String COL_10 = "a_string";
    public static final String COL_11 = "a_stringedit";
    public static final String COL_2 = "subject";
    public static final String COL_3 = "class_time";
    public static final String COL_4 = "batch";
    public static final String COL_5 = "student_id";
    public static final String COL_6 = "name";
    public static final String COL_7 = "slot_id";
    public static final String COL_8 = "student_id";
    public static final String COL_9 = "slot_id";
    public static final String DATABASE_NAME = "DataDownload.db";
    public static final String TABLE_NAME1 = "slots";
    public static final String TABLE_NAME2 = "student";
    public static final String TABLE_NAME3 = "slot_student";
    public static final String TABLE_NAME4 = "attendance";

    public downloaddata(Context context) {
        super(context, DATABASE_NAME, null, 12);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table slots (slot_id INTEGER ,subject TEXT,class_time TEXT,batch TEXT)");
        db.execSQL("create table student (name TEXT ,student_id TEXT)");
        db.execSQL("create table slot_student (slot_id INTEGER ,student_id TEXT)");
        db.execSQL("create table attendance (slot_id INTEGER PRIMARY KEY  ,a_string TEXT,a_stringedit TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS slots");
        db.execSQL("DROP TABLE IF EXISTS student");
        db.execSQL("DROP TABLE IF EXISTS slot_student");
        db.execSQL("DROP TABLE IF EXISTS attendance");
        onCreate(db);
    }

    public boolean deleteslot(int slot) {
        getWritableDatabase().execSQL(" delete from slots where slot_id=" + slot);
        return true;
    }

    public boolean deleteattendance(int slot) {
        getWritableDatabase().execSQL(" delete from attendance where slot_id=" + slot);
        return true;
    }

    public boolean truncatestudent() {
        getWritableDatabase().execSQL(" delete from student");
        return true;
    }

    public boolean truncateslotstudent() {
        getWritableDatabase().execSQL(" delete from slot_student");
        return true;
    }

    public boolean truncateslots() {
        getWritableDatabase().execSQL(" delete from slots");
        return true;
    }

    public boolean insertslots(String id, String subject, String class_time, String batch) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("slot_id", id);
        contentValues.put(COL_2, subject);
        contentValues.put(COL_3, class_time);
        contentValues.put(COL_4, batch);
        if (db.insert(TABLE_NAME1, null, contentValues) == -1) {
            return false;
        }
        return true;
    }

    public boolean insertstudent(String student_id, String name) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("student_id", student_id);
        contentValues.put(COL_6, name);
        if (db.insert(TABLE_NAME2, null, contentValues) == -1) {
            return false;
        }
        return true;
    }

    public boolean insertslotid(String slot_id, String student_id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("slot_id", slot_id);
        contentValues.put("student_id", student_id);
        if (db.insert(TABLE_NAME3, null, contentValues) == -1) {
            return false;
        }
        return true;
    }

    public boolean insertattendance(int slot, String attendnace, String editattend) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("slot_id", Integer.valueOf(slot));
        contentValues.put(COL_10, attendnace);
        contentValues.put(COL_11, editattend);
        if (db.insert(TABLE_NAME4, null, contentValues) == -1) {
            return false;
        }
        return true;
    }

    public Cursor getattendance() {
        return getWritableDatabase().rawQuery("select * from attendance", null);
    }

    public Cursor getAllData() {
        return getWritableDatabase().rawQuery("select * from slots where date(class_time) = date('now') ", null);
    }

    public Cursor getAllData2() {
        return getWritableDatabase().rawQuery("select * from slots", null);
    }

    public Cursor getAllDatajoinedname(int slot_id) {
        return getWritableDatabase().rawQuery("select st.student_id as student_id, st.name as name  from slot_student ss left join student st on st.student_id = ss.student_id where ss.slot_id = " + slot_id, null);
    }

    public Cursor getAllDataspecificslot(int s) {
        return getWritableDatabase().rawQuery("select * from slots where slot_id=" + s + "", null);
    }

    public Cursor getAllDatastudent() {
        return getWritableDatabase().rawQuery("select * from student", null);
    }

    public Cursor getAllDataslotstudent(int s) {
        SQLiteDatabase db = getWritableDatabase();
        String slotid = String.valueOf(s);
        String slot_id = "slot_id";
        return db.rawQuery("select * from slot_student where slot_id = " + s, null);
    }
}
