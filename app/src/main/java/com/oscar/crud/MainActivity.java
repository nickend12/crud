package com.oscar.crud;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private EditText editTextId, editTextName, editTextEmail;
    private Button createButton, readButton, updateButton, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        editTextId = findViewById(R.id.editTextId); // Campo para el ID del usuario
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        createButton = findViewById(R.id.createButton);
        readButton = findViewById(R.id.readButton);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);

        createButton.setOnClickListener(view -> createUser());
        readButton.setOnClickListener(view -> readUsers());
        updateButton.setOnClickListener(view -> updateUser());
        deleteButton.setOnClickListener(view -> deleteUser());
    }

    private void createUser() {
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();

        if (!name.isEmpty() && !email.isEmpty()) {
            long result = dbHelper.insertUser(name, email);
            if (result != -1) {
                showToast("Usuario creado con ID: " + result);
                editTextName.setText("");
                editTextEmail.setText("");
                logUsers();  // Llama aquí para ver los usuarios después de crear
            } else {
                showToast("No se pudo crear el usuario.");
            }
        } else {
            showToast("Por favor, completa todos los campos.");
        }
    }

    private void readUsers() {
        Cursor cursor = dbHelper.getUsers();
        StringBuilder users = new StringBuilder();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String email = cursor.getString(2);
                users.append("ID: ").append(id).append(", Nombre: ").append(name).append(", Email: ").append(email).append("\n");
            } while (cursor.moveToNext());
        } else {
            users.append("No hay usuarios en la base de datos.");
        }
        cursor.close();
        showDialog("Usuarios", users.toString());
        logUsers();  // Llama aquí para ver los usuarios en el log
    }

    private void updateUser() {
        String idString = editTextId.getText().toString();
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();

        if (!idString.isEmpty() && !name.isEmpty() && !email.isEmpty()) {
            int id = Integer.parseInt(idString);
            int result = dbHelper.updateUser(id, name, email);
            if (result > 0) {
                showToast("Usuario actualizado.");
                logUsers();  // Llama aquí para ver los usuarios después de actualizar
            } else {
                showToast("No se pudo actualizar el usuario. Verifique el ID.");
            }
        } else {
            showToast("Por favor, completa todos los campos.");
        }
    }

    private void deleteUser() {
        String idString = editTextId.getText().toString();
        if (!idString.isEmpty()) {
            int id = Integer.parseInt(idString);
            int result = dbHelper.deleteUser(id);
            if (result > 0) {
                showToast("Usuario eliminado.");
                logUsers();  // Llama aquí para ver los usuarios después de eliminar
            } else {
                showToast("No se pudo eliminar el usuario. Verifique el ID.");
            }
        } else {
            showToast("Por favor, ingresa el ID del usuario a eliminar.");
        }
    }

    private void logUsers() {
        Cursor cursor = dbHelper.getUsers();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String email = cursor.getString(2);
                Log.d("User", "ID: " + id + ", Name: " + name + ", Email: " + email);
            } while (cursor.moveToNext());
        } else {
            Log.d("User", "No hay usuarios en la base de datos.");
        }
        cursor.close();
    }

    private void showDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
