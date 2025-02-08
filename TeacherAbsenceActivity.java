package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class TeacherAbsenceActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private RecyclerView absencesRecyclerView;
    private AbsenceAdapterForTeacher absenceAdapter;
    private TextView teacherNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_absence);


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        teacherNameTextView = findViewById(R.id.teacher_name_text_view);
        absencesRecyclerView = findViewById(R.id.absence_recycler_view);
        absencesRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        fetchTeacherIdAndAbsences();
    }

    private void fetchTeacherIdAndAbsences() {
        String userEmail = mAuth.getCurrentUser().getEmail();
        Log.e("TeacherAbsenceActivity", "Teacher email "+userEmail);

        db.collection("Teachers")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {

                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                        String teacherId = document.getString("teacherId");
                        Log.e("TeacherAbsenceActivity", "Teacherid "+teacherId);
                        String teacherName = document.getString("name");
                        Log.e("TeacherAbsenceActivity", "Teachername "+teacherName);

                        if (teacherId != null && teacherName != null) {

                            teacherNameTextView.setText(teacherName);


                            fetchTeacherAbsences(teacherId);
                        } else {
                            Log.e("TeacherAbsenceActivity", "TeacherId or TeacherName is null");
                            Toast.makeText(this, "Teacher data not found.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("TeacherAbsenceActivity", "Teacher document not found.");
                        Toast.makeText(this, "Teacher data not found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("TeacherAbsenceActivity", "Error fetching teacher data: ", e);
                    Toast.makeText(this, "Failed to fetch teacher data.", Toast.LENGTH_SHORT).show();
                });
    }


    private void fetchTeacherAbsences(@NonNull String teacherId) {

        db.collection("Absences")
                .whereEqualTo("teacherId", teacherId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<Absence> absences = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            Absence absence = document.toObject(Absence.class);
                            if (absence != null) {
                                absences.add(absence);
                            }
                        }

                        if (absences.isEmpty()) {
                            Toast.makeText(this, "No absences found.", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("TeacherAbsenceActivity", "Fetched absences: " + absences.size());
                        }


                        updateRecyclerView(absences);
                    } else {
                        Log.e("TeacherAbsenceActivity", "Error fetching absences: ", task.getException());
                        Toast.makeText(this, "Error fetching absences.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("TeacherAbsenceActivity", "Error querying absences: ", e);
                    Toast.makeText(this, "Failed to fetch absences.", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateRecyclerView(List<Absence> absences) {

        if (absenceAdapter == null) {
            absenceAdapter = new AbsenceAdapterForTeacher(this, absences);

            absencesRecyclerView.setAdapter(absenceAdapter);
        } else {
            absenceAdapter.notifyDataSetChanged();
        }
    }
}
