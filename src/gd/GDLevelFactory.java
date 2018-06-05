package gd;

import java.io.IOException;
import java.util.*;

/**
 * Utility class to convert raw data into gd.GDLevel instances
 */
public abstract class GDLevelFactory {
	
	/**
	 * Associates the integer value in the raw data with the corresponding difficulty
	 */
	private static Map<Integer, Difficulty> difficultyByValue = new HashMap<>();
	
	/**
	 * Associates the integer value in the raw data with the corresponding Demon difficulty
	 */
	private static Map<Integer, DemonDifficulty> demonDifficultyByValue = new HashMap<>();
	
	static {
		difficultyByValue.put(0, Difficulty.NA);
		difficultyByValue.put(10, Difficulty.EASY);
		difficultyByValue.put(20, Difficulty.NORMAL);
		difficultyByValue.put(30, Difficulty.HARD);
		difficultyByValue.put(40, Difficulty.HARDER);
		difficultyByValue.put(50, Difficulty.INSANE);
		
		demonDifficultyByValue.put(0, DemonDifficulty.HARD);
		demonDifficultyByValue.put(3, DemonDifficulty.EASY);
		demonDifficultyByValue.put(4, DemonDifficulty.MEDIUM);
		demonDifficultyByValue.put(5, DemonDifficulty.INSANE);
		demonDifficultyByValue.put(6, DemonDifficulty.EXTREME);
	}
	
	/**
	 * Reads the rawdata and return an instance of gd.GDLevel corresponding to the requested level.
	 * When searching for a level using filters, several search results can show up (up to 10 per page).
	 * So it's necessary to provide which result item is the requested level.
	 * 
	 * @param rawData - urlencoded String of the level search results
	 * @param index - result item corresponding to the requested level
	 * @return new instance of gd.GDLevel
	 * @throws IndexOutOfBoundsException if the index given doesn't point to a search item.
	 */
	public static GDLevel buildGDLevelSearchedByFilter(String rawData, int index, boolean download)
			throws IndexOutOfBoundsException {
		try {
			Map<Integer, String> structuredLvlInfo = structureRawData(cutOneLevel(cutLevelInfoPart(rawData), index));
			Map<Long, String> structuredCreatorsInfo = structureCreatorsInfo(cutCreatorInfoPart(rawData, download));

			if(structuredLvlInfo.size() == 0)
				return null;
			// Determines the difficulty of the level
			Difficulty lvlDiff = difficultyByValue.get(Integer.parseInt(structuredLvlInfo.get(9)));
			if (structuredLvlInfo.get(25).equals("1"))
				lvlDiff = Difficulty.AUTO;
			if (structuredLvlInfo.get(17).equals("1"))
				lvlDiff = Difficulty.DEMON;
			
			// Level creator info
			String creator = "-";
			if (structuredCreatorsInfo != null && structuredLvlInfo.containsKey(6))
				creator = structuredCreatorsInfo.get(Long.parseLong(structuredLvlInfo.get(6)));
		
			return new GDLevel(
				Long.parseLong(structuredLvlInfo.get(1)),
				structuredLvlInfo.get(2),
				creator,
				lvlDiff,
				demonDifficultyByValue.get(Integer.parseInt(structuredLvlInfo.get(43))),
				Short.parseShort(structuredLvlInfo.get(18)),
				Integer.parseInt(structuredLvlInfo.get(19)),
				structuredLvlInfo.get(42).equals("1"),
				Long.parseLong(structuredLvlInfo.get(10)),
				Long.parseLong(structuredLvlInfo.get(14))
			);
		} catch (NullPointerException|IllegalArgumentException e) {
			throw new IndexOutOfBoundsException();
		}
	}
	
	// The private methods below are used to make the code of the public methods clearer
	private static String cutLevelInfoPart(String rawData) {
		try {
			return rawData.split("#")[0];
		} catch (ArrayIndexOutOfBoundsException e) {
			return "";
		}
	}
	
	private static String cutCreatorInfoPart(String rawData, boolean download) {
		try {
			return rawData.split("#")[download ? 3 : 1];
		} catch (ArrayIndexOutOfBoundsException e) {
			return "";
		}
	}

	private static String cutOneLevel(String levelInfoPartRD, int index) {
		try {
			return levelInfoPartRD.split("\\|")[index];
		} catch (ArrayIndexOutOfBoundsException e) {
			return "";
		}
	}
	
	private static Map<Long, String> structureCreatorsInfo(String creatorsInfoRD) {
		if (creatorsInfoRD.isEmpty())
			return null;
		
		String[] arrayCreatorsRD = creatorsInfoRD.split("\\|");
		Map<Long, String> structuredCreatorslInfo = new HashMap<>();
		
		for (String creatorRD : arrayCreatorsRD) {
			structuredCreatorslInfo.put(Long.parseLong(creatorRD.split(":")[0]), creatorRD.split(":")[1]);
		}
		
		return structuredCreatorslInfo;
	}

	public static Map<Integer, String> structureRawData(String rawData) {
		String[] arrayOfData = rawData.split(":");
		Map<Integer, String> result = new HashMap<>();

		try {
			for (int i = 0 ; i < arrayOfData.length ; i += 2) {
				result.put(Integer.parseInt(arrayOfData[i]), (i+1 < arrayOfData.length) ? arrayOfData[i+1] : "");
			}
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {

		}

		return result;
	}
}
