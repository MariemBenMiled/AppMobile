package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class AgentTeacherListActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private RecyclerView teachersRecyclerView;
    private TeacherAdapter teacherAdapter;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_teacher_list);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        teachersRecyclerView = findViewById(R.id.teachers_recycler_view);
        teachersRecyclerView.setLayoutManager(new LinearLayoutManager(this));




        fetchAllTeachers();

        addButton = findViewById(R.id.add_teacher);
        addButton.setOnClickListener(view -> {
            Intent intent = new Intent(AgentTeacherListActivity.this, AddteacherActivity.class);
            startActivity(intent);
        });
    }


        private void fetchAllTeachers() {

            db.collection("Teachers")
                    .get()
                    .addOnSuccessListener(teacherSnapshots -> {
                        List<Teacher> Teachers = new ArrayList<>();

                        for (QueryDocumentSnapshot teacherDoc : teacherSnapshots) {
                            String id = teacherDoc.getId();
                            String teacherId = teacherDoc.getString("teacherId");
                            String className = teacherDoc.getString("classname");
                            String email = teacherDoc.getString("email");
                            String name = teacherDoc.getString("name");




                            Teacher teacher = new Teacher(id,className,name,teacherId,email );
                            Teachers.add(teacher);
                        }


                        updateRecyclerView(Teachers);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error fetching absences.", Toast.LENGTH_SHORT).show();
                        Log.e("AdminActivity", "Error fetching absences: " + e.getMessage());
                    });
        }

    private void updateRecyclerView(List<Teacher> Teachers) {
        if (teacherAdapter == null) {
            teacherAdapter = new TeacherAdapter(this, Teachers);
            teachersRecyclerView.setAdapter(teacherAdapter);
        } else {
            teacherAdapter.notifyDataSetChanged();
        }
    }
}
