
package com.example.studenttaskplanner;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    EditText edtTaskId;
    Button btnShowTask;
    TextView txtTaskDetails;

    Taskdb database;

    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        btnBack=findViewById(R.id.btnBack);


        btnBack.setOnClickListener(v -> finish());

        edtTaskId = findViewById(R.id.edtTaskId);
        btnShowTask = findViewById(R.id.btnShowTask);
        txtTaskDetails = findViewById(R.id.txtTaskDetails);

        database = new Taskdb(this);

        btnShowTask.setOnClickListener(v -> showTask());
    }

    private void showTask() {
        String text = edtTaskId.getText().toString().trim();

        if (text.isEmpty()) {
            edtTaskId.setError("Enter task ID");
            return;
        }

        int id;

        try {
            id = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            edtTaskId.setError("Enter a valid ID");
            return;
        }

        Taskdb.Task task = database.getTaskById(id);

        if (task == null) {
            txtTaskDetails.setText("No task found with ID " + id);
        } else {
            String details =
                    "Task ID: " + task.id +
                            "\n\nTitle: " + task.title +
                            "\n\nNotes: " +
                            (task.notes == null || task.notes.isEmpty()
                                    ? "No notes"
                                    : task.notes) +
                            "\n\nDue Date: " + task.dueDate +
                            "\n\nStatus: " +
                            (task.done ? "Completed" : "Pending");

            txtTaskDetails.setText(details);
        }
    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
