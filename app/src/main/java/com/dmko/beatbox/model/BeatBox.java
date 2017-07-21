package com.dmko.beatbox.model;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatBox {
    private static final String TAG = "BeatBox";
    private static final String SOUNDS_FOLDER = "sample_sounds";
    private static final int MAX_SOUNDS = 5;
    private AssetManager mAssets;
    private List<Sound> mSounds = new ArrayList<>();
    private SoundPool mSoundPool;
    private float mRate;

    public BeatBox(Context context) {
        mRate = 1;
        mAssets = context.getAssets();
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        loadSounds();
    }

    public List<Sound> getSounds() {
        return mSounds;
    }

    public void play(Sound sound) {
        Integer soundId = sound.getSoundId();
        if (soundId == null) return;
        mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, mRate);
    }

    public void release() {
        mSoundPool.release();
    }

    public void setRate(float rate) {
        if (rate < 0.5) mRate = (float) 0.5;
        else if (rate > 2) mRate = 2;
        else mRate = rate;
    }


    private void loadSounds() {
        String[] soundNames;

        try {
            soundNames = mAssets.list(SOUNDS_FOLDER);
            for (String filename : soundNames) {
                String assetPath = SOUNDS_FOLDER + "/" + filename;
                Sound sound = new Sound(assetPath);
                load(sound);
                mSounds.add(sound);
            }
            Log.i(TAG, "Found " + soundNames.length + " sounds");
        } catch (IOException e) {
            Log.e(TAG, "Couldn't load list assets", e);
        }
    }

    private void load(Sound sound) throws IOException {
        AssetFileDescriptor afd = mAssets.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(afd, 1);
        sound.setSoundId(soundId);
    }
}
