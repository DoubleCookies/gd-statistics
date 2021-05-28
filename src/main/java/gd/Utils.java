package gd;

import gd.model.GDSong;

import java.util.HashMap;
import java.util.Map;

import static gd.Constants.DEFAULT_SOUNDTRACKS;

/**
 * Contains utility static methods
 *
 * @author Alex1304
 */
public abstract class Utils {

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
     * @param str   - the string to convert to map
     * @param regex - the regex representing the separator between values in the string. In the example above,
     *              it would be ":"
     * @return a Map of Integer, String
     * @throws NumberFormatException - if a key of the map couldn't be converted to int. Example, the string
     *                               <code>1:abc:A:xyz</code> would throw this exception.
     */
    public static Map<Integer, String> splitToMap(String str, String regex) {
        Map<Integer, String> map = new HashMap<>();
        String[] splitted = str.split(regex);

        for (int i = 0; i < splitted.length - 1; i += 2)
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
        return DEFAULT_SOUNDTRACKS.containsKey(id) ? DEFAULT_SOUNDTRACKS.get(id) : new GDSong(0, "-", "Unknown");
    }
}
