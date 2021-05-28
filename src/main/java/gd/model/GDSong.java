package gd.model;

/**
 * Represents a song used in GD levels
 *
 * @author Alex1304
 */
public class GDSong {

    private long songID;
    private String songAuthorName;
    private String songSize;
    private String songTitle;
    private String downloadURL;
    private boolean isCustom;

    /**
     * @param songID         - the song ID
     * @param songAuthorName - the song author name
     * @param songSize       - the song author size
     * @param songTitle      - the song title
     * @param downloadURL    - link to the song audio file
     * @param isCustom       - whether the song is custom
     */
    public GDSong(long songID, String songAuthorName, String songSize, String songTitle, String downloadURL, boolean isCustom) {
        this.songID = songID;
        this.songAuthorName = songAuthorName;
        this.songSize = songSize;
        this.songTitle = songTitle;
        this.downloadURL = downloadURL;
        this.isCustom = isCustom;
    }

    /**
     * Quick constructor for non-custom songs
     *
     * @param id             - standard song Id
     * @param songAuthorName - the song author name
     * @param songTitle      - the song title
     */
    public GDSong(int id, String songAuthorName, String songTitle) {
        this(id, songAuthorName, "", songTitle, "", false);
    }

    @Override
    public String toString() {
        return "| " + songID + " | " + songAuthorName + " | " + songTitle + " | ";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (songID ^ (songID >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof GDSong))
            return false;
        GDSong other = (GDSong) obj;
        return songID == other.songID;
    }
}
