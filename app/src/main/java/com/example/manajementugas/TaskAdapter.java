package com.example.manajementugas;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private Context context;
    private OnTaskActionListener listener;

    public interface OnTaskActionListener {
        void onTaskDelete(Task task);
        void onTaskEdit(Task task);
        void onTaskStatusChanged(Task task);
    }

    public TaskAdapter(List<Task> taskList, OnTaskActionListener listener) {
        this.taskList = taskList;
        this.listener = listener;
    }

    public void updateData(List<Task> newTasks) {
        this.taskList = newTasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task currentTask = taskList.get(position);
        holder.tvTitle.setText(currentTask.getTitle());
        holder.tvCourse.setText(currentTask.getCourse());
        holder.tvPriority.setText(currentTask.getPriority());
        holder.tvDeadline.setText(currentTask.getDeadline());
        holder.rbStatus.setChecked(currentTask.isCompleted());

        updateTaskVisuals(holder, currentTask.isCompleted());

        // Handle Priority Colors
        int bgColor, textColor;
        if ("TINGGI".equalsIgnoreCase(currentTask.getPriority())) {
            bgColor = R.color.bg_high_priority;
            textColor = R.color.text_high_priority;
        } else if ("SEDANG".equalsIgnoreCase(currentTask.getPriority())) {
            bgColor = R.color.bg_medium_priority;
            textColor = R.color.text_medium_priority;
        } else {
            bgColor = R.color.bg_low_priority;
            textColor = R.color.text_low_priority;
        }

        holder.tvPriority.setTextColor(ContextCompat.getColor(context, textColor));
        holder.tvPriority.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, bgColor)));

        holder.rbStatus.setOnClickListener(v -> {
            boolean newStatus = !currentTask.isCompleted();
            currentTask.setCompleted(newStatus);
            holder.rbStatus.setChecked(newStatus);
            updateTaskVisuals(holder, newStatus);
            if (listener != null) listener.onTaskStatusChanged(currentTask);
        });

        holder.ivDelete.setOnClickListener(v -> {
            if (listener != null) listener.onTaskDelete(currentTask);
        });

        holder.ivEdit.setOnClickListener(v -> {
            if (listener != null) listener.onTaskEdit(currentTask);
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    private void updateTaskVisuals(TaskViewHolder holder, boolean isCompleted) {
        if (isCompleted) {
            holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.cardTask.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_completed));
        } else {
            holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.cardTask.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCourse, tvPriority, tvDeadline;
        RadioButton rbStatus;
        ImageView ivEdit, ivDelete;
        MaterialCardView cardTask;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_task_title);
            tvCourse = itemView.findViewById(R.id.tv_course);
            tvPriority = itemView.findViewById(R.id.tv_priority);
            tvDeadline = itemView.findViewById(R.id.tv_deadline);
            rbStatus = itemView.findViewById(R.id.rb_task_status);
            ivEdit = itemView.findViewById(R.id.iv_edit);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            cardTask = itemView.findViewById(R.id.card_task);
        }
    }
}
