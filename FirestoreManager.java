package com.example.myapplication;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class FirestoreManager {
    private FirebaseFirestore db;

    public FirestoreManager() {
        db = FirebaseFirestore.getInstance();
    }


    public void storeTimetableToFirestore(String[][] schedule) {
        if (schedule == null || schedule.length == 0) {
            System.err.println("Schedule is empty or null. Cannot store to Firestore.");
            return;
        }


        Map<String, Object> timetableData = new HashMap<>();


        for (int i = 0; i < schedule.length; i++) {
            Map<String, String> timeSlot = new HashMap<>();
            timeSlot.put("Time", schedule[i][0]); // First column is the time


            timeSlot.put("Monday", schedule[i][1]);
            timeSlot.put("Tuesday", schedule[i][2]);
            timeSlot.put("Wednesday", schedule[i][3]);
            timeSlot.put("Thursday", schedule[i][4]);
            timeSlot.put("Friday", schedule[i][5]);


            timetableData.put("Slot_" + (i + 1), timeSlot);
        }


        db.collection("Timetables")
                .add(timetableData)
                .addOnSuccessListener(documentReference -> {
                    System.out.println("Timetable successfully added to Firestore with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    System.err.println("Error adding timetable to Firestore: " + e.getMessage());
                });
    }
}
