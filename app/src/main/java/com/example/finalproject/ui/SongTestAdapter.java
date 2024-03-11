package com.example.finalproject.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.finalproject.R;
import com.example.finalproject.modal.SongTestGridView;

import java.util.ArrayList;

public class SongTestAdapter extends ArrayAdapter<SongTestGridView> {
    public SongTestAdapter(@NonNull Context context, ArrayList<SongTestGridView> songTestGridViews) {
        super(context, 0, songTestGridViews);
    }

    @NonNull
    @Override
    public View getView(int position,@NonNull View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.grid_song_card_item, parent, false);
        }
        SongTestGridView currentSong = getItem(position);
        ImageButton imageButton = listItemView.findViewById(R.id.ib_music);
        TextView songTitle = listItemView.findViewById(R.id.tv_SongName);
        TextView songUrl = listItemView.findViewById(R.id.tv_url);

        if (currentSong != null) {
            imageButton.setImageResource(currentSong.getImageThumbnail());
            songTitle.setText(currentSong.getTitle());
            songUrl.setText(currentSong.getSongUrl());
        }
        return listItemView;
    }
}
