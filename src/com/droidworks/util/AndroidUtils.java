package com.droidworks.util;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;

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

}
