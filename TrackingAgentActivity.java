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

public class TrackingAgentActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private EditText teacherIdInput, absenceDateInput, absenceTimeInput, salleInput, classeInput;
    private Button addAbsenceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trackingagent_activity);


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        teacherIdInput = findViewById(R.id.teacher_id_input);
        absenceDateInput = findViewById(R.id.absence_date_input);
        absenceTimeInput = findViewById(R.id.absence_time_input);
        salleInput = findViewById(R.id.salle_input);
        classeInput = findViewById(R.id.classe_input);
        addAbsenceButton = findViewById(R.id.add_absence_button);


        addAbsenceButton.setOnClickListener(v -> addAbsence());


    }

    private void addAbsence() {

        String simpleTeacherId = teacherIdInput.getText().toString().trim();
        String absenceDate = absenceDateInput.getText().toString().trim();
        String absenceTime = absenceTimeInput.getText().toString().trim();
        String room = salleInput.getText().toString().trim();
        String className = classeInput.getText().toString().trim();

        if (simpleTeacherId.isEmpty() || absenceDate.isEmpty() || absenceTime.isEmpty() || room.isEmpty() || className.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        db.collection("Teachers")
                .document(simpleTeacherId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String teacherId = document.getString("teacherId");


                            Map<String, Object> absenceData = new HashMap<>();
                            absenceData.put("teacherId", teacherId);
                            absenceData.put("date", absenceDate);
                            absenceData.put("time", absenceTime);
                            absenceData.put("room", room);
                            absenceData.put("class", className);
                            absenceData.put("agentId", mAuth.getCurrentUser().getUid());


                            db.collection("Absences")
                                    .add(absenceData)
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(this, "Absence added successfully", Toast.LENGTH_SHORT).show();
                                        clearFields();
                                        navigateToAbsneceList();

                                    })
                                    .addOnFailureListener(e -> Toast.makeText(this, "Error adding absence", Toast.LENGTH_SHORT).show());
                        } else {
                            Toast.makeText(this, "Teacher ID not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Error retrieving teacher data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateToAbsneceList() {

        Intent intent = new Intent(TrackingAgentActivity.this, AdminActivity.class);
        startActivity(intent);
    }

    private void clearFields() {
        teacherIdInput.setText("");
        absenceDateInput.setText("");
        absenceTimeInput.setText("");
        salleInput.setText("");
        classeInput.setText("");
    }
}
