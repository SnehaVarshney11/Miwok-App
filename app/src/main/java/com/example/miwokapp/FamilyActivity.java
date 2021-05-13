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

public class FamilyActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_family);

        //create and setup the AudioManager to request audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //Create an array of words
        final ArrayList<word> words = new ArrayList<word>();
        words.add(new word("Father","әpә", R.drawable.family_father,R.raw.audio_family_father));
        words.add(new word("Mother","әṭa",R.drawable.family_mother,R.raw.audio_family_mother));
        words.add(new word("Son","angsi", R.drawable.family_son,R.raw.audio_family_son));
        words.add(new word("Daughter","tune", R.drawable.family_daughter,R.raw.audio_family_daughter));
        words.add(new word("Older brother","taachi", R.drawable.family_older_brother,R.raw.audio_family_older_brother));
        words.add(new word("young brother","chalitti", R.drawable.family_younger_brother,R.raw.audio_family_younger_brother));
        words.add(new word("older sister","teṭe", R.drawable.family_older_sister,R.raw.audio_family_older_sister));
        words.add(new word("Young sister", "kolliti", R.drawable.family_younger_sister,R.raw.audio_family_younger_sister));
        words.add(new word("Grandmother","ama", R.drawable.family_grandmother,R.raw.audio_family_grandmother));
        words.add(new word("Grandfather", "paapa", R.drawable.family_grandfather,R.raw.audio_family_grandfather));


        WordAdapter adapter = new WordAdapter(this, words, R.color.category_family);
        ListView listView = findViewById(R.id.list3);
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

                    mMediaPlayer = MediaPlayer.create(FamilyActivity.this, w.getAudioResourceId());
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
