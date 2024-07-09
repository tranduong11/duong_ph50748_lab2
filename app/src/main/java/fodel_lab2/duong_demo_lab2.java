package fodel_lab2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.duong_lab2.R;

import java.util.List;

public class duong_demo_lab2 extends AppCompatActivity {
    private EditText etTitle, etContent, etDate, etType;
    private Button btnAdd;
    private RecyclerView recyclerView;
    private TodoAdapter adapter;
    private TodoDAO todoDAO;
    private List<Todo> todoList;
    private Todo currentEditingTodo = null;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft21_main2);
        init();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentEditingTodo == null) {
                    addTodo();
                } else {
                    updateTodo();
                }
            }
        });

        adapter.setOnItemClickListener(
                new TodoAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                deleteTodo(position);
            }

            @Override
            public void onEditClick(int position) {
                editTodo(position);
            }

            @Override
            public void onStatusChange(int position, boolean isDone) {
                updateTodoStatus(position, isDone);
            }
        });
    }

    private void addTodo() {
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();
        String date = etDate.getText().toString();
        String type = etType.getText().toString();

        Todo todo = new Todo(0, title, content, date,
                type, 0);
        todoDAO.addTodo(todo);
        todoList.add(0, todo);
        adapter.notifyItemInserted(0);
        recyclerView.scrollToPosition(0);
        clearFields();
    }
    private void updateTodo() {
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();
        String date = etDate.getText().toString();
        String type = etType.getText().toString();
        currentEditingTodo.setTitle(title);
        currentEditingTodo.setContent(content);
        currentEditingTodo.setDate(date);
        currentEditingTodo.setType(type);
        todoDAO.updateTodo(currentEditingTodo);
        int position = todoList.indexOf(currentEditingTodo);
        adapter.notifyItemChanged(position);

        currentEditingTodo = null;
        btnAdd.setText("Add");
        clearFields();
    }
    private void editTodo(int position) {
        currentEditingTodo = todoList.get(position);

        etTitle.setText(currentEditingTodo.getTitle());
        etContent.setText(currentEditingTodo.getContent());
        etDate.setText(currentEditingTodo.getDate());
        etType.setText(currentEditingTodo.getType());

        btnAdd.setText("Update");
    }
    private void clearFields() {
        etTitle.setText("");
        etContent.setText("");
        etDate.setText("");
        etType.setText("");
    }

    private void deleteTodo(int position) {
        Todo todo = todoList.get(position);
        todoDAO.deleteTodo(todo.getId());
        todoList.remove(position);
        adapter.notifyItemRemoved(position);
    }


    private void updateTodoStatus(int position, boolean isDone) {
        Todo todo = todoList.get(position);
        todo.setStatus(isDone ? 1 : 0);
        todoDAO.updateTodoStatus(todo.getId(), todo.getStatus());

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyItemChanged(position);
            }
        });
        Toast.makeText(this, "Đã update status thành công", Toast.LENGTH_SHORT).show();
    }

    public void init(){
        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        etDate = findViewById(R.id.etDate);
        etType = findViewById(R.id.etType);
        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.draft21_recyclerView);

        todoDAO = new TodoDAO(this);
        todoList = todoDAO.getAllTodos();

        adapter = new TodoAdapter(todoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                duong_demo_lab2.this));
        recyclerView.setAdapter(adapter);
    }
}