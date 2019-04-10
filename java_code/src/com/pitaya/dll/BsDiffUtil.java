package com.pitaya.dll;

public class BsDiffUtil {
    public native static void diff(String oldfile, String newfile, String patchfile);

    static{
        System.load("E:\\eclipse_workplace\\ndkmerge\\code\\bsdiff\\x64\\Debug\\bsdiff.dll");
    }
}
