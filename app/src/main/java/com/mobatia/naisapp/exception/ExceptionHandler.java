/**
 * 
 */
package com.mobatia.naisapp.exception;

import android.app.Application;
import android.content.Context;
import android.content.Intent;


import com.mobatia.naisapp.activities.splash.SplashActivity;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author RIJO K JOSE
 * 
 */
public class ExceptionHandler extends Application implements
		Thread.UncaughtExceptionHandler {

	/** The mContext */
	private static Context mContext;

	/** The mIntent */
	private Intent mIntent;

	public ExceptionHandler() {
		ExceptionHandler.mContext = null;
	}

	public ExceptionHandler(Context mContext) {
		ExceptionHandler.mContext = mContext;
	}

	public ExceptionHandler(Context mContext, Class<?> mClass) {
		ExceptionHandler.mContext = mContext;
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

	public static Context getContext() {
		return mContext;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see Application#onCreate()
	 */

	@Override
	public void onCreate() {
		mContext = this;
		super.onCreate();
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see Thread.UncaughtExceptionHandler#uncaughtException(Thread,
	 *      Throwable)
	 */

	@Override
	public void uncaughtException(Thread thread, Throwable exception) {
		StringWriter mStringWriter = new StringWriter();
		exception.printStackTrace(new PrintWriter(mStringWriter));
		System.out.println(mStringWriter);
		mIntent = new Intent(mContext, SplashActivity.class);
		String s = mStringWriter.toString();
		System.out.println("wizz exception " + s);
		// you can use this String to know what caused the exception and in
		// which Activity
		mIntent.putExtra("uncaught Exception",
				"Exception is: " + mStringWriter.toString());
		mIntent.putExtra("uncaught stacktrace", s);
		mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mContext.startActivity(mIntent);
		System.out.println("error " + s);
		// for restarting the Activity
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}
}
