package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_activity);


        db = FirebaseFirestore.getInstance();


        TextView statisticsTitle = findViewById(R.id.statistics_title);
        TextView statisticsSummary = findViewById(R.id.statistics_summary);
        barChart = findViewById(R.id.statistics_bar_chart);


        fetchStatistics(statisticsSummary);
    }

    private void fetchStatistics(TextView statisticsSummary) {
        db.collection("Absences")
                .get()
                .addOnSuccessListener(absenceSnapshots -> {
                    Map<String, Integer> absencesByTeacher = new HashMap<>();


                    for (QueryDocumentSnapshot absenceDoc : absenceSnapshots) {
                        String teacherId = absenceDoc.getString("teacherId");

                        absencesByTeacher.put(
                                teacherId,
                                absencesByTeacher.getOrDefault(teacherId, 0) + 1
                        );
                    }


                    displayStatistics(absencesByTeacher, statisticsSummary);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error fetching statistics.", Toast.LENGTH_SHORT).show();
                    Log.e("StatisticsActivity", "Error fetching statistics: " + e.getMessage());
                });
    }

    private void displayStatistics(Map<String, Integer> absencesByTeacher, TextView statisticsSummary) {
        StringBuilder statsBuilder = new StringBuilder();

        statsBuilder.append("Absences by Teacher (Teacher ID):\n\n");
        List<BarEntry> barEntries = new ArrayList<>();
        int index = 0;


        for (Map.Entry<String, Integer> entry : absencesByTeacher.entrySet()) {
            String teacherId = entry.getKey();
            int absences = entry.getValue();

            statsBuilder.append("Teacher ID: ").append(teacherId)
                    .append(" - Absences: ").append(absences)
                    .append("\n");


            barEntries.add(new BarEntry(index, absences));
            index++;
        }


        statisticsSummary.setText(statsBuilder.toString());


        BarDataSet barDataSet = new BarDataSet(barEntries, "Absences by Teacher ID");
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.invalidate();
    }
}
