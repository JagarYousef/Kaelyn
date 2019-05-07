package me.jagar.kaelyn.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import me.jagar.kaelyn.constants.RecordConstant;
import me.jagar.kaelyn.models.Record;

public class SQLDatabaseHelper extends SQLiteOpenHelper {

    public SQLDatabaseHelper(Context context) {
        super(context, RecordConstant.DB_NAME, null, RecordConstant.DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + RecordConstant.RECORDS_TABLE_NAME + " ("
                + RecordConstant.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + RecordConstant.AMOUNT + " REAL,"
                + RecordConstant.RATE + " REAL,"
                + RecordConstant.TIME + " REAL,"
                + RecordConstant.NOTE + " TEXT,"
                + RecordConstant.EMO + " INTEGER,"
                + RecordConstant.DATE + " INTEGER)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecordConstant.RECORDS_TABLE_NAME);
        this.onCreate(sqLiteDatabase);
    }

    public void insertRecord(Record record){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(RecordConstant.AMOUNT, record.getAmount());
        contentValues.put(RecordConstant.RATE, record.getRate());
        contentValues.put(RecordConstant.TIME, record.getTime());
        contentValues.put(RecordConstant.NOTE, record.getNote());
        contentValues.put(RecordConstant.EMO, record.getEmo());
        contentValues.put(RecordConstant.DATE, record.getDate());
        db.insert(RecordConstant.RECORDS_TABLE_NAME, null, contentValues);
        db.close();
    }
    public void updateRecord(Record record){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(RecordConstant.AMOUNT, record.getAmount());
        contentValues.put(RecordConstant.RATE, record.getRate());
        contentValues.put(RecordConstant.TIME, record.getTime());
        contentValues.put(RecordConstant.NOTE, record.getNote());
        contentValues.put(RecordConstant.EMO, record.getEmo());
        contentValues.put(RecordConstant.DATE, record.getDate());
        db.update(RecordConstant.RECORDS_TABLE_NAME, contentValues, "id = ?", new String[]{String.valueOf(record.getId())});
        db.close();
    }

    public List<Record> getAllRecords(){
        List<Record> records = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + RecordConstant.RECORDS_TABLE_NAME + " ORDER BY " + RecordConstant.DATE + " DESC";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Record record = new Record();
                record.setId(cursor.getInt(0));
                record.setAmount(cursor.getDouble(1));
                record.setRate(cursor.getDouble(2));
                record.setTime(cursor.getDouble(3));
                record.setNote(cursor.getString(4));
                record.setEmo(cursor.getInt(5));
                record.setDate(cursor.getLong(6));
                records.add(record);
            } while (cursor.moveToNext());
        }
        db.close();
        return records;
    }

    public List<Record> getSimleRecords(){
        List<Record> records = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + RecordConstant.RECORDS_TABLE_NAME + " WHERE emo = 0";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Record record = new Record();
                record.setId(cursor.getInt(0));
                record.setAmount(cursor.getDouble(1));
                record.setRate(cursor.getDouble(2));
                record.setTime(cursor.getDouble(3));
                record.setNote(cursor.getString(4));
                record.setEmo(cursor.getInt(5));
                record.setDate(cursor.getLong(6));
                records.add(record);
            } while (cursor.moveToNext());
        }
        db.close();
        return records;
    }
    public List<Record> getNormalRecords(){
        List<Record> records = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + RecordConstant.RECORDS_TABLE_NAME + " WHERE emo = 1";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Record record = new Record();
                record.setId(cursor.getInt(0));
                record.setAmount(cursor.getDouble(1));
                record.setRate(cursor.getDouble(2));
                record.setTime(cursor.getDouble(3));
                record.setNote(cursor.getString(4));
                record.setEmo(cursor.getInt(5));
                record.setDate(cursor.getLong(6));
                records.add(record);
            } while (cursor.moveToNext());
        }
        db.close();
        return records;
    }

    public List<Record> getSadRecord(){
        List<Record> records = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + RecordConstant.RECORDS_TABLE_NAME + " WHERE emo = 2";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Record record = new Record();
                record.setId(cursor.getInt(0));
                record.setAmount(cursor.getDouble(1));
                record.setRate(cursor.getDouble(2));
                record.setTime(cursor.getDouble(3));
                record.setNote(cursor.getString(4));
                record.setEmo(cursor.getInt(5));
                record.setDate(cursor.getLong(6));
                records.add(record);
            } while (cursor.moveToNext());
        }
        db.close();
        return records;
    }

    public void deleteRecord(Record record){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(RecordConstant.RECORDS_TABLE_NAME, "id=?", new String[]{String.valueOf(record.getId())});
        db.close();
    }

    public double getTotalAmount(){
        double totalAmount = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + RecordConstant.RECORDS_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                totalAmount += cursor.getDouble(1);
            } while (cursor.moveToNext());
        }
        db.close();
        return totalAmount;
    }

    public double getTotalTime(){
        double totalTime = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + RecordConstant.RECORDS_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                totalTime += cursor.getDouble(3);
            } while (cursor.moveToNext());
        }
        db.close();
        return totalTime;
    }
}
