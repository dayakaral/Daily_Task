package com.daya.dailytask;

import android.os.Bundle;

import com.daya.dailytask.data.DatabaseHandler;
import com.daya.dailytask.model.Task;
import com.daya.dailytask.ui.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private View popview;
    private DatabaseHandler databaseHandler;
    //private TextView textView;
    private Button popupSaveButton;
    private EditText taskTitle;
    private EditText taskDuration;
    private EditText taskPriority;
    private RadioGroup radioGroup;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Task> taskList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        databaseHandler = new DatabaseHandler(MainActivity.this);
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskList = new ArrayList<>();
        if(databaseHandler.getCount()!=0){

            taskList = databaseHandler.getAllTask();
        }

        recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this,taskList);
        Log.d("TaskList", "onCreate: "+"problem");
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();





        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopup();
            }
        });








    }




    private void createPopup() {

        builder = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.popup,null);
        taskTitle = view.findViewById(R.id.taskTitle);
        taskDuration = view.findViewById(R.id.taskDuration);
        taskPriority = view.findViewById(R.id.taskProirity);
        radioGroup = view.findViewById(R.id.radioGroup);
        popupSaveButton = view.findViewById(R.id.popupSaveButton);
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();
        popupSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!taskTitle.getText().toString().isEmpty()
                &&!taskDuration.getText().toString().isEmpty()
                &&!taskPriority.getText().toString().isEmpty()
                ){
                    savePopupData(v);
                }else {
                    Snackbar.make(v,"Empty Fields",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void savePopupData(View v) {

        Task task = new Task();
        task.setTaskName(taskTitle.getText().toString().trim());
        task.setTaskDuration(Integer.parseInt(taskDuration.getText().toString().trim()));
        task.setTaskPriority(Integer.parseInt(taskPriority.getText().toString().trim()));
        switch (radioGroup.getCheckedRadioButtonId()){
            case R.id.easyButton:
                task.setTaskType("easy");
                break;
            case R.id.mediumButton:
                task.setTaskType("medium");
                break;
            case R.id.hardButton:
                task.setTaskType("hard");
                break;
        }
        databaseHandler.addTask(task);
        taskList.add(databaseHandler.getTask(taskList.size()+1));
        recyclerViewAdapter.notifyDataSetChanged();
        Snackbar.make(v,"SAVED",Snackbar.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        },900);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
