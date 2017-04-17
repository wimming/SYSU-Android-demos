package com.ym.lab6;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * Created by ym on 16-11-3.
 */

public class MusicService extends Service {
    private MediaPlayer mediaPlayer;
    private final IBinder binder = new MyBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
//        try {
        mediaPlayer = MediaPlayer.create(this, R.raw.sample_music);
//            AssetFileDescriptor media = getAssets().openFd("sample_music.mp3");
//                mediaPlayer.setDataSource(media.getFileDescriptor(), media.getStartOffset(), media.getLength());
        mediaPlayer.setLooping(true);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return binder;
    }

    public void play() {
        mediaPlayer.start();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void reset() throws IOException {
        mediaPlayer.stop();
        mediaPlayer.prepare();
        mediaPlayer.seekTo(0);
    }

    public int getMusicCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }
    public int getMusicDuration() {
        return mediaPlayer.getDuration();
    }

    public void setMusicPosition(int milliseconds) {
        mediaPlayer.seekTo(milliseconds);
    }

    public class MyBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }
}
