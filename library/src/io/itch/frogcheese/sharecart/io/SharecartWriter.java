package io.itch.frogcheese.sharecart.io;

import io.itch.frogcheese.sharecart.Sharecart;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class SharecartWriter implements Closeable {

	private PrintWriter writer;

	public SharecartWriter(String path) throws FileNotFoundException {
		this(new File(path));
	}

	public SharecartWriter(File sharecart) throws FileNotFoundException {
		this.writer = new PrintWriter(sharecart);
	}

	public void write(Sharecart sharecart) {
		StringBuilder file = new StringBuilder(Constants.TITLE);
		file.append(Constants.DELIMITER_PARAMETER);
		file.append(Constants.PARAMETER_X).append(Constants.DELIMITER_VALUE).append(shortString(sharecart.x()));
		file.append(Constants.DELIMITER_PARAMETER);
		file.append(Constants.PARAMETER_Y).append(Constants.DELIMITER_VALUE).append(shortString(sharecart.y()));
		file.append(Constants.DELIMITER_PARAMETER);
		for(int i = 0; i < Constants.PARAMETER_MISC.length; i++) {
			file.append(Constants.PARAMETER_MISC[i]).append(Constants.DELIMITER_VALUE).append(intString(sharecart.misc(i)));
			file.append(Constants.DELIMITER_PARAMETER);
		}
		file.append(Constants.PARAMETER_NAME).append(Constants.DELIMITER_VALUE).append(sharecart.name());
		file.append(Constants.DELIMITER_PARAMETER);
		for(int i = 0; i < Constants.PARAMETER_SWITCH.length; i++) {
			file.append(Constants.PARAMETER_SWITCH[i]).append(Constants.DELIMITER_VALUE).append(booleanString(sharecart.switchValue(i)));
			file.append(Constants.DELIMITER_PARAMETER);
		}


		this.writer.write(file.toString());
	}

	private String shortString(short value) {
		return String.valueOf(value);
	}

	private String intString(int value) {
		return String.valueOf(value);
	}

	private String booleanString(boolean value) {
		return String.valueOf(value).toUpperCase();
	}

	@Override
	public void close() throws IOException {
		this.writer.close();
	}

}
