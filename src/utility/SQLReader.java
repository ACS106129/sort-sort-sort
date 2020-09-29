package utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * SQL Reader for .sql file
 */
public class SQLReader {

	private String dataName = "";

	private Charset encodeType = null;

	private File fileSource = null;

	// attribute / attribute's datas
	private Map<String, List<String>> dataMaps = new LinkedHashMap<>();

	/**
	 * SQL Reader constructor with UTF-8 encoding
	 * 
	 * @param targetFilePath file path
	 */
	public SQLReader(String targetFilePath) throws FileNotFoundException {
		this(new File(targetFilePath), StandardCharsets.UTF_8);
	}

	/**
	 * SQL Reader constructor with UTF-8 encoding
	 * 
	 * @param targetFile file
	 */
	public SQLReader(File targetFile) throws FileNotFoundException {
		this(targetFile, StandardCharsets.UTF_8);
	}

	/**
	 * SQL Reader constructor
	 * 
	 * @param targetFilePath file path
	 * @param encodeType     type of encode
	 */
	public SQLReader(String targetFilePath, Charset encodeType) throws FileNotFoundException {
		this(new File(targetFilePath), encodeType);
	}

	/**
	 * SQL Reader constructor
	 * 
	 * @param targetFile file
	 * @param encodeType type of encode
	 */
	public SQLReader(File targetFile, Charset encodeType) throws FileNotFoundException {
		if (targetFile.getName().indexOf(".sql") == -1)
			throw new FileNotFoundException("Failed in creating SQLReader object");
		fileSource = targetFile;
		this.encodeType = encodeType;
	}

	/**
	 * Read input data from SQL file
	 */
	public void readData() throws IOException {
		Scanner sc = new Scanner(fileSource, encodeType);
		boolean dataFind = false;
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			if (line.indexOf("INSERT INTO") != -1) {
				int index = 0;
				List<String> attrs = splitIntoElements(line.substring(line.indexOf('`'), line.lastIndexOf(')')), '`');
				for (String attr : attrs) {
					if (index == 0)
						dataName = attr;
					else
						dataMaps.put(attr, new CopyOnWriteArrayList<>());
					++index;
				}
				dataFind = true;
				continue;
			}
			if (!dataFind)
				continue;
			List<String> elements = splitIntoElements(line.substring(line.indexOf('('), line.lastIndexOf(')')), '\'');
			int index = 0;
			for (List<String> data : dataMaps.values()) {
				data.add(elements.get(index++));
			}
			if (line.lastIndexOf(';') > line.lastIndexOf(')'))
				break;
		}
		sc.close();
	}

	private List<String> splitIntoElements(String line, char quote) {
		List<String> elements = new ArrayList<>();
		boolean isElement = false;
		int startIndex = 0;
		for (int i = 0; i < line.length(); ++i) {
			if (line.charAt(i) != quote)
				continue;
			if (!isElement) {
				isElement = true;
				startIndex = i + 1;
				continue;
			}
			if (line.charAt(i - 1) != '\\' || (i - 2 >= 0 && line.charAt(i - 2) == '\\')) {
				elements.add(line.substring(startIndex, i));
				isElement = false;
			}
		}
		return elements;
	}

	public String getDataName() {
		return dataName;
	}

	public Charset getEncodeType() {
		return encodeType;
	}

	public File getFileSource() {
		return fileSource;
	}

	public Map<String, List<String>> getDataMaps() {
		return dataMaps;
	}
}