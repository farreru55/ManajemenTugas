package com.example.manajementugas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {

    private CalendarView calendarView;
    private TextView tvSelectedDate, tvTaskCount, tvEmptyState;
    private RecyclerView rvCalendarTasks;
    private TaskAdapter adapter;
    private TaskViewModel viewModel;
    private String currentDateStr;
    private List<Task> allTasks = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarView = view.findViewById(R.id.calendar_view);
        tvSelectedDate = view.findViewById(R.id.tv_selected_date);
        tvTaskCount = view.findViewById(R.id.tv_task_count);
        tvEmptyState = view.findViewById(R.id.tv_empty_state);
        rvCalendarTasks = view.findViewById(R.id.rv_calendar_tasks);

        viewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        setupRecyclerView();

        viewModel.getTasks().observe(getViewLifecycleOwner(), tasks -> {
            this.allTasks = tasks;
            filterTasksByDate(currentDateStr);
        });

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            currentDateStr = new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID")).format(calendar.getTime());
            tvSelectedDate.setText("Tugas pada " + currentDateStr);
            filterTasksByDate(currentDateStr);
        });

        // Set initial date
        long date = calendarView.getDate();
        currentDateStr = new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID")).format(date);
        tvSelectedDate.setText("Tugas pada " + currentDateStr);

        return view;
    }

    private void setupRecyclerView() {
        adapter = new TaskAdapter(new ArrayList<>(), new TaskAdapter.OnTaskActionListener() {
            @Override
            public void onTaskDelete(Task task) {
                viewModel.removeTask(task);
            }

            @Override
            public void onTaskEdit(Task task) {
                showAddTaskDialog(task);
            }

            @Override
            public void onTaskStatusChanged(Task task) {
                viewModel.updateTask(task);
            }
        });
        rvCalendarTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCalendarTasks.setAdapter(adapter);
    }

    private void showAddTaskDialog(Task taskToEdit) {
        AddTaskDialogFragment dialog = AddTaskDialogFragment.newInstance(taskToEdit);
        dialog.show(getParentFragmentManager(), "AddTaskDialog");
    }

    private void filterTasksByDate(String date) {
        if (date == null) return;
        List<Task> filteredTasks = new ArrayList<>();
        for (Task task : allTasks) {
            if (date.equals(task.getDeadline())) {
                filteredTasks.add(task);
            }
        }

        if (filteredTasks.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            rvCalendarTasks.setVisibility(View.GONE);
            tvTaskCount.setText("0 tugas");
        } else {
            tvEmptyState.setVisibility(View.GONE);
            rvCalendarTasks.setVisibility(View.VISIBLE);
            tvTaskCount.setText(filteredTasks.size() + " tugas");
        }
        adapter.updateData(filteredTasks);
    }
}
