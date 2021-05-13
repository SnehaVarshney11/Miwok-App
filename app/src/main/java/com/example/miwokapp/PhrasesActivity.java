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

public class PhrasesActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_phrases);

        //create and setup the AudioManager to request audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //Create an array of words
        final ArrayList<word> words = new ArrayList<word>();
        words.add(new word("Where are you going?","minto wuksus",R.raw.audio_phrase_where_are_you_going));
        words.add(new word("What is your name?","tinnә oyaase'nә",R.raw.audio_phrase_what_is_your_name));
        words.add(new word("My name is..","oyaaset..",R.raw.audio_phrase_my_name_is));
        words.add(new word("How are you felling?","michәksәs?",R.raw.audio_phrase_how_are_you_feeling));
        words.add(new word("I'm felling good","kuchi achit",R.raw.audio_phrase_im_feeling_good));
        words.add(new word("Are you coming?", "әәnәs'aa?",R.raw.audio_phrase_are_you_coming));
        words.add(new word("Yes, I'm coming","hәә’ әәnәm",R.raw.audio_phrase_yes_im_coming));
        words.add(new word("I'm coming","әәnәm",R.raw.audio_phrase_im_coming));
        words.add(new word("Let's go","yoowutis",R.raw.audio_phrase_lets_go));
        words.add(new word("Come here.", "әnni'nem",R.raw.audio_phrase_come_here));


        WordAdapter adapter = new WordAdapter(this, words,R.color.category_phrases);
        ListView listView = findViewById(R.id.list4);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                word w = words.get(position);
                //release media player if it currently exists because we are about to play a diff sound file
                releaseMediaPlayer();

                //Request audio focus for playback
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        //use the music stream
                        AudioManager.STREAM_MUSIC,
                        //request permanent focus
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    //we have audio focus now

                    mMediaPlayer = MediaPlayer.create(PhrasesActivity.this, w.getAudioResourceId());
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
