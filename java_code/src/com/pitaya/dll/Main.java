package com.pitaya.dll;

import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {
        //得到差分包
        System.out.println("开始拆分");
        final int[] count = {0};
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                count[0]++;
                System.out.println(String.format("已进行了%d秒", count[0]));
            }
        }, 0,1000);
        BsDiffUtil.diff(ConstantsW.OLD_APK_PATH, ConstantsW.NEW_APK_PATH, ConstantsW.PATCH_PATH);
        timer.cancel();
        System.out.println("拆分完成");
    }
}
