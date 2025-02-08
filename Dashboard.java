package com.example.myapplication;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;

public class Dashboard extends AppCompatActivity {
    private Button viewAbsence;
    private Button viewTeacher;
    private Button viewStatistics;
    private Button viewSchedules;
    private ActivityResultLauncher<String> filePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);


        viewAbsence = findViewById(R.id.view_absence);
        viewAbsence.setOnClickListener(view -> {
            Intent intent = new Intent(Dashboard.this, AdminActivity.class);
            startActivity(intent);
        });


        viewTeacher = findViewById(R.id.view_teacher);
        viewTeacher.setOnClickListener(view -> {
            Intent intent = new Intent(Dashboard.this, AgentTeacherListActivity.class);
            startActivity(intent);
        });


        viewStatistics = findViewById(R.id.view_statistics);
        viewStatistics.setOnClickListener(view -> {
            Intent intent = new Intent(Dashboard.this, StatisticsActivity.class);
            startActivity(intent);
        });


        viewSchedules = findViewById(R.id.view_schedules);
        viewSchedules.setOnClickListener(view -> {
            String filePath = getExternalFilesDir(null) + "/schedule.pdf";
            CreatePDF createPDF = new CreatePDF();
            createPDF.createSchedulePDF(this);

            PDFExtractor pdfExtractor = new PDFExtractor();
            pdfExtractor.extractDataFromPDF(filePath);

            openPDF(filePath);
        });


        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    String pdfFilePath = result.getPath();
                    if (pdfFilePath != null) {
                        PDFExtractor pdfExtractor = new PDFExtractor();
                        pdfExtractor.extractDataFromPDF(pdfFilePath);

                        Toast.makeText(Dashboard.this, "Timetable extracted and uploaded to Firestore", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Dashboard.this, "No file selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openPDF(String filePath) {
        File pdfFile = new File(filePath);
        Uri pdfUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", pdfFile);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No PDF viewer found on your device", Toast.LENGTH_SHORT).show();
        }
    }
}
