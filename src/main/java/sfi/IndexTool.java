package sfi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class IndexTool {
	private static final String SPLITTER = "[^a-zA-Z0-9aáeéiíoóöőuúüűAÁEÉIÍOÓÖŐUÚÜŰ]";

	private static final Logger LOGGER = Logger.getLogger(IndexTool.class.getName());

	@Parameter(names = {"--inputDir", "-i"}, required = true)
	private String inputDir;

	@Parameter(names = {"--outputDir", "-o"}, required = true)
	private String outputDir;

	public static void main(final String[] args) throws IOException {
		IndexTool main = new IndexTool();
		JCommander.newBuilder().addObject(main).build().parse(args);
		main.run();
	}

	private void run() throws IOException {
		File dir = new File(inputDir);
		File out = new File(outputDir);
		if (out.exists()) {
			FileUtils.forceDelete(out);
			LOGGER.log(Level.INFO, "{0} deleted", out);
		}
		prepareFiles(dir);
	}

	private void prepareFiles(final File dir) throws IOException {
		for (File target : dir.listFiles()) {
			prepareFile(target);
		}
	}

	private void prepareFile(final File target) throws IOException {
		String name = target.getName();
		String[] nameParts = name.split(SPLITTER);
		createLinks(target, name, nameParts);
	}

	private void createLinks(final File target, final String name, final String[] nameParts) throws IOException {
		for (String namePart : nameParts) {
			if (namePart.length() > 0) {
				namePart = createLink(target, name, namePart);
			}
		}
	}

	private String createLink(final File target, final String name, String namePart) throws IOException {
		File out;
		namePart = namePart.toUpperCase();
		out = new File(outputDir, namePart.substring(0, 1));
		out = new File(out, namePart);
		if (!out.exists()) {
			out.mkdirs();
		} else {
		}
		Path link = new File(out, name).toPath();
		if (!link.toFile().exists()) {
			Files.createSymbolicLink(link, Paths.get(target.getAbsolutePath()));
			LOGGER.log(Level.INFO, "{0} linked to {1}", new Object[] {target.toPath(), link});
		}
		return namePart;
	}

}
