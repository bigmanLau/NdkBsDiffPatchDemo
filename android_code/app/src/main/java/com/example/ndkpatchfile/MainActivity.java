package com.example.ndkpatchfile;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.example.ndkpatchfile.utils.ApkUtils;
import com.example.ndkpatchfile.utils.BsPatch;
import com.example.ndkpatchfile.utils.Constants;
import com.example.ndkpatchfile.utils.DownloadUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity {



    private String SD_CARD_PATH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = findViewById(R.id.tv_title);
        tv.setText(BuildConfig.VERSION_NAME);
        SD_CARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

    }

    public void startDownload(View view) {
        new ApkUpdateTask().execute();
    }

    class ApkUpdateTask extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                //1.下载差分包
                Log.d("jason", "开始下载");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "开始下载", Toast.LENGTH_SHORT).show();

                    }
                });
                File patchFile = DownloadUtils.download(Constants.URL_PATCH_DOWNLOAD);

                //获取当前应用的apk文件/data/app/app
                String oldfile = ApkUtils.getSourceApkPath(MainActivity.this, getPackageName());
                //2.合并得到最新版本的APK文件
                String newfile = Constants.NEW_APK_PATH;
                String patchfile = patchFile.getAbsolutePath();
                BsPatch.patch(oldfile, newfile, patchfile);


            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            //3.安装
            if(result){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "您正在进行无流量更新", Toast.LENGTH_SHORT).show();

                    }
                });
                ApkUtils.installApk(MainActivity.this, Constants.NEW_APK_PATH);
            }
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
