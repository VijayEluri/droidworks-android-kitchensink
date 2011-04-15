package com.droidworks.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
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
    
    // TODO, one day i might fix this to make sense...
    @Deprecated
    public static void tweetMessage(Context context, String message) {
    	Intent tweetIntent = new Intent(Intent.ACTION_SEND);
    	tweetIntent.putExtra(Intent.EXTRA_TEXT, message);
    	tweetIntent.setType("application/twitter");

    	PackageManager pm = context.getPackageManager();
    	List<ResolveInfo> lract 
    	= pm.queryIntentActivities(tweetIntent,
    	    PackageManager.MATCH_DEFAULT_ONLY);

    	boolean resolved = false;

    	for(ResolveInfo ri: lract)
    	{
    	    if(ri.activityInfo.name.endsWith(".SendTweet"))
    	    {
    	        tweetIntent.setClassName(ri.activityInfo.packageName,
    	                        ri.activityInfo.name);
    	        resolved = true;
    	        break;
    	    }
    	}
    	
    	context.startActivity(resolved ? tweetIntent :
    	    Intent.createChooser(tweetIntent, "Choose one"));    	
    }

    public static boolean tweetNativeApp(Context context, String message) {
    	boolean rv = false;

    	try{
    		Intent intent = new Intent(Intent.ACTION_SEND);
    		intent.putExtra(Intent.EXTRA_TEXT, message);
    		intent.setType("text/plain");
    		final PackageManager pm = context.getPackageManager();
    		final List<?> activityList = pm.queryIntentActivities(intent, 0);
    	        int len =  activityList.size();
    		for (int i = 0; i < len; i++) {
    			final ResolveInfo app = (ResolveInfo) activityList.get(i);
    			if ("com.twitter.android.PostActivity".equals(app.activityInfo.name)) {
    				final ActivityInfo activity=app.activityInfo;
    				final ComponentName name=new ComponentName(activity.applicationInfo.packageName, activity.name);
    				intent=new Intent(Intent.ACTION_SEND);
    				intent.addCategory(Intent.CATEGORY_LAUNCHER);
    				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
    				intent.setComponent(name);
    				intent.putExtra(Intent.EXTRA_TEXT, message);
    				context.startActivity(intent);
    				rv = true;
    				break;
    			}
    		}
    	}
    	catch(final ActivityNotFoundException ignored) {
    		// do nothing
    	}

    	return rv;
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
