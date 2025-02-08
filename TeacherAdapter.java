package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.TeacherViewHolder> {

    private Context context;
    private List<Teacher> teacherList;
    private FirebaseFirestore db;

    public TeacherAdapter(Context context, List<Teacher> teacherList) {
        this.context = context;
        this.teacherList = teacherList;
        this.db = FirebaseFirestore.getInstance();

    }

    @NonNull
    @Override
    public TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.teacher_item, parent, false);
        return new TeacherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherViewHolder holder, int position) {
        Teacher teacher = teacherList.get(position);
        holder.teacherNameTextView.setText(teacher.getName());
        holder.classNameTextView.setText(teacher.getClassName());
        holder.EmailTextView.setText(teacher.getEmail());

        holder.deleteButtonteacher.setOnClickListener(view -> {

            db.collection("Teachers").document(teacher.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {

                        teacherList.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Teacher deleted successfully.", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Error deleting teacher: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

    }

    @Override
    public int getItemCount() {
        return teacherList != null ? teacherList.size() :0;
    }

    public static class TeacherViewHolder extends RecyclerView.ViewHolder {
        TextView teacherNameTextView;
        TextView classNameTextView;

        TextView EmailTextView;
        ImageButton deleteButtonteacher;


        public TeacherViewHolder(View itemView) {
            super(itemView);
            teacherNameTextView = itemView.findViewById(R.id.teacher_name_text_view);
            classNameTextView = itemView.findViewById(R.id.class_name_text_view);
            EmailTextView = itemView.findViewById(R.id.email_text_view);
            deleteButtonteacher = itemView.findViewById(R.id.delete_teacher_button);


        }
    }
}
