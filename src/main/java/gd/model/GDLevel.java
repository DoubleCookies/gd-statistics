package gd.model;

import gd.enums.DemonDifficulty;
import gd.enums.Difficulty;

/**
 * Represents one level in Geometry Dash. All statistics and attributes of a Geometry
 * Dash level are defined here (name, creator, difficulty, stars, length, etc)
 * 
 * @author Alex1304
 *
 */
public class GDLevel {
	
	/**
	 * The ID of the level
	 */
	private long id;
	
	/**
	 * The name of the level
	 */
	private String name;
	
	/**
	 * The name of the creator who created this level
	 */
	private String creator;
	
	/**
	 * The level difficulty
	 */
	private Difficulty difficulty;
	
	/**
	 * If it's a Demon, the type of Demon difficulty
	 */
	private DemonDifficulty demonDifficulty;
	
	/**
	 * The number of stars assigned to the level
	 */
	private short stars;
	
	/**
	 * The featured score of the level, or a value &lt;= 0 if not featured
	 */
	private int featuredScore;
	
	/**
	 * Whether the level is marked as Epic
	 */
	private boolean epic;
	
	/**
	 * Amount of downloads for the level
	 */
	private long downloads;
	
	/**
	 * Amount of likes for the level
	 */
	private long likes;

	/**
	 * The level description
	 */
	private String description;

	/**
	 * Constructs an instance of gd.model.GDLevel by providing all of its attributes at
	 * once.
	 * 
	 * @param id
	 *            - the ID of the level
	 * @param name
	 *            - the name of the level
	 * @param creator
	 *            - the name of the user who created this level
	 * @param difficulty
	 *            - the level difficulty
	 * @param demonDifficulty
	 *            - if it's a Demon, the type of Demon difficulty
	 * @param stars
	 *            - the number of stars assigned to the level
	 * @param featuredScore
	 *            - the featured score of the level, or a value &lt;= 0 if not
	 *            featured
	 * @param epic
	 *            - whether the level is marked as Epic
	 * @param downloads
	 *            - amount of downloads for the level
	 * @param likes
	 *            - amount of likes for the level
	 * @throws IllegalArgumentException
	 *             if the argument {@code pass} &lt; -2
	 */
	public GDLevel(long id, String name, String creator, Difficulty difficulty,
				   DemonDifficulty demonDifficulty, short stars, int featuredScore, boolean epic, long downloads,
				   long likes, String description) {
		this.id = id;
		this.name = name;
		this.creator = creator;
		this.difficulty = difficulty;
		this.demonDifficulty = demonDifficulty;
		this.stars = stars;
		this.featuredScore = featuredScore;
		this.epic = epic;
		this.downloads = downloads;
		this.likes = likes;
		this.description = description;
	}
	
	/**
	 * Gets the ID of the level
	 * 
	 * @return long
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * Gets the name of the level
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the name of the creator who created this level
	 * 
	 * @return String
	 */
	public String getCreator() {
		return creator;
	}

	
	/**
	 * Gets the level difficulty
	 * 
	 * @return gd.enums.Difficulty
	 */
	public Difficulty getDifficulty() {
		return difficulty;
	}
	
	/**
	 * Gets the type of Demon difficulty
	 * 
	 * @return gd.enums.DemonDifficulty
	 */
	public DemonDifficulty getDemonDifficulty() {
		return demonDifficulty;
	}
	
	/**
	 * Gets the number of stars assigned to the level
	 * 
	 * @return
	 */
	public short getStars() {
		return stars;
	}
	
	/**
	 * Gets the featured score of the level, or a value &lt;= 0 if not featured
	 * 
	 * @return int
	 */
	public int getFeaturedScore() {
		return featuredScore;
	}
	
	/**
	 * Whether the level is marked as Epic
	 * 
	 * @return boolean
	 */
	public boolean isEpic() {
		return epic;
	}
	
	/**
	 * Gets the amount of downloads for the level
	 * 
	 * @return long
	 */
	public long getDownloads() {
		return downloads;
	}

	/**
	 * Gets the amount of likes for the level
	 * 
	 * @return long
	 */
	public long getLikes() {
		return likes;
	}

	/**
	 * Sets the name of the level
	 *
	 * @param name - String
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the name of the creator who created this level
	 *
	 * @param creator - String
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * Sets the level difficulty
	 *
	 * @param difficulty - gd.enums.Difficulty
	 */
	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	/**
	 * Sets the type of Demon difficulty
	 *
	 * @param demonDifficulty - gd.enums.DemonDifficulty
	 */
	public void setDemonDifficulty(DemonDifficulty demonDifficulty) {
		this.demonDifficulty = demonDifficulty;
	}

	/**
	 * Sets the number of stars assigned to the level
	 *
	 * @param stars - short
	 */
	public void setStars(short stars) {
		this.stars = stars;
	}

	/**
	 * Sets the featured score of the level, or a value &lt;= 0 if not featured
	 *
	 * @param featuredScore - int
	 */
	public void setFeaturedScore(int featuredScore) {
		this.featuredScore = featuredScore;
	}

	/**
	 * Sets whether the level is marked as Epic
	 *
	 * @param epic - boolean
	 */
	public void setEpic(boolean epic) {
		this.epic = epic;
	}

	/**
	 * Sets the amount of downloads for the level
	 *
	 * @param downloads - long
	 */
	public void setDownloads(long downloads) {
		this.downloads = downloads;
	}

	/**
	 * Sets the amount of likes for the level
	 *
	 * @param likes - long
	 */
	public void setLikes(long likes) {
		this.likes = likes;
	}


	/**
	 * Whether the level is featured.
	 * 
	 * @return boolean
	 */
	public boolean isFeatured() {
		return featuredScore > 0;
	}
	
	/**
	 * Whether the level is Awarded, i.e the amount of stars is greater than 0.
	 * 
	 * @return boolean
	 */
	public boolean isAwarded() {
		return stars > 0;
	}

	/**
	 * Gets the level description
	 *
	 * @return String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the level description
	 *
	 * @param description - String
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "\"" + name + "\" by " + creator + " (" + id + ") — likes: " + likes + ", downloads: " + downloads;
	}

	public String markdownString() {
		return "| " + name + " | " + creator + " | " + id + " | " + likes + " | " + downloads;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	
	/**
	 * Two levels are considered as equal if and only if they have both
	 * the same ID
	 * 
	 * @see {@link Object#equals(Object)}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GDLevel other = (GDLevel) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
