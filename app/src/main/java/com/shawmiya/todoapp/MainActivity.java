package com.shawmiya.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText addTask, updateTask;
    Button btnAddTask;
    ImageButton btnRefresh, btnUpdateTask, btnDelete;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);
        addTask = findViewById(R.id.taskEditTextField);
        updateTask = findViewById(R.id.deleteAndUpdateTextField);
        btnAddTask = findViewById(R.id.buttonAdd);
        btnUpdateTask = findViewById(R.id.buttonUpdate);
        btnDelete = findViewById(R.id.buttonDelete);
        btnRefresh = findViewById(R.id.buttonRefresh); // get the reference to the refresh button
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        updateRecyclerView();
        addData();
        updateData();
        deleteData();

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                refreshData();
                Toast.makeText(MainActivity.this, "Task Refreshed", Toast.LENGTH_LONG).show();
            }
        });
    }

    //Add Data
    public void addData() {
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isInserted = myDb.insertData(addTask.getText().toString());

                if (isInserted == true) {
                    Toast.makeText(MainActivity.this, "Task Inserted Successfully", Toast.LENGTH_LONG).show();
                    updateRecyclerView(); // update the RecyclerView with the latest data
                } else {
                    Toast.makeText(MainActivity.this, "Task Not Inserted", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Task Updating List Field
    public void updateRecyclerView() {
        Cursor results = myDb.getAllData();
        if (results.getCount() == 0) {
            showMessage("Note : ","No Task available to Display");
            return;
        }
        ArrayList<String> dataList = new ArrayList<>();
        while (results.moveToNext()) {
            String data = "ID :" + results.getString(0) + " " +
                    "   " + results.getString(1) + "\n";
            dataList.add(data);

        }
        adapter = new RecyclerView.Adapter<RecyclerView.ViewHolder>() {

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
                RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(view) {};
                return viewHolder;
            }
            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                ((TextView) holder.itemView.findViewById(android.R.id.text1)).setText(dataList.get(position));
            }

            @Override
            public int getItemCount() {
                return dataList.size();
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private void showMessage(String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
     //Updatae Data
    public void updateData(){
        btnUpdateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isUpdate = myDb.updateData(updateTask.getText().toString(), addTask.getText().toString());

                if (isUpdate == true) {
                    Toast.makeText(MainActivity.this, "Task Updated", Toast.LENGTH_LONG).show();
                    updateRecyclerView(); // update the RecyclerView with the latest data
                }
                else {
                    Toast.makeText(MainActivity.this, "Task Not Updated", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Delete Data
    public void deleteData(){
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer deleteDataRows = myDb.deleteData(updateTask.getText().toString());
                if (deleteDataRows > 0) {
                    Toast.makeText(MainActivity.this, "Task Deleted", Toast.LENGTH_LONG).show();
                    updateRecyclerView(); // update the RecyclerView with the latest data
                }

                else {
                    Toast.makeText(MainActivity.this, "Task not Deleted", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    // method for refreshing database
    public void refreshData() {
        myDb.deleteAllData(); // delete all data from the database
        updateRecyclerView(); // update the RecyclerView with empty data
        adapter = new RecyclerView.Adapter<RecyclerView.ViewHolder>() { // create an empty adapter
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
                RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(view) {};
                return viewHolder;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                ((TextView) holder.itemView.findViewById(android.R.id.text1)).setText("");
            }

            @Override
            public int getItemCount() {
                return 0;
            }


        };
        recyclerView.setAdapter(adapter); // set the empty adapter to the RecyclerView
        addTask.setText(""); // clear the task text field
        updateTask.setText(""); // clear the update text field

    }

}