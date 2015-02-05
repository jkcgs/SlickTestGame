package com.makzk.games.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;

public class Utils {
	/**
	 * Cargar archivo dentro de un <code>String</code>, incluso si está dentro
	 * de un archivo jar.
	 * @param filepath La ubicación del archivo, respecto al directorio de
	 * ejecución, o del empaquetado jar.
	 * @return El contenido del archivo (<code>String</code>), o <code>null</code> si el archivo no ha podido
	 * ser leído.
	 */
	public static String getResourceContent(String filepath) {
		String content = null;
		FileSystem fs = null;
		try {
			Path path = null;
			final URL url = ResourceLoader.getResource(filepath);

			if(url.toString().contains("!")) {
				final Map<String, String> env = new HashMap<>();
				final String[] array = url.toURI().toString().split("!");
				final URI uri = URI.create(array[0]);
				fs = FileSystems.newFileSystem(uri, env);
				path = fs.getPath(array[1]);
			} else {
				path = Paths.get(url.toURI());
			}

			final byte[] lines = Files.readAllBytes(path);
			content = new String(lines, Charset.defaultCharset());

			if(fs != null) {
				fs.close();
			}
		} catch (IOException | URISyntaxException e) {
			Log.error(e);
		}
		
		return content;
	}
}
