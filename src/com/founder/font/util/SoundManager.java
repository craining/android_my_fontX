package com.founder.font.util;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.founder.font.R;

public class SoundManager {

	static private SoundManager _instance;
	private static SoundPool mSoundPool;
	private static HashMap<Integer, Integer> mSoundPoolMap;
	private static AudioManager mAudioManager;
	private static Context mContext;

	private SoundManager() {
	}

	/**
	 ************************************************************* 
	 * @brief: 返回SoundManager的一个实例，如果不存在就创建
	 * @author: $Author: an
	 * @return 返回SoundManager的一个实例
	 ************************************************************* 
	 */
	static synchronized public SoundManager getInstance() {
		if (_instance == null)
			_instance = new SoundManager();
		return _instance;
	}

	/**
	 ************************************************************* 
	 * @brief: 初始化存储的声音
	 * @author: $Author: an
	 * @param context
	 *            上下文对象
	 ************************************************************* 
	 */
	public void initSounds(Context context) {
		mContext = context;
		mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
		mSoundPoolMap = new HashMap<Integer, Integer>();
		mAudioManager = (AudioManager) mContext
				.getSystemService(Context.AUDIO_SERVICE);
	}

	/**
	 ************************************************************* 
	 * @brief: Add a new Sound to the SoundPool
	 * @author: $Author: an
	 * @param Index
	 *            - The Sound Index for Retrieval
	 * @param SoundID
	 *            - The Android ID for the Sound asset.
	 ************************************************************* 
	 */
	public void addSound(int Index, int SoundID) {
		mSoundPoolMap.put(Index, mSoundPool.load(mContext, SoundID, 1));
	}

	/**
	 ************************************************************* 
	 * @brief: 从当前的assets加载各种不同的声音资源
	 * @author: $Author: an
	 ************************************************************* 
	 */
	public void loadSounds() {
		mSoundPoolMap.put(1, mSoundPool.load(mContext, R.raw.sound, 1));
	}

	/**
	 ************************************************************* 
	 * @brief: Plays a Sound
	 * @author: $Author: an
	 * @param index
	 *            - The Index of the Sound to be played
	 * @param speed
	 *            - The Speed to play not, not currently used but included for
	 *            compatibility
	 ************************************************************* 
	 */
	public void playSound(int index, float speed) {
		float streamVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume
				/ mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume,
				1, 0, speed);
	}

	/**
	 ************************************************************* 
	 * @brief: Stop a Sound
	 * @author: $Author: an
	 * @param index
	 *            - index of the sound to be stopped
	 ************************************************************* 
	 */
	public void stopSound(int index) {
		mSoundPool.stop(mSoundPoolMap.get(index));
	}

	/**
	 ************************************************************* 
	 * @brief: Cleanup a Sound
	 * @author: $Author: an
	 ************************************************************* 
	 */
	public void cleanup() {
		mSoundPool.release();
		mSoundPool = null;
		mSoundPoolMap.clear();
		mAudioManager.unloadSoundEffects();
		_instance = null;

	}

}