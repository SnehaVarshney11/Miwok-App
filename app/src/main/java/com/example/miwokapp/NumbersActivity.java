package com.example.miwokapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class NumbersActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_numbers);

        //create and setup the AudioManager to request audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //Create an array of words
        final ArrayList<word> words = new ArrayList<word>();
        words.add(new word("one", "lutti", R.drawable.number_one, R.raw.audio_number_one));
        words.add(new word("Two", "otiiko", R.drawable.number_two, R.raw.audio_number_two));
        words.add(new word("Three", "tolookosu", R.drawable.number_three, R.raw.audio_number_three));
        words.add(new word("Four", "oyyisa", R.drawable.number_four, R.raw.audio_number_four));
        words.add(new word("Five", "massokka", R.drawable.number_five, R.raw.audio_number_five));
        words.add(new word("Six", "temmokka", R.drawable.number_six, R.raw.audio_number_six));
        words.add(new word("Seven", "kenekaku", R.drawable.number_seven, R.raw.audio_number_seven));
        words.add(new word("Eight", "kawinta", R.drawable.number_eight, R.raw.audio_number_eight));
        words.add(new word("Nine", "wo'e", R.drawable.number_nine, R.raw.audio_number_nine));
        words.add(new word("Ten", "na'aacha", R.drawable.number_ten, R.raw.audio_number_ten));


        WordAdapter adapter = new WordAdapter(this, words, R.color.category_numbers);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
//        GridView gridView = findViewById(R.id.list);
//        gridView.setAdapter(itemsAdapter);

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

                    mMediaPlayer = MediaPlayer.create(NumbersActivity.this, w.getAudioResourceId());
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
        //when the activity is stopped, release the media player resources because we won't be playing any more soounds
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



//        for(int i=0; i<10; i++) {
//            TextView wordView = new TextView(this);
//            wordView.setText(words.get(i));
//            rootView.addView(wordView);
//        }
//       verify the contents of ArrayList by printing out each ArrayList elements to the log
//        Log.v("NumbersActivity", "Word at index 0: " +words.get(0));
//        Log.v("NumbersActivity", "Word at index 1: " +words.get(1));
//
    }
