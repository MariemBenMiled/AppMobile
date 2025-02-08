
package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;

import com.google.firebase.firestore.FirebaseFirestore;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class AbsenceAdapterForTeacher extends RecyclerView.Adapter<AbsenceAdapterForTeacher.AbsenceViewHolder> {

    private List<Absence> absenceList;
    private Context context;
    private FirebaseFirestore db;



    public AbsenceAdapterForTeacher(Context context, List<Absence> absenceList) {
        this.context = context;
        this.absenceList = absenceList != null ? absenceList : new ArrayList<>();
        this.db = FirebaseFirestore.getInstance();

    }

    @NonNull
    @Override
    public AbsenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.teacher_absence_item, parent, false);
        return new AbsenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AbsenceViewHolder holder, int position) {

        Absence absence = absenceList.get(position);


        if (absence != null) {
            holder.dateTextView.setText(absence.getDate() != null ? absence.getDate() : "N/A");
            holder.timeTextView.setText(absence.getTime() != null ? absence.getTime() : "N/A");
            holder.roomTextView.setText(absence.getRoom() != null ? absence.getRoom() : "N/A");

        }


    }

    @Override
    public int getItemCount() {
        return absenceList != null ? absenceList.size() : 0;
    }


    public static class AbsenceViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, timeTextView, roomTextView;

        public AbsenceViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTextView = itemView.findViewById(R.id.date_text_view);
            timeTextView = itemView.findViewById(R.id.time_text_view);
            roomTextView = itemView.findViewById(R.id.room_text_view);


        }
    }
}
