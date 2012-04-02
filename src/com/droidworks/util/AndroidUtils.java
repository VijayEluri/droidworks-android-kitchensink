package com.droidworks.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class AndroidUtils {

	public static String getDeviceId(Context context) {
		TelephonyManager manager = (TelephonyManager)
			context.getSystemService(Context.TELEPHONY_SERVICE);

		return manager.getDeviceId();
	}

	/**
	 * Simple method to test for wifi connectivity.
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiActive(Context context) {

		ConnectivityManager	mConnMgr = (ConnectivityManager)
			context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo.State wifiState = mConnMgr
			.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();

		if ( wifiState == NetworkInfo.State.CONNECTED) {
			return true;
		}

		return false;
	}
	
    // creates a null listener
    public static OnClickListener nullListener() {
        return new OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }
        };
    }

    public static Bitmap getBitmap(Drawable d) {
		int width = d.getIntrinsicWidth();
		int height = d.getIntrinsicHeight();
		d.setBounds(0, 0, width, height);
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		d.draw(canvas);
		return bitmap;
    }

    static public boolean hasStorage(boolean requireWriteAccess) {

        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if (requireWriteAccess) {
                boolean writable = checkFsWritable();
                return writable;
            }
            else {
                return true;
            }
        }
        else if (!requireWriteAccess
        		&& Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {

            return true;
        }
        return false;
    }

    // Create a temporary file to see whether a volume is really writeable.
    // It's important not to put it in the root directory which may have a
    // limit on the number of files.
    static private boolean checkFsWritable() {
        String directoryName = Environment
        	.getExternalStorageDirectory().toString() + "/dcim";

        File directory = new File(directoryName);

        if (!directory.isDirectory()) {
            if (!directory.mkdirs()) {
                return false;
            }
        }

        File f = new File(directoryName, ".probe");
        try {
            // Remove stale file if any
            if (f.exists()) {
                f.delete();
            }
            if (!f.createNewFile())
                return false;
            f.delete();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public static int getScaledPixels(Context ctx, int unscaled) {
    	return (int) (unscaled * ctx.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * Utility method to test if a package is installed or not.
     *
     * @param context
     * @param packagName
     * @return
     */
    public static boolean isPackageInstalled(Context context, String packagName) {
		// test to see if tunewiki is installed
		boolean rv = true;

    	try {
			PackageManager pm = context.getPackageManager();
			pm.getPackageInfo(packagName, 0);
		}
		catch (NameNotFoundException e) {
			rv = false;
		}

		return rv;
    }
    
    // TODO, testing
    public static void share(Context context, String subject, String body) {
    	
    	Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
   // 	sharingIntent.setType("text/plain");
    	sharingIntent.putExtra(Intent.EXTRA_TEXT, body);
    	sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
    	sharingIntent.setType("application/twitter");
    	
    	context.startActivity(Intent.createChooser(sharingIntent, "choose method"));	
    }

    public static void sendEmail(Context context, String subject, String body,
    		String error) {

		Intent i = new Intent(Intent.ACTION_SEND);

		i.putExtra(Intent.EXTRA_SUBJECT, subject);
		i.putExtra(Intent.EXTRA_TEXT, body);
		i.setType("message/rfc822");

		try {
			context.startActivity(i);
		}
		catch (ActivityNotFoundException e) {
			Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
		}
    }
    
    /**
     * Method to find a twitter client on the device.  I lifted this from :
     * http://regis.decamps.info/blog/2011/06/intent-to-open-twitter-client-on-android/
     * 
     * @param context
     * @return
     */
    public static Intent findTwitterClient(Context context) {
		final String[] twitterApps = {
				// package // name - nb installs (thousands)
				"com.twitter.android", // official - 10 000
				"com.twidroid", // twidroyd - 5 000
				"com.handmark.tweetcaster", // Tweecaster - 5 000
				"com.thedeck.android" // TweetDeck - 5 000 
		};
		Intent tweetIntent = new Intent();
		tweetIntent.setType("text/plain");
		final PackageManager packageManager = context.getPackageManager();
		
		List<ResolveInfo> list = packageManager.queryIntentActivities(
				tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

		for (int i = 0; i < twitterApps.length; i++) {
			for (ResolveInfo resolveInfo : list) {
				String p = resolveInfo.activityInfo.packageName;
				if (p != null && p.startsWith(twitterApps[i])) {
					tweetIntent.setPackage(p);
					return tweetIntent;
				}
			}
		}
		
		return null;
	}

    /**
     * Method to send a tweet.
     * 
     * @param context
     * @param message
     * @return
     */
    public static boolean tweet(Context context, String message) {

    	try {
    		Intent intent = findTwitterClient(context);
    		
    		if (intent == null)
    			return false;
    		
    		intent.setAction(Intent.ACTION_SEND);
    		intent.putExtra(Intent.EXTRA_TEXT, message);
    		intent.setType("text/plain");
    		context.startActivity(intent);
    	}
    	catch(final ActivityNotFoundException ignored) {
    		return false;
    	}

    	return true;
    }

	/**
	 * Returns true when platform version is lower or equal to 1.5
	 * Since prior to 1.5 there was no Build.VERSION.SDK_INT available.
	 *
	 * @return
	 */
	public static boolean isAPILevelLower4() {
		return "1.5".compareTo(Build.VERSION.RELEASE) >= 0;
	}

	@SuppressWarnings("unchecked")
	public static int getAPINumber() {
		// SDK_INT is available from API 4
		if (isAPILevelLower4()) return 3;
		int version = 3;
		try {
			Class buildClass = Build.VERSION.class;
			Field sdkint = buildClass.getField("SDK_INT");
			version = sdkint.getInt(null);
		} catch (Exception ignore) {}
		return version;
	}

}
