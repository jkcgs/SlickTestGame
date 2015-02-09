package com.makzk.games.util;

import java.util.HashMap;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.util.Log;

public class SoundManager {
	HashMap<String, Sound> sounds = new HashMap<String, Sound>();

	public void add(String name, Sound snd) {
		if(!sounds.containsKey(name)) {
			Log.info("Loading sound: " + name);
			sounds.put(name, snd);
		} else {
			Log.info("Sound named " + name + " already loaded.");
		}
	}
	
	public void add(String name, String ref) throws SlickException {
		Sound snd = new Sound(ref);
		add(name, snd);
	}
	
	public Sound get(String name) {
		if(loaded(name)) {
			return sounds.get(name);
		} else {
			return null;
		}
	}
	
	public boolean loaded(String name) {
		return sounds.containsKey(name);
	}

	public void play(String name, float volume) {
		if(loaded(name)) {
			get(name).play(1.0f, volume);
		} else {
			Log.warn("The requested sound to play '" + name + "' is not loaded.");
		}
	}

	public void play(String name) {
		play(name, 1);
	}
}
