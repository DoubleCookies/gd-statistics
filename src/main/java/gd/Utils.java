package gd;

import gd.model.GDSong;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains utility static methods
 *
 * @author Alex1304
 *
 */
public abstract class Utils {

    private static final Map<Integer, GDSong> AUDIO_TRACKS = initAudioTracks();

    private static Map<Integer, GDSong> initAudioTracks() {
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

    /**
     * Transforms a string into a map. The string must be in a specific format for this
     * method to work. For example, a string formatted as <code>"1:abc:2:def:3:xyz"</code>
     * will return the following map:
     * <pre>
     * 1 =&gt; abc
     * 2 =&gt; def
     * 3 =&gt; xyz
     * </pre>
     *
     * @param str - the string to convert to map
     * @param regex - the regex representing the separator between values in the string. In the example above,
     * it would be ":"
     * @return a Map of Integer, String
     * @throws NumberFormatException - if a key of the map couldn't be converted to int. Example, the string
     * <code>1:abc:A:xyz</code> would throw this exception.
     */
    public static Map<Integer, String> splitToMap(String str, String regex) {
        Map<Integer, String> map = new HashMap<>();
        String[] splitted = str.split(regex);

        for (int i = 0 ; i < splitted.length - 1 ; i += 2)
            map.put(Integer.parseInt(splitted[i]), splitted[i + 1]);

        return map;
    }

    /**
     * Gets an audio track by its ID
     *
     * @param id - the audio track id
     * @return GDSong
     */
    public static GDSong getAudioTrack(int id) {
        return AUDIO_TRACKS.containsKey(id) ?  AUDIO_TRACKS.get(id) : new GDSong(0, "-", "Unknown");
    }
}
