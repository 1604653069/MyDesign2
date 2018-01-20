package com.xuf.www.gobang.util;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.xuf.www.gobang.R;

public class AudioService extends Service implements MediaPlayer.OnCompletionListener {
	MediaPlayer player;
	private final IBinder binder = new AudioBinder();
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	public void onCreate() {
		super.onCreate();

		player = MediaPlayer.create(this, R.raw.backgroundmusic);
		player.setOnCompletionListener(this);
		player.setLooping(true);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		if (!player.isPlaying()) {
			new MusicPlayThread().start();
		}
		else player.isPlaying();
		return START_STICKY;
	}

	

	public void onCompletion(MediaPlayer mp) {
		stopSelf();

	}

	public void onDestroy() {
		super.onDestroy();
		if (player.isPlaying()) {
			player.stop();
		}
		player.release();
	}


	public class AudioBinder extends Binder {

		public AudioService getService() {
			return AudioService.this;
		}
	}

	private class MusicPlayThread extends Thread {
		public void run() {
			if (!player.isPlaying()) {
				player.start();
			}
		}
	}
   
}