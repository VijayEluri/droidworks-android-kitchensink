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

package com.droidworks.util;

import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author jasonhudgins
 *
 */
public class GuidManager {

	private final Context mContext;

	public static final String PREFS_GUID = "com.droidworks.DROIDWORKS_GUID";
	public static final String KEY_GUID = "guid";

	public GuidManager(Context context) {
		super();
		mContext = context;
	}

	public String getGuid() {
		String guid = null;

		SharedPreferences prefs = mContext.getSharedPreferences(PREFS_GUID,
				Context.MODE_WORLD_WRITEABLE);

		// if we already have a guid, just return it
		if (prefs.contains(KEY_GUID))
			return prefs.getString(KEY_GUID, "no guid");

		// otherwise we generate it and
		guid = UUID.randomUUID().toString();

		// save it
		Editor editor = prefs.edit();
		editor.putString(KEY_GUID, guid);
		editor.commit();

		// before returning it.
		return guid;
	}

}
