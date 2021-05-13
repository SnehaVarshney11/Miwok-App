package com.example.miwokapp;

public class word {
    //Default translation for the word
    private String mDefaultTranslation;

    //Miwok translation for the word
    private String mMiwokTranslation;

    //Image resource id for the word
    private int mImageResourceId = NO_IMAGE_PROVIDED;

    private static  final int NO_IMAGE_PROVIDED = -1;

    //Audio resource id for the word
    private int mAudioResourceId;

    //Constructor
    public word(String DefaultTranslation, String miwokTranslation, int audioResourceId){
        mDefaultTranslation = DefaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mAudioResourceId = audioResourceId;
    }

    //Constructor
    public word(String DefaultTranslation, String miwokTranslation, int ImageResourceId, int audioResourceId){
        mDefaultTranslation = DefaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mImageResourceId = ImageResourceId;
        mAudioResourceId = audioResourceId;
    }


    //Get default transtion of the word
    public String getDefaultTranslation(){
        return mDefaultTranslation;
    }

    //Get miwok translation of the word
    public String getMiwokTranslation(){
        return mMiwokTranslation;
    }

    //Return the image resource id of the word
    public int getImageResoucreId() {
        return mImageResourceId;
    }

    public boolean hasImage(){
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }

    //Return the audio resource id of the word
    public int getAudioResourceId(){ return mAudioResourceId; }


}
