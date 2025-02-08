package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView absencesRecyclerView;
    private AbsenceAdapter absenceadapter;

    private Button addButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity);


        db = FirebaseFirestore.getInstance();


        absencesRecyclerView = findViewById(R.id.admin_recycler_view);
        absencesRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        fetchAllAbsences();

        addButton = findViewById(R.id.add_Button);
        addButton.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, TrackingAgentActivity.class);
            startActivity(intent);
        });
    }

    private void fetchAllAbsences() {

        db.collection("Absences")
                .get()
                .addOnSuccessListener(absenceSnapshots -> {
                    List<Absence> absences = new ArrayList<>();

                    for (QueryDocumentSnapshot absenceDoc : absenceSnapshots) {

                        String id = absenceDoc.getId();
                        String teacherId = absenceDoc.getString("teacherId");
                        String className = absenceDoc.getString("class");
                        String room = absenceDoc.getString("room");
                        String time = absenceDoc.getString("time");
                        String date = absenceDoc.getString("date");
                        String agentId = absenceDoc.getString("agentId");


                        Absence absence = new Absence(id,className,date,teacherId,  room, time,agentId);
                        absences.add(absence);
                    }


                    updateRecyclerView(absences);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error fetching absences.", Toast.LENGTH_SHORT).show();
                    Log.e("AdminActivity", "Error fetching absences: " + e.getMessage());
                });
    }
    @SuppressLint("NotifyDataSetChanged")
    private void updateRecyclerView(List<Absence> absences) {
        if (absenceadapter == null) {
            absenceadapter = new AbsenceAdapter(this, absences);
            absencesRecyclerView.setAdapter(absenceadapter);
        } else {
            absenceadapter.notifyDataSetChanged();
        }
    }
}
