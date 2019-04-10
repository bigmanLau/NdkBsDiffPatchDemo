package com.example.ndkpatchfile.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;


import com.example.ndkpatchfile.BuildConfig;

import java.io.File;


public class ApkUtils {

	public static boolean isInstalled(Context context, String packageName) {
		PackageManager pm = context.getPackageManager();
		boolean installed = false;
		try {
			pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			installed = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return installed;
	}

	/**
	 * 获取已安装Apk文件的源Apk文件
	 * 如：/data/app/my.apk
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static String getSourceApkPath(Context context, String packageName) {
		if (TextUtils.isEmpty(packageName))
			return null;

		try {
			ApplicationInfo appInfo = context.getPackageManager()
					.getApplicationInfo(packageName, 0);
			return appInfo.sourceDir;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 安装Apk
	 * 
	 * @param context
	 * @param apkPath
	 */
	public static void installApk(Context context, String apkPath) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			/* Android N 写法*/
			intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", new File(apkPath));
			intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
		} else {
			/* Android N之前的老版本写法*/
			intent.setDataAndType(Uri.fromFile(new File("apk地址")), "application/vnd.android.package-archive");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		context.startActivity(intent);


	}
}