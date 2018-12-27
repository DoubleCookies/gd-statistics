package gd;

import gd.model.GDSong;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    /* Song data */
    public static final int INDEX_SONG_ID = 1;
    public static final int INDEX_SONG_TITLE = 2;
    public static final int INDEX_SONG_AUTHOR = 4;
    public static final int INDEX_SONG_SIZE = 5;
    public static final int INDEX_SONG_URL = 10;

    /* Level data */
    public static final int INDEX_LEVEL_AUDIO_TRACK = 12;
    public static final int INDEX_LEVEL_SONG_ID = 35;

    /* Default geometry dash soundtracks */
    public static final Map<Integer, GDSong> AUDIO_TRACKS = initDefaultSoundtracks();

    private static Map<Integer, GDSong> initDefaultSoundtracks() {
        Map<Integer, GDSong> map = new HashMap<>();
        map.put(0, new GDSong(-1,"ForeverBound", "Stereo Madness"));
        map.put(1, new GDSong(-2,"DJVI", "Back On Track"));
        map.put(2, new GDSong(-3,"Step", "Polargeist"));
        map.put(3, new GDSong(-4,"DJVI", "Dry Out"));
        map.put(4, new GDSong(-5,"DJVI", "Base After Base"));
        map.put(5, new GDSong(-6,"DJVI", "Cant Let Go"));
        map.put(6, new GDSong(-7,"Waterflame", "Jumper"));
        map.put(7, new GDSong(-8,"Waterflame", "Time Machine"));
        map.put(8, new GDSong(-9,"DJVI", "Cycles"));
        map.put(9, new GDSong(-10,"DJVI", "xStep"));
        map.put(10, new GDSong(-11,"Waterflame", "Clutterfunk"));
        map.put(11, new GDSong(-12,"DJ-Nate", "Theory of Everything"));
        map.put(12, new GDSong(-13,"Waterflame", "Electroman Adventures"));
        map.put(13, new GDSong(-14,"DJ-Nate", "Clubstep"));
        map.put(14, new GDSong(-15,"DJ-Nate", "Electrodynamix"));
        map.put(15, new GDSong(-16,"Waterflame", "Hexagon Force"));
        map.put(16, new GDSong(-17,"Waterflame", "Blast Processing"));
        map.put(17, new GDSong(-18,"DJ-Nate", "Theory of Everything 2"));
        map.put(18, new GDSong(-19,"Waterflame", "Geometrical Dominator"));
        map.put(19, new GDSong(-20,"F-777", "Deadlocked"));
        map.put(20, new GDSong(-21,"MDK", "Fingerdash"));
        return map;
    }
}
