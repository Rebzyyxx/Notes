package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Note> notes = new ArrayList<>();
    private NoteAdapter noteAdapter;
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataManager = new DataManager(this);
        notes = dataManager.loadNotes();
        noteAdapter = new NoteAdapter(this, notes);

        ListView noteListView = findViewById(R.id.list);
        noteListView.setAdapter(noteAdapter);

        Button addNoteButton = findViewById(R.id.addNote);
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note note = (Note) parent.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, NoteDetailsActivity.class);
                intent.putExtra("Заголовок", note.getTitle());
                intent.putExtra("Текст", note.getDescription());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                String title = data.getStringExtra("Заголовок");
                String text = data.getStringExtra("Текст");

                if (title != null && text != null) {
                    Note note = new Note();
                    note.setTitle(title);
                    note.setText(text);

                    if (notes == null) {
                        notes = new ArrayList<>();
                    }

                    notes.add(note);
                    noteAdapter.notifyDataSetChanged();
                    dataManager.saveNotes(notes);
                } else {
                    Toast.makeText(this, "Получены неверные данные", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}