package com.example.finalproject;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.VideoView;

import com.example.finalproject.modal.SongTestGridView;
import com.example.finalproject.ui.SongTestAdapter;

import java.util.ArrayList;

public class SongsActivity extends AppCompatActivity {
    GridView gridView;
    public static boolean PLAY = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);

        gridView = findViewById(R.id.gridViewSong);

        // Create an ArrayList of SongTestGridView objects
        ArrayList<SongTestGridView> songTestGridViews = new ArrayList<>();
        songTestGridViews.add(new SongTestGridView("Song 1", R.drawable.song_sample, "https://www.youtube.com/"));
        songTestGridViews.add(new SongTestGridView("Song 2", R.drawable.song_sample, "https://www.youtube.com/"));
        songTestGridViews.add(new SongTestGridView("Song 3", R.drawable.song_sample, "https://www.youtube.com/"));
        songTestGridViews.add(new SongTestGridView("Song 4", R.drawable.song_sample, "https://www.youtube.com/"));
        songTestGridViews.add(new SongTestGridView("Song 5", R.drawable.song_sample, "https://www.youtube.com/"));
        songTestGridViews.add(new SongTestGridView("Song 6", R.drawable.song_sample, "https://www.youtube.com/"));
        songTestGridViews.add(new SongTestGridView("Song 7", R.drawable.song_sample, "https://www.youtube.com/"));
        songTestGridViews.add(new SongTestGridView("Song 8", R.drawable.song_sample, "https://www.youtube.com/"));
        songTestGridViews.add(new SongTestGridView("Song 9", R.drawable.song_sample, "https://www.youtube.com/"));
        songTestGridViews.add(new SongTestGridView("Song 10", R.drawable.song_sample, "https://www.youtube.com/"));

        SongTestAdapter SongAdapter = new SongTestAdapter(this, songTestGridViews);
        gridView.setAdapter(SongAdapter);

    }
}