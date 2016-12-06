package com.fogok.explt.core;

import com.fogok.explt.utils.Prefers;

/**
 * Created by FOGOK on 01.12.2016 11:20.
 * Если ты это читаешь, то знай, что этот код хуже
 * кожи разлагающегося бомжа лежащего на гнилой
 * лавочке возле остановки автобуса номер 985
 */
public class SoundCore {

    public static boolean MUSIC_ENABLED;
    public static boolean SOUND_ENABLED;

    public static void init(){
        MUSIC_ENABLED = Prefers.getBool(Prefers.KeyMusicEnabled, true);
        SOUND_ENABLED = Prefers.getBool(Prefers.KeySoundEnabled, true);
    }

}
