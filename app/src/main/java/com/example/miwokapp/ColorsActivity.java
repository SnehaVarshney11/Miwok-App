package com.example.miwokapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity {
    MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;

    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                            focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        mMediaPlayer.pause();
                        mMediaPlayer.seekTo(0);
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        //the AUDIOFOCUS_GAIN case means we have regained focus and can
                        //resume playback
                        mMediaPlayer.start();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        //The AUDIOFOCUS_LOSS case means we have lost audio focus and stop playback and cleanup resource
                        releaseMediaPlayer();
                    }
                }
            };
    private MediaPlayer.OnCompletionListener mComplitionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colors);

        //create and setup the AudioManager to request audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //Create an array of words
        final ArrayList<word> words = new ArrayList<word>();
        words.add(new word("Red","weṭeṭṭi", R.drawable.color_red,R.raw.audio_color_red));
        words.add(new word("Green","chokokki",R.drawable.color_green,R.raw.audio_color_green));
        words.add(new word("Brown","ṭakaakki", R.drawable.color_brown,R.raw.audio_color_brown));
        words.add(new word("Gray","ṭopoppi", R.drawable.color_gray,R.raw.audio_color_gray));
        words.add(new word("Black","kululli", R.drawable.color_black,R.raw.audio_color_black));
        words.add(new word("White","kelelli", R.drawable.color_white,R.raw.audio_color_white));
        words.add(new word("dusty yellow","ṭopiisә", R.drawable.color_dusty_yellow,R.raw.audio_color_dusty_yellow));
        words.add(new word("mustard yellow","chiwiiṭә",R.drawable.color_mustard_yellow,R.raw.audio_color_mustard_yellow));

        WordAdapter adapter = new WordAdapter(this, words, R.color.category_colors);
        ListView listView = findViewById(R.id.list2);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                word w = words.get(position);
                releaseMediaPlayer();
                //Request audio focus for playback
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        //use the music stream
                        AudioManager.STREAM_MUSIC,
                        //request permanent focus
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    //we have audio focus now

                    mMediaPlayer = MediaPlayer.create(ColorsActivity.this, w.getAudioResourceId());
                    mMediaPlayer.start();
                    //setup a listener on media player, so that we can stop and release the media player once the sound finished playing
                    mMediaPlayer.setOnCompletionListener(mComplitionListener);
                }

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    //cleanup the media player by releasing its resource
    private void releaseMediaPlayer(){
        //if media player is not null, then it may be currently playing a sound
        if(mMediaPlayer!=null){
            mMediaPlayer.release();
            mMediaPlayer=null;
            //Abandon audio focus when playback complete
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}