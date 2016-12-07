package com.fogok.explt.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.fogok.explt.utils.Prefers;

import java.util.Random;

/**
 * Created by FOGOK on 01.12.2016 11:20.
 * Если ты это читаешь, то знай, что этот код хуже
 * кожи разлагающегося бомжа лежащего на гнилой
 * лавочке возле остановки автобуса номер 985
 */
public class SoundCore {

    public static boolean MUSIC_ENABLED;
    public static boolean SOUND_ENABLED;

    private static float VOLUME = 1f;

    public enum Sounds{
        Stone, Notify, Restart
    }

    public enum Musics{
        MainMenu, Game
    }

    private static Sound stone, notify, restart;
    private static Music mainMenu, game;

    public static void init(){
        MUSIC_ENABLED = Prefers.getBool(Prefers.KeyMusicEnabled, true);
        SOUND_ENABLED = Prefers.getBool(Prefers.KeySoundEnabled, true);


        mainMenu = Gdx.audio.newMusic(Gdx.files.internal("music/main.mp3"));
        mainMenu.setLooping(true);
        game = Gdx.audio.newMusic(Gdx.files.internal("music/game.mp3"));
        game.setLooping(true);

        stone = Gdx.audio.newSound(Gdx.files.internal("music/stone.mp3"));
        notify = Gdx.audio.newSound(Gdx.files.internal("music/notify.mp3"));
        restart = Gdx.audio.newSound(Gdx.files.internal("music/restart.mp3"));
    }

    public static void playSound(Sounds s){
        playSound(s, false);
    }

    public static void playSound(Sounds s, boolean rndmz){
        if(SOUND_ENABLED){
            switch (s){
                case Stone:
                    stone.play(VOLUME * 1f, 0.8f + ((float) rnd.nextInt(20) / 100f), 0);
                    break;
                case Notify:
                    notify.play(VOLUME * 0.5f, 0.8f + ((float) rnd.nextInt(40) / 100f), 0);
                    break;
                case Restart:
                    restart.play(VOLUME * 1f, 0.8f + ((float) rnd.nextInt(40) / 100f), 0);
                    break;
            }
        }
    }

    public static void mainMenuMusicSet(){
        if (MUSIC_ENABLED)
            mainMenu.play();
        else
            mainMenu.pause();
    }

    public static Music getMainMenu(){
        return mainMenu;
    }

    public static Music getGame() {
        return game;
    }

    public static void playMusic(Musics m){
        if (MUSIC_ENABLED){
            switch (m){
                case Game:
                    if (!game.isPlaying())
                        game.play();
                    game.setVolume(VOLUME * 0.8f);
                    break;
                case MainMenu:
                    if (!mainMenu.isPlaying())
                        mainMenu.play();
                    mainMenu.setVolume(VOLUME * 1f);
                    break;
            }
        }
    }

    public static void stopMusic(Musics m){
        switch (m){
            case Game:
                game.stop();
                break;
            case MainMenu:
                mainMenu.stop();
                break;
        }
    }

    public static void setVolume(float VOLUME) {
        SoundCore.VOLUME = VOLUME;
        if (game.isPlaying())
            game.setVolume(VOLUME * 0.8f);

        if (mainMenu.isPlaying())
            mainMenu.setVolume(VOLUME * 1f);
    }

    private static Random rnd = new Random();

    public static void disposeAll() {
        mainMenu.dispose();
        mainMenu = null;

        game.dispose();
        game = null;

        stone.dispose();
        stone = null;

        notify.dispose();
        notify = null;

        restart.dispose();
        restart = null;
    }

}
