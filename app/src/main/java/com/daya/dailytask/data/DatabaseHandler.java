package com.daya.dailytask.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.daya.dailytask.model.Task;
import com.daya.dailytask.util.Util;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private Context context;
    public DatabaseHandler(@Nullable Context context) {

        super(context,Util.DATABASE_NAME,null,Util.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + Util.TABLE_NAME + "("
                + Util.KEY_ID + " INTEGER PRIMARY KEY,"
                + Util.KEY_TASK_NAME + " TEXT,"
                + Util.KEY_TASK_DURATION + " INTEGER,"
                + Util.KEY_TASK_PRIORITY + " INTEGER,"
                + Util.KEY_TYPE + " TEXT);";
        db.execSQL(sql);
       // Log.d("create_table", "onCreate: "+"created table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+Util.TABLE_NAME);
        onCreate(db);
    }

    public void addTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.KEY_TASK_NAME,task.getTaskName());
        contentValues.put(Util.KEY_TASK_DURATION,task.getTaskDuration());
        contentValues.put(Util.KEY_TASK_PRIORITY,task.getTaskPriority());
        contentValues.put(Util.KEY_TYPE,task.getTaskType());
        db.insert(Util.TABLE_NAME,null,contentValues);
        //Log.d("Item_add", "addTask: "+"item added");
        db.close();
    }

    public Task getTask(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Task task = new Task();
        Cursor cursor = db.query(Util.TABLE_NAME,
                new String[]{Util.KEY_ID,
                        Util.KEY_TASK_NAME,
                        Util.KEY_TASK_DURATION,
                        Util.KEY_TASK_PRIORITY,
                        Util.KEY_TYPE},
                Util.KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if(cursor!=null){

            cursor.moveToFirst();
            task.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Util.KEY_ID))));
            task.setTaskName(cursor.getString(cursor.getColumnIndex(Util.KEY_TASK_NAME)));
            task.setTaskDuration(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Util.KEY_TASK_DURATION))));
            task.setTaskPriority(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Util.KEY_TASK_PRIORITY))));
            task.setTaskType(cursor.getString(cursor.getColumnIndex(Util.KEY_TYPE)));

        }
        else
            Log.d("Daya", "getTask: "+"getting is failed");
        cursor.close();
        db.close();
        return task;
    }

    public List<Task> getAllTask(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Task> taskList = new ArrayList<>();

        Cursor cursor = db.rawQuery("select * from "+Util.TABLE_NAME,null);
        if(cursor!=null){

            cursor.moveToFirst();
            do {
                Task task = new Task();
                task.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Util.KEY_ID))));
                task.setTaskName(cursor.getString(cursor.getColumnIndex(Util.KEY_TASK_NAME)));
                task.setTaskDuration(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Util.KEY_TASK_DURATION))));
                task.setTaskPriority(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Util.KEY_TASK_PRIORITY))));
                task.setTaskType(cursor.getString(cursor.getColumnIndex(Util.KEY_TYPE)));
                taskList.add(task);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();


        return taskList;
    }

    public void updateTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.KEY_TASK_NAME,task.getTaskName());
        contentValues.put(Util.KEY_TASK_DURATION,task.getTaskDuration());
        contentValues.put(Util.KEY_TASK_PRIORITY,task.getTaskPriority());
        contentValues.put(Util.KEY_TYPE,task.getTaskType());
        db.update(Util.TABLE_NAME, contentValues
        , Util.KEY_ID +"=?", new String[]{String.valueOf(task.getId())});
        db.close();

    }

    public void deleteTask(int i){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Util.TABLE_NAME,Util.KEY_ID+"=?",new String[]{String.valueOf(i)});

        db.close();

    }

    public int getCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+Util.TABLE_NAME,null);
        return cursor.getCount();
    }

}
