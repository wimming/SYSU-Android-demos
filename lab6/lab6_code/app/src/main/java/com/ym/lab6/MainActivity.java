package com.ym.lab6;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    enum STATE {
        PLAYING,
        PAUSED,
        STOPPED
    }

    private STATE state = STATE.STOPPED;
    public Handler mHandler = new Handler();
    private Runnable progressRunnable;

    private ObjectAnimator objectAnimator;

    @BindView(R.id.stop)
    Button stop;
    @BindView(R.id.play)
    Button play;
    @BindView(R.id.quit)
    Button quit;
    @BindView(R.id.currentTime)
    TextView currentTime;
    @BindView(R.id.maxTime)
    TextView maxTime;
    @BindView(R.id.musicSeekBar)
    SeekBar musicSeekBar;
    @BindView(R.id.stateText)
    TextView stateText;
    @BindView(R.id.imageView)
    ImageView imageView;

    @OnClick(R.id.stop)
    public void stop() {
        state = STATE.STOPPED;
        stateText.setText("STOP");
        play.setText("PLAY");
        try {
            musicService.reset();
            objectAnimator.cancel();
            objectAnimator.start();
            objectAnimator.pause();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @OnClick(R.id.play)
    public void play() {
        if (state == STATE.PAUSED || state == STATE.STOPPED) {
            state = STATE.PLAYING;
            stateText.setText("PLAYING");
            play.setText("PAUSE");
            objectAnimator.resume();
            musicService.play();
        }
        else if (state == STATE.PLAYING) {
            state = STATE.PAUSED;
            stateText.setText("PAUSE");
            play.setText("PLAY");
            objectAnimator.pause();
            musicService.pause();
        }
    }
    @OnClick(R.id.quit)
    public void quit() {
        state = STATE.STOPPED;
        stateText.setText("STOP");
        play.setText("PLAY");
        objectAnimator.cancel();
        unbindService(sc);
        MainActivity.this.finish();
        System.exit(0);
    }

    private MusicService musicService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        bindService(new Intent(MainActivity.this, MusicService.class), sc, BIND_AUTO_CREATE);

        progressRunnable = new Runnable() {
            @Override
            public void run() {
                musicSeekBar.setMax(musicService.getMusicDuration());
                musicSeekBar.setProgress(musicService.getMusicCurrentPosition());
                mHandler.postDelayed(progressRunnable, 100);
            }
        };

        musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");
                String current = timeFormat.format(new Date(musicService.getMusicCurrentPosition()));
                String max = timeFormat.format(new Date(musicService.getMusicDuration()));
                maxTime.setText(max);
                currentTime.setText(current);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                musicService.setMusicPosition(seekBar.getProgress());
            }
        });

    }

    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicService = ((MusicService.MyBinder)service).getService();

            mHandler.post(progressRunnable);

//            RotateAnimation rotateAnimation = new RotateAnimation(0, ((float) musicService.getMusicDuration())/360*5,
//                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//            rotateAnimation.setDuration(musicService.getMusicDuration());
//            rotateAnimation.setInterpolator(new LinearInterpolator());

            objectAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, ((float) musicService.getMusicDuration())/360*5);
            objectAnimator.setDuration(musicService.getMusicDuration());
            objectAnimator.setInterpolator(new LinearInterpolator());

            objectAnimator.start();
            objectAnimator.pause();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
        }
    };

}
