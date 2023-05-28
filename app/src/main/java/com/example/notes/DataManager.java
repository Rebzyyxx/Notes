package com.example.notes;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class DataManager {

    private static final String PREFS_NAME = "AppPrefs";
    private static final String KEY_NOTES = "Notes";

    private SharedPreferences sharedPreferences;

    public DataManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveNotes(List<Note> notes) {
        Gson gson = new Gson();
        String notesJson = gson.toJson(notes);
        sharedPreferences.edit().putString(KEY_NOTES, notesJson).commit();
    }

    public List<Note> loadNotes() {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Note>>() {}.getType();
        String notesJson = sharedPreferences.getString(KEY_NOTES, gson.toJson(Collections.emptyList()));
        if (notesJson != null) {
            return gson.fromJson(notesJson, type);
        } else {
            return Collections.emptyList();
        }
    }
}
