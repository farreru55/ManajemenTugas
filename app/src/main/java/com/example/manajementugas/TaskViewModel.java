package com.example.manajementugas;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private final DatabaseHelper dbHelper;
    private final MutableLiveData<List<Task>> taskList = new MutableLiveData<>();

    public TaskViewModel(@NonNull Application application) {
        super(application);
        dbHelper = new DatabaseHelper(application);
        loadTasks();
    }

    public LiveData<List<Task>> getTasks() {
        return taskList;
    }

    private void loadTasks() {
        taskList.setValue(dbHelper.getAllTasks());
    }

    public void addTask(Task task) {
        dbHelper.addTask(task);
        loadTasks();
    }

    public void removeTask(Task task) {
        dbHelper.deleteTask(task);
        loadTasks();
    }

    public void updateTask(Task task) {
        dbHelper.updateTask(task);
        loadTasks();
    }
}
