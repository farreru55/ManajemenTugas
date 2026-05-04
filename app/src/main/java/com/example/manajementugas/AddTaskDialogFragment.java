package com.example.manajementugas;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskDialogFragment extends DialogFragment {

    private TextInputEditText etTaskName, etCourseName;
    private AutoCompleteTextView actvPriority;
    private MaterialButton btnPickDate, btnSave, btnCancel;
    private TextView tvDialogTitle;
    private Calendar calendar = Calendar.getInstance();
    private TaskViewModel viewModel;
    private String selectedDate = "";
    private Task taskToEdit;

    public static AddTaskDialogFragment newInstance(Task task) {
        AddTaskDialogFragment fragment = new AddTaskDialogFragment();
        fragment.taskToEdit = task;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        tvDialogTitle = view.findViewById(R.id.tv_dialog_title);
        etTaskName = view.findViewById(R.id.et_task_name);
        etCourseName = view.findViewById(R.id.et_course_name);
        actvPriority = view.findViewById(R.id.actv_priority);
        btnPickDate = view.findViewById(R.id.btn_pick_date);
        btnSave = view.findViewById(R.id.btn_save_task);
        btnCancel = view.findViewById(R.id.btn_cancel_task);

        setupPriorityDropdown();
        setupDatePicker();

        if (taskToEdit != null) {
            tvDialogTitle.setText("Edit Tugas");
            etTaskName.setText(taskToEdit.getTitle());
            etCourseName.setText(taskToEdit.getCourse());
            actvPriority.setText(taskToEdit.getPriority(), false);
            selectedDate = taskToEdit.getDeadline();
            btnPickDate.setText(selectedDate);
            // Sync calendar with existing date if possible, for now just keep string
        }

        btnCancel.setOnClickListener(v -> dismiss());
        btnSave.setOnClickListener(v -> saveTask());
    }

    private void setupPriorityDropdown() {
        String[] priorities = {"TINGGI", "SEDANG", "RENDAH"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, priorities);
        actvPriority.setAdapter(adapter);
    }

    private void setupDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        btnPickDate.setOnClickListener(v -> {
            new DatePickerDialog(requireContext(), dateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void updateLabel() {
        String myFormat = "dd MMMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("id", "ID"));
        selectedDate = sdf.format(calendar.getTime());
        btnPickDate.setText(selectedDate);
    }

    private void saveTask() {
        String name = etTaskName.getText().toString().trim();
        String course = etCourseName.getText().toString().trim();
        String priority = actvPriority.getText().toString().trim();

        if (name.isEmpty() || course.isEmpty() || priority.isEmpty() || selectedDate.isEmpty()) {
            Toast.makeText(getContext(), "Mohon lengkapi semua data", Toast.LENGTH_SHORT).show();
            return;
        }

        if (taskToEdit == null) {
            int id = (int) (System.currentTimeMillis() / 1000);
            Task newTask = new Task(id, name, course, priority, selectedDate, false);
            viewModel.addTask(newTask);
        } else {
            taskToEdit.setTitle(name);
            taskToEdit.setCourse(course);
            taskToEdit.setPriority(priority);
            taskToEdit.setDeadline(selectedDate);
            viewModel.updateTask(taskToEdit);
        }
        dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
