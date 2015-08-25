package com.droidworks.misc;

import android.content.ComponentName;
import android.media.AudioManager;
import android.os.Build;

@Deprecated
public abstract class MediaButtonManager {

	public abstract void register(AudioManager audioManager, ComponentName component);

	public abstract void unregister(AudioManager audioManager, ComponentName component);


	public static MediaButtonManager getInstance() {
		if (Integer.parseInt(Build.VERSION.SDK) <= 7) {
	            return PreFroyo.Holder.sInstance;
		}
	    else {
	            return FroyoAndBeyond.Holder.sInstance;
	    }
	}

	private static class PreFroyo extends MediaButtonManager {

		private static class Holder {
			private static final PreFroyo sInstance = new PreFroyo();
	    }

		@Override
		public void register(AudioManager audioManager, ComponentName component) {
			// do nothing
		}

		@Override
		public void unregister(AudioManager audioManager, ComponentName component) {
			// do nothing
		}
	}

	private static class FroyoAndBeyond extends MediaButtonManager {

		private static class Holder {
			private static final FroyoAndBeyond sInstance = new FroyoAndBeyond();
	    }

		@Override
		public void register(AudioManager audioManager, ComponentName component) {
			audioManager.registerMediaButtonEventReceiver(component);
		}

		@Override
		public void unregister(AudioManager audioManager, ComponentName component) {
			audioManager.unregisterMediaButtonEventReceiver(component);
		}

	}

}
