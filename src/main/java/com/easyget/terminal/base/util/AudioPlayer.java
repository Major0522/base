package com.easyget.terminal.base.util;

import javafx.scene.media.AudioClip;

/**
 * 声音播放器
 * 
 * @author lidan
 * 
 */
public class AudioPlayer implements Runnable {

	private AudioClip audioClip;

	public AudioPlayer(AudioClip audioClip) {
		this.audioClip = audioClip;
	}

	/**
	 * 播放音频，如果当前AudioClip正在播放中，则跳过
	 */
	public void play() {
		if (audioClip.isPlaying()) {
			return;
		} else {
			run();
		}
	}

	public void stop() {
		audioClip.stop();
	}

	@Override
	public void run() {
		audioClip.play();
	}

	public boolean isPlaying() {
		return audioClip.isPlaying();
	}

}
