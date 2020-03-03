package com.daya.dailytask.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daya.dailytask.R;
import com.daya.dailytask.data.DatabaseHandler;
import com.daya.dailytask.model.Task;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Task> taskList;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater layoutInflater;

    public RecyclerViewAdapter(Context context,List<Task> taskList){
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item_view,parent,false);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Task task = taskList.get(position);
        //task.setId(task.getTaskPriority());
        holder.title_view.setText(task.getTaskName());
        holder.duration_view.setText(String.valueOf(task.getTaskDuration())+" min");

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title_view;
        public TextView duration_view;
        public CheckBox done_checkbox;
        public CheckBox failed_checkbox;
        public ImageView editButton;
        public ImageView deleteButotn;
        public ViewHolder(@NonNull View itemView,Context cxt) {
            super(itemView);
            title_view = itemView.findViewById(R.id.taskTitle_view);
            duration_view = itemView.findViewById(R.id.taskDuration_view);
            done_checkbox = itemView.findViewById(R.id.doneCheckbox);
            failed_checkbox = itemView.findViewById(R.id.failedCheckbox);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButotn = itemView.findViewById(R.id.deleteButton);
            editButton.setOnClickListener(this);
            deleteButotn.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            switch(v.getId()){

                case R.id.editButton:
                    Task mytask = taskList.get(getAdapterPosition());
                    editTask(mytask);
                    break;
                case R.id.deleteButton:
                    Task sometask = taskList.get(getAdapterPosition());

                    deleteTask(sometask.getId());
                    break;
            }
        }

        private void editTask(final Task task) {

            builder = new AlertDialog.Builder(context);
            layoutInflater = LayoutInflater.from(context);
            View simple_view = layoutInflater.inflate(R.layout.popup,null);
           final EditText title_view = simple_view.findViewById(R.id.taskTitle);
           final EditText duration_view = simple_view.findViewById(R.id.taskDuration);
           final EditText priority_view = simple_view.findViewById(R.id.taskProirity);
           final RadioGroup radioGroup = simple_view.findViewById(R.id.radioGroup);
            RadioButton easyRadio = simple_view.findViewById(R.id.easyButton);
            RadioButton mediumRadio = simple_view.findViewById(R.id.mediumButton);
            RadioButton hardRadio = simple_view.findViewById(R.id.hardButton);
            final Button saveButton = simple_view.findViewById(R.id.popupSaveButton);
            builder.setView(simple_view);
            dialog = builder.create();
            dialog.show();
            title_view.setText(task.getTaskName());
            duration_view.setText(String.valueOf(task.getTaskDuration()));
            priority_view.setText(String.valueOf(task.getTaskPriority()));
            saveButton.setText("UPDATE");
            switch (task.getTaskType()){

                case "easy":
                    easyRadio.setChecked(true);
                    break;
                case "medium":
                    mediumRadio.setChecked(true);
                    break;
                case "hard":
                    hardRadio.setChecked(true);
                    break;
            }

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!title_view.getText().toString().isEmpty()
                            &&!duration_view.getText().toString().isEmpty()
                            &&!priority_view.getText().toString().isEmpty()
                    ){
                        DatabaseHandler databaseHandler = new DatabaseHandler(context);
                        task.setTaskName(title_view.getText().toString().trim());
                        task.setTaskDuration(Integer.parseInt(duration_view.getText().toString().trim()));
                        task.setTaskPriority(Integer.parseInt(priority_view.getText().toString().trim()));
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
                        databaseHandler.updateTask(task);

                        notifyItemChanged(getAdapterPosition(),task);
                        Snackbar.make(v,"UPDATE",Snackbar.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        },900);
                    }else {
                        Snackbar.make(v,"Empty Fields",Snackbar.LENGTH_SHORT).show();
                    }

                }
            });

        }

        private void deleteTask(final int id) {

            builder = new AlertDialog.Builder(context);
            layoutInflater = LayoutInflater.from(context);
            View simple_view = layoutInflater.inflate(R.layout.confirm_delete,null);
            Button yesButton = simple_view.findViewById(R.id.yesButton);
            Button noButton = simple_view.findViewById(R.id.noButton);

            builder.setView(simple_view);
            dialog = builder.create();
            dialog.show();

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db = new DatabaseHandler(context);

                    taskList.remove(getAdapterPosition());

                    dialog.dismiss();
                    notifyItemRemoved(getAdapterPosition());
                    db.deleteTask(id);
                    Log.d("DeleteBug", "onClick: "+"recycle is"+id);
                }
            });

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });



        }
    }
}
