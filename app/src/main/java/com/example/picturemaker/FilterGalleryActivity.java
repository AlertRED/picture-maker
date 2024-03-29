package com.example.picturemaker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.picturemaker.storage.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FilterGalleryActivity extends AppCompatActivity {

    private Spinner spin_genre;
    private Spinner spin_author;
    private Spinner spin_level;
    private String genre;
    private String level;
    private String author;

    private void RefreshAuthors(Map<String, Integer> authors) {
        List<String> new_list = new ArrayList<>();
        new_list.add(this.getString(R.string.any));
        new_list.addAll(authors.keySet());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new_list);
        this.spin_author.setAdapter(adapter);
        if (this.author != null)
            this.spin_author.setSelection(new_list.indexOf(this.author));
    }

    private void RefreshGenres(Map<String, Integer> genres) {
        List<String> new_list = new ArrayList<>();
        new_list.add(this.getString(R.string.any));
        new_list.addAll(genres.keySet());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new_list);
        this.spin_genre.setAdapter(adapter);
        if (this.genre != null)
            this.spin_genre.setSelection(new_list.indexOf(this.genre));
    }

    private void RefreshLevels(Map<String, Integer> levels) {
        List<String> new_list = new ArrayList<>();
        new_list.add(this.getString(R.string.any));
        new_list.addAll(levels.keySet());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new_list);
        this.spin_level.setAdapter(adapter);
        if (this.level != null)
            this.spin_level.setSelection(new_list.indexOf(this.level));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_gallery);

        Toolbar toolbar = (Toolbar) findViewById(R.id.filter_gallery_toolbar);
        setSupportActionBar(toolbar);
        Button accept_button = (Button) findViewById(R.id.accept_filter);
        accept_button.setOnClickListener(v -> {
            Intent answerIntent = new Intent();
            answerIntent.putExtra("genre", (String) spin_genre.getSelectedItem());
            answerIntent.putExtra("author", (String) spin_author.getSelectedItem());
            answerIntent.putExtra("level", (String) spin_level.getSelectedItem());
            setResult(RESULT_OK, answerIntent);
            finish();
        });
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        toolbar.setNavigationOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        this.spin_genre = (Spinner) findViewById(R.id.spin_filter_genre);
        this.spin_author = (Spinner) findViewById(R.id.spin_filter_act);
        this.spin_level = (Spinner) findViewById(R.id.spin_filter_level);

        this.genre = this.getIntent().getStringExtra("genre");
        this.level = this.getIntent().getStringExtra("level");
        this.author = this.getIntent().getStringExtra("author");

        Storage storage = Storage.getInstance(this);
        storage.GetAuthors(this::RefreshAuthors);
        storage.GetGenres(this::RefreshGenres);
        storage.GetLevels(this::RefreshLevels);
    }
}