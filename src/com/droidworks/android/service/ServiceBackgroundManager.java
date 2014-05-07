/**
 * Copyright 2010 Jason L. Hudgins
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.droidworks.android.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.os.Build;

/**
 * This code in this class is borrowed heavily from a blog post by Josh Guilfoyle
 *
 *  http://devtcg.blogspot.com/2009/12/gracefully-supporting-multiple-android.html
 *
 * @author jasonhudgins
 *
 */
@Deprecated
public abstract class ServiceBackgroundManager {

	public static ServiceBackgroundManager getInstance() {
        return EclairAndBeyond.Holder.sInstance;
	}

	public abstract void showNotification(Service context, int id, Notification notification);

	public abstract void hideNotification(Service context, int id);

	private static class EclairAndBeyond extends ServiceBackgroundManager {

		private static class Holder {
	            private static final EclairAndBeyond sInstance = new EclairAndBeyond();
	    }

	    public void showNotification(Service context, int id, Notification n) {
	    	context.startForeground(id, n);
	    }

	    public void hideNotification(Service context, int id) {
	    	context.stopForeground(true);
	    }
	}


}
