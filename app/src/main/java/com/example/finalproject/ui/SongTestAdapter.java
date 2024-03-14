package com.example.finalproject.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.finalproject.R;
import com.example.finalproject.SongsActivity;
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

        // Set the onClickListener for the ImageButton
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSong();
                Toast.makeText(getContext(), "ImageButton clicked", Toast.LENGTH_SHORT).show();
            }
        });
        return listItemView;
    }

    public void playSong() {
        Dialog dialog = new Dialog(this.getContext());
        dialog.setContentView(R.layout.dialogue_video);

        Button btnClose = dialog.findViewById(R.id.btnClose);
        dialog.show();
        VideoView videoView = dialog.findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(this.getContext());
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        Uri uri = Uri.parse("https://media.geeksforgeeks.org/wp-content/uploads/20201217192146/Screenrecorder-2020-12-17-19-17-36-828.mp4?_=1");
        videoView.setVideoURI(uri);
        videoView.start();

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
