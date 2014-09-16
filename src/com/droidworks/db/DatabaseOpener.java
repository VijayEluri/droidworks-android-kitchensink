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

package com.droidworks.db;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.droidworks.util.StringUtils;

/**
 * @author jasonhudgins
 *
 */
public class DatabaseOpener extends SQLiteOpenHelper {

	protected final Context mContext;
	private String mName;

    public DatabaseOpener(Context context, String name,
			CursorFactory factory, int version) {

		super(context, name, factory, version);
		mName = name;
		mContext = context;
	}

    // called by onCreate
    protected boolean executeScript(String sqlScript, SQLiteDatabase db) {

    	if (null == db) {
    		Log.e(getClass().getName(), "Database param is null!");
    		return false;
    	}
		try {
			AssetManager am = mContext.getAssets();
			String sql = StringUtils.slurp(am.open(sqlScript));

			// iterate over sql commands, remove linefeeds, ignore
			// comments, and execute each statement in the script
			String[] commands = sql.split(";");
			for (String c : commands) {
				c = c.replace("\n", "").trim();
				if (c.length() > 0 && !c.startsWith("#"))
					db.execSQL(c);
			}
			return true;
		}
		catch (FileNotFoundException e) {
			Log.e(getClass().getName(),
				String.format("unable to open script file: [%1$s]", sqlScript), e);
			db = null;
		}
		catch (IOException e) {
			Log.e(getClass().getName(),
				String.format(
						"IO error initialzing database from [%1$s]", sqlScript), e);
			db = null;
		}

		return false;
    }

    protected String getScriptNameCreate() {
    	return mName + "_create.sql";
    }

	@Override
    public void onCreate(SQLiteDatabase db) {
		String filename = getScriptNameCreate();
		db.beginTransaction();
		try {
			if (executeScript(filename, db) ) {
				db.setTransactionSuccessful();
			}
		}
		finally {
			db.endTransaction();
		}
    }

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

    protected String getName() {
        return mName;
    }

}

