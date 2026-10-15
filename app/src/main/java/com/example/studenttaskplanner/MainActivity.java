
package com.example.studenttaskplanner;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText edtTitle, edtNotes;
    Button btnChooseDate, btnAddTask, btnShowTasks;
    TextView txtChosenDate;

    Taskdb database;
    String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtTitle = findViewById(R.id.edtTitle);
        edtNotes = findViewById(R.id.edtNotes);
        txtChosenDate = findViewById(R.id.txtChosenDate);

        btnChooseDate = findViewById(R.id.btnChooseDate);
        btnAddTask = findViewById(R.id.btnAddTask);
        btnShowTasks = findViewById(R.id.btnShowTasks);

        database = new Taskdb(this);

        Calendar today = Calendar.getInstance();

        selectedDate = String.format(
                Locale.US,
                "%04d-%02d-%02d",
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH) + 1,
                today.get(Calendar.DAY_OF_MONTH)
        );

        txtChosenDate.setText("Due date: " + selectedDate);

        btnChooseDate.setOnClickListener(v -> openDatePicker());
        btnAddTask.setOnClickListener(v -> addTask());

        btnShowTasks.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    MainActivity2.class
            );

            startActivity(intent);
        });
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, day) -> {
                    selectedDate = String.format(
                            Locale.US,
                            "%04d-%02d-%02d",
                            year,
                            month + 1,
                            day
                    );

                    txtChosenDate.setText("Due date: " + selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        dialog.show();
    }

    private void addTask() {
        String title = edtTitle.getText().toString().trim();
        String notes = edtNotes.getText().toString().trim();

        if (title.isEmpty()) {
            edtTitle.setError("Enter task title");
            return;
        }

        long result = database.addTask(title, notes, selectedDate);

        if (result != -1) {
            Toast.makeText(
                    this,
                    "Task saved! Task ID: " + result,
                    Toast.LENGTH_LONG
            ).show();

            edtTitle.setText("");
            edtNotes.setText("");
        } else {
            Toast.makeText(
                    this,
                    "Could not save task",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
