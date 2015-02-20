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

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;

import com.makzk.games.entities.Player;

public class Utils {
	
	/**
	 * Crear un triángulo (equilátero?) en la dirección deseada
	 * @param x La posición inicial X
	 * @param y La posición inicial Y
	 * @param a El tamaño del lado
	 * @param dir La dirección a la que apunta
	 * @return El triángulo (Polygon) generado
	 */
	public static Polygon createEqTriangle(float x, float y, float a, Direction dir) {
		float points[] = new float[]{(x+(a/2)), y, x, (y+a), (x+a), (y+a)};
		Polygon p = new Polygon(points);

		double rotAngle = 0;
		switch(dir) {
			case SOUTH: rotAngle = Math.PI; break;
			case EAST: rotAngle = Math.PI / 2; break;
			default: 
			case WEST: rotAngle = (3 * Math.PI) / 2; break;
		}
		
		if(dir != Direction.NORTH) {
			p = (Polygon) p.transform(
					Transform.createRotateTransform(
						(float) rotAngle, 
						p.getCenterX(), 
						p.getCenterY())
					);
		}
		return p;
	}
	
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
	
	/**
	 * Dibuja la entrada del jugador (las teclas presionadas)
	 * @param g El objetivo en el cual dibujar
	 * @param p El jugador que recibe la entrada
	 * @param x La posición inicial x para dibujar
	 * @param y La posición inicial y para dibujar
	 */
	public static void drawPlayerInput(Input in, Graphics g, Player p, int x, int y) {
		g.setColor(new Color(255,255,255,100));
		g.fillRect(x, y, 120, 30);
		g.setColor(in.isKeyDown(p.getControlLeft()) ? Color.red : Color.white);
		g.fillRect(x + 5, y + 5, 20, 20);
		g.setColor(in.isKeyDown(p.getControlRight()) ? Color.red : Color.white);
		g.fillRect(x + 30, y + 5, 20, 20);
		g.setColor(in.isKeyDown(p.getControlJump()) ? Color.red : Color.white);
		g.fillRect(x + 55, y + 5, 60, 20);
		
		g.setColor(Color.black);
		g.fill(Utils.createEqTriangle(x+10, y+7, 12, Direction.WEST));
		g.fill(Utils.createEqTriangle(x+33, y+7, 12, Direction.EAST));
		g.fill(Utils.createEqTriangle(x+80, y+8, 12, Direction.NORTH));
	}
	
	/**
	 * Dibujar tres triángulos en forma de otro triángulo
	 * @param g El objetivo en el cual dibujar
	 * @param x La posición inicial X
	 * @param y La posición inicial Y
	 * @param tsize El tamaño de cada triángulo
	 */
	public static void drawTriforce(Graphics g, int x, int y, int tsize) {
		g.setColor(Color.yellow);

		g.fill(Utils.createEqTriangle(x+(tsize/2)+1, y, tsize, Direction.NORTH));
		g.fill(Utils.createEqTriangle(x, y+tsize, 12, Direction.NORTH));
		g.fill(Utils.createEqTriangle(x+tsize, y+tsize, 12, Direction.NORTH));
	}
}
