
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

public class AbsenceAdapter extends RecyclerView.Adapter<AbsenceAdapter.AbsenceViewHolder> {

    private List<Absence> absenceList;
    private Context context;
    private FirebaseFirestore db;



    public AbsenceAdapter(Context context, List<Absence> absenceList) {
        this.context = context;
        this.absenceList = absenceList != null ? absenceList : new ArrayList<>();
        this.db = FirebaseFirestore.getInstance();

    }

    @NonNull
    @Override
    public AbsenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.absence_item, parent, false);
        return new AbsenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AbsenceViewHolder holder, int position) {

        Absence absence = absenceList.get(position);


        if (absence != null) {
            holder.dateTextView.setText(absence.getDate() != null ? absence.getDate() : "N/A");
            holder.timeTextView.setText(absence.getTime() != null ? absence.getTime() : "N/A");
            holder.roomTextView.setText(absence.getRoom() != null ? absence.getRoom() : "N/A");
            holder.classNameTextView.setText(absence.getClassName() != null ? absence.getClassName() : "N/A");
        }

        holder.deleteButton.setOnClickListener(view -> {

            db.collection("Absences").document(absence.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {

                        absenceList.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Absence deleted successfully.", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Error deleting absence: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    public int getItemCount() {
        return absenceList != null ? absenceList.size() : 0;
    }


    public static class AbsenceViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, timeTextView, roomTextView, classNameTextView;
        ImageButton deleteButton;
        public AbsenceViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTextView = itemView.findViewById(R.id.date_text_view);
            timeTextView = itemView.findViewById(R.id.time_text_view);
            roomTextView = itemView.findViewById(R.id.room_text_view);
            classNameTextView = itemView.findViewById(R.id.class_name_text_view);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
