package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AddNoteActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText textEditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        Button backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        titleEditText = findViewById(R.id.noteTitle);
        textEditText = findViewById(R.id.noteText);
        saveButton = findViewById(R.id.saveNoteButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEditText.getText().toString();
                String text = textEditText.getText().toString();

                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(text)) {
                    Note note = new Note(title, text);

                    List<Note> noteList = getNoteListFromStorage();
                    noteList.add(note);
                    saveNoteListToStorage(noteList);

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("Заголовок", title);
                    resultIntent.putExtra("Текст", text);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(AddNoteActivity.this, "Напишите заголовок и текст", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private static final String NOTE_PREFS_NAME = "notePrefs";
    private static final String NOTE_LIST_KEY = "noteList";


    private List<Note> getNoteListFromStorage() {
        SharedPreferences sharedPreferences = getSharedPreferences(NOTE_PREFS_NAME, Context.MODE_PRIVATE);
        String noteListJson = sharedPreferences.getString(NOTE_LIST_KEY, null);
        if (noteListJson != null) {
            Type listType = new TypeToken<List<Note>>() {}.getType();
            return new Gson().fromJson(noteListJson, listType);
        } else {
            return new ArrayList<>();
        }
    }

    private void saveNoteListToStorage(List<Note> noteList) {
        SharedPreferences sharedPreferences = getSharedPreferences(NOTE_PREFS_NAME, Context.MODE_PRIVATE);
        String noteListJson = new Gson().toJson(noteList);
        sharedPreferences.edit().putString(NOTE_LIST_KEY, noteListJson).apply();
    }
}