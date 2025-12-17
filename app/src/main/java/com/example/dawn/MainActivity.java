package com.example.dawn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dawn.dto.Task.TaskItemDTO;
import com.example.dawn.network.RetrofitClient;
import com.example.dawn.utils.CommonUtils;
import com.example.dawn.utils.MyLogger;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    RecyclerView taskRecycler;
    TaskAdapter adapter;

    View addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        taskRecycler = findViewById(R.id.taskRecycler);
        taskRecycler.setAdapter(new TaskAdapter(new ArrayList<>(),
                MainActivity.this::onClickEditTask));
        taskRecycler.setLayoutManager(
                new androidx.recyclerview.widget.LinearLayoutManager(this)
        );

        addButton = findViewById(R.id.addButton);

        addButton.setOnClickListener(v ->
                {
                    goToAddTaskActivity();
                }
        );
        CommonUtils.showLoading();
        loadTaskList();

    }

    private void loadTaskList() {
        RetrofitClient.getInstance().getTaskApi().list()
                .enqueue(new Callback<List<TaskItemDTO>>() {
            @Override
            public void onResponse(Call<List<TaskItemDTO>> call, Response<List<TaskItemDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new TaskAdapter(response.body(),
                            MainActivity.this::onClickEditTask);
                    taskRecycler.setAdapter(adapter);
                    CommonUtils.hideLoading();
                }
            }

            @Override
            public void onFailure(Call<List<TaskItemDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void onClickEditTask(TaskItemDTO item) {
        //MyLogger.toast(MainActivity.this, "Зміна задачі");
        Intent intent = new Intent(this, EditTaskActivity.class);
        intent.putExtra("task_id", item.getId());
        intent.putExtra("task_name", item.getName());
        intent.putExtra("task_image", item.getImage());
        this.startActivity(intent);
    }

}
