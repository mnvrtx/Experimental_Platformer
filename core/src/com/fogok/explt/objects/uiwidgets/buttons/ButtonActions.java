package com.fogok.explt.objects.uiwidgets.buttons;


import com.fogok.explt.utils.Prefers;

import static com.fogok.explt.core.SoundCore.MUSIC_ENABLED;
import static com.fogok.explt.core.SoundCore.SOUND_ENABLED;


/**
 * Created by FOGOK on 13.10.2016 0:49.
 * Если ты это читаешь, то знай, что этот код хуже
 * кожи разлагающегося бомжа лежащего на гнилой
 * лавочке возле остановки автобуса номер 985
 */

public class ButtonActions {

    public enum All{
        SOUND, MUSIC
    }

    public static void activateAction(All action){
        switch (action){
            case SOUND:
                SOUND_ENABLED = !SOUND_ENABLED;
                Prefers.putBool(Prefers.KeySoundEnabled, SOUND_ENABLED);
                break;
            case MUSIC:
                MUSIC_ENABLED = !MUSIC_ENABLED;
                Prefers.putBool(Prefers.KeyMusicEnabled, MUSIC_ENABLED);
                break;
        }
    }

}
