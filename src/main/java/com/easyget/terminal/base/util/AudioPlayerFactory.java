package com.easyget.terminal.base.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.media.AudioClip;

public class AudioPlayerFactory {
	private static AudioPlayerFactory INSTANCE;

	private Map<String, AudioPlayer> audioPlayerMap;

	private AudioPlayerFactory() {
		audioPlayerMap = new HashMap<String, AudioPlayer>();
	}

	public static AudioPlayerFactory getInstance() {
		if (null == INSTANCE) {
			INSTANCE = new AudioPlayerFactory();
		}
		return INSTANCE;
	}

	public synchronized void playByFileName(String audioFileName) {
		Collection<AudioPlayer> values = audioPlayerMap.values();
		for (AudioPlayer p : values) {
			if (p.isPlaying()) {
				return;
			}
		}
		AudioPlayer player = null;
		if (audioPlayerMap.containsKey(audioFileName)) {
			player = audioPlayerMap.get(audioFileName);
		} else {
			AudioClip audioClip = new AudioClip(audioFileName);
			player = new AudioPlayer(audioClip);
			audioPlayerMap.put(audioFileName, player);
		}
		player.play();
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
		}
	}
}