package com.example.manajementugas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListFragment extends Fragment {

    private RecyclerView rvTasks;
    private TaskAdapter adapter;
    private Spinner spinnerSort;
    private MaterialButton btnAddTask;
    private TaskViewModel viewModel;
    private List<Task> currentList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        rvTasks = view.findViewById(R.id.rv_tasks);
        spinnerSort = view.findViewById(R.id.spinner_sort);
        btnAddTask = view.findViewById(R.id.btn_add_task);

        viewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        setupRecyclerView();
        setupSpinner();

        viewModel.getTasks().observe(getViewLifecycleOwner(), tasks -> {
            currentList = new ArrayList<>(tasks);
            applySorting();
        });

        btnAddTask.setOnClickListener(v -> showAddTaskDialog(null));

        return view;
    }

    private void setupSpinner() {
        String[] sortOptions = {"Terbaru", "Prioritas Tinggi", "Deadline Terdekat"};
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, sortOptions);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(sortAdapter);

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applySorting();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void applySorting() {
        int position = spinnerSort.getSelectedItemPosition();
        switch (position) {
            case 0: // Terbaru (berdasarkan ID/Timestamp)
                Collections.sort(currentList, (t1, t2) -> Integer.compare(t2.getId(), t1.getId()));
                break;
            case 1: // Prioritas Tinggi
                Collections.sort(currentList, (t1, t2) -> {
                    int p1 = getPriorityValue(t1.getPriority());
                    int p2 = getPriorityValue(t2.getPriority());
                    return Integer.compare(p2, p1);
                });
                break;
            case 2: // Deadline Terdekat
                // Simple string compare for this mockup (ideally parse to Date)
                Collections.sort(currentList, (t1, t2) -> t1.getDeadline().compareTo(t2.getDeadline()));
                break;
        }
        adapter.updateData(currentList);
    }

    private int getPriorityValue(String priority) {
        if ("TINGGI".equalsIgnoreCase(priority)) return 3;
        if ("SEDANG".equalsIgnoreCase(priority)) return 2;
        return 1; // RENDAH
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
        rvTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTasks.setAdapter(adapter);
    }

    private void showAddTaskDialog(Task taskToEdit) {
        AddTaskDialogFragment dialog = AddTaskDialogFragment.newInstance(taskToEdit);
        dialog.show(getParentFragmentManager(), "AddTaskDialog");
    }
}
