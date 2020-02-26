package com.example.andriod.radioplayer;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
;


public class MainActivity extends AppCompatActivity {

    private PlayerView playerView;
    private SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private long playbackPosition;
    private int currentWindow;
    private ImageButton imageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playerView = findViewById(R.id.audio_view);
        imageButton = findViewById(R.id.playTrigger);

    }



    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            if(player == null) {
                initializePlayer();
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (player == null) {
            initializePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
//        if (Util.SDK_INT > 23) {
//            releasePlayer();
//        }
    }

    @Override
    public void onDestroy() {
        releasePlayer();
        super.onDestroy();
    }

    public void playStopButton(View v) {
        if (player == null) {
            initializePlayer();
        } else {
            releasePlayer();
        }


    }

    private void initializePlayer() {
        playerView.setActivated(true);
        imageButton.setImageResource(R.drawable.ic_stop_white);
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);

        Uri uri = Uri.parse(getString(R.string.media_url_mp3));
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }
    private void releasePlayer() {
        if (player != null) {
            playerView.setActivated(false);
            imageButton.setImageResource(R.drawable.ic_play_arrow_black);
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }
}
