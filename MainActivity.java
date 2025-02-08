package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        emailEditText = findViewById(R.id.Email);
        passwordEditText = findViewById(R.id.Password);
        Button submitButton = findViewById(R.id.Submit);


        submitButton.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();


        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Log.d("MainActivity", "Login successful. User ID: " + user.getUid());

                            fetchUserRole(user.getUid());
                        }
                    } else {
                        Log.e("MainActivity", "Authentication failed: ", task.getException());
                        Toast.makeText(this, "Authentication failed. Try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchUserRole(String userId) {

        DocumentReference userDoc = db.collection("Users").document(userId);

        userDoc.get().addOnSuccessListener(document -> {
            if (document.exists()) {

                Log.d("MainActivity", "User document data: " + document.getData());


                String role = document.getString("role");
                if (role != null) {
                    handleRoleNavigation(role);
                } else {
                    Toast.makeText(this, "User role not found in Firestore.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("MainActivity", "User document does not exist.");
                Toast.makeText(this, "User document not found.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Log.e("MainActivity", "Failed to fetch user role: ", e);
            Toast.makeText(this, "Error fetching user data.", Toast.LENGTH_SHORT).show();
        });
    }

    private void handleRoleNavigation(String role) {
        if (role == null) {
            Toast.makeText(this, "Role is null. Check your Firestore database.", Toast.LENGTH_SHORT).show();
            return;
        }


        switch (role) {
            case "tracking agent":
                navigateToTrackingAgentActivity();
                break;
            case "teacher":
                navigateToTeacherAbsenceActivity();
                break;
            case "admin":
                navigateToDashboard();
                break;

            default:
                Toast.makeText(this, "Role not recognized or not allowed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToTrackingAgentActivity() {
        Intent intent = new Intent(MainActivity.this, AdminActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToTeacherAbsenceActivity() {
        Intent intent = new Intent(MainActivity.this, TeacherAbsenceActivity.class);
        startActivity(intent);
        finish();
    }
    private void navigateToDashboard() {
        Intent intent = new Intent(MainActivity.this, Dashboard.class);
        startActivity(intent);
        finish();
    }

}