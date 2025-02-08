package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddteacherActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private EditText teacherIdInput,  nameInput, emailInput, classnameInput;
    private Button addteacherButton;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_teacher_activity);


        db = FirebaseFirestore.getInstance();


        teacherIdInput = findViewById(R.id.teacher_id_input);
        nameInput = findViewById(R.id.name_input);
        emailInput = findViewById(R.id.email_input);
        classnameInput = findViewById(R.id.classname_input);
        addteacherButton = findViewById(R.id.add_teacher_button);


        addteacherButton.setOnClickListener(v -> addTeacher());
    }

    private void addTeacher() {

        String teacherId = teacherIdInput.getText().toString().trim();
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String classname = classnameInput.getText().toString().trim();


        if (teacherId.isEmpty() || name.isEmpty() || email.isEmpty() || classname.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> teacherData = new HashMap<>();
        teacherData.put("teacherId", teacherId);
        teacherData.put("name", name);
        teacherData.put("email", email);
        teacherData.put("classname", classname);


        db.collection("Teachers")
                .document(teacherId)
                .set(teacherData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Teacher added successfully", Toast.LENGTH_SHORT).show();
                    clearFields();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error adding teacher: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    private void clearFields() {
        teacherIdInput.setText("");
        classnameInput.setText("");
        emailInput.setText("");
        nameInput.setText("");
    }
}
