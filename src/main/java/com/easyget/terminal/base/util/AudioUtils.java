package com.easyget.terminal.base.util;

import org.apache.log4j.Logger;

import javafx.scene.media.AudioClip;

/**
 * 播放声音工具类
 */
public class AudioUtils {
	
	private static Logger logger = Logger.getLogger(AudioUtils.class);
	
	private static AudioUtils INSTANCE = new AudioUtils();

	private AudioUtils() {

	}

	public static AudioUtils getInstance() {
		return INSTANCE;
	}

	/**
	 * 播放条码扫描成功提示声音
	 */
	public void playScanBarcode() {
		playSysAudio("scanner.wav");
	}

	/**
	 * 播放"欢迎使用云柜"提示声音
	 */
	public void playWelcom() {
		playSysAudio("welcom.wav");
	}

	/**
	 * 播放"请关闭格口"提示声音
	 */
	public void playCloseCell() {
		playSysAudio("cellClosePrompt.wav");
	}

	/**
	 * 播放"打开格口失败"提示声音
	 */
	public void playOpenCellFial() {
		playSysAudio("openCellFail.wav");
	}
	/**
	 * 播放"请选择充值金额"提示声音
	 */
	public void playSelectRechargeMoney() {
		playSysAudio("selectRechargeMoney.wav");
	}
	/**
	 * 播放"请取件"提示声音
	 */
	public void playTakeExpPrompt() {
		playSysAudio("takeExpPrompt.wav");
	}
	/**
	 * 播放"抽奖"提示声音
	 */
	public void playAwardSound() {
		playSysAudio("award.wav");
	}
	/**
	 * 播放"抽奖"提示声音
	 */
	public void playPutExpSuccess() {
		playSysAudio("putExpSuccess.wav");
	}
	/**
	 * 播放"抽奖"提示声音
	 */
	public void playActionFail() {
		playSysAudio("actionFail.wav");
	}
	/**
	 * 播放"键盘"提示声音
	 */
	public void playKeyPanClick() {
		String audioFilePath = this.getClass().getResource("/base/audio/keyPanClick.wav").toExternalForm();
		
		AudioPlayer player = null;
		AudioClip audioClip = new AudioClip(audioFilePath);
		player = new AudioPlayer(audioClip);
		player.play();
	}

	/**
	 * 播放系统预设音频
	 * @param fileName 音频文件名称，包含后缀名
	 */
	private void playSysAudio(String fileName) {
		logger.debug("playByFileName:" + fileName);
		String audioFilePath = this.getClass().getResource("/base/audio/" + fileName).toExternalForm();
		play(audioFilePath);
	}

	public void play(final String audioFileName) {
		new Thread("Thread_AudioUtil-play") {
			@Override
			public void run() {
				AudioPlayerFactory.getInstance().playByFileName(audioFileName);
			}
		}.start();
	}
}