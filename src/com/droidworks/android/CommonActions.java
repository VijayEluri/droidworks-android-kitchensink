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

package com.droidworks.android;

/**
 * Class to hold common action strings for android broadcasts and intents.
 *
 * @author Jason Hudgins <jasonleehudgins@gmail.com>
 *
 */
public class CommonActions {

	public static final String ACTION_BT_HEADSET_CHANGED = "android.bluetooth.headset.action.STATE_CHANGED";
	public static final String EXTRA_BT_HEADSET_STATE = "android.bluetooth.headset.extra.STATE";
	public static final String EXTRA_BT_HEADSET_PREVIOUS_STATE = "android.bluetooth.headset.extra.PREVIOUS_STATE";

	// Handle calls to stock pause
	public static final String ACTION_STOCK_MUSIC_SERVICE_COMMAND = "com.android.music.musicservicecommand";
	public static final String EXTRA_STOCK_MUSIC_COMMAND = "command";
	public static final String EXTRA_STOCK_MUSIC_PAUSE = "pause";

}
