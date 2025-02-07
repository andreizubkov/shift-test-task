package com.andreizubkov.shift_test_task;

import java.util.ArrayList;

public class shiftTestTask {
    
    public static void main(String[] args) {
        String result_path = "";
        String result_prefix = "";
        boolean append = false;
        boolean short_stat = false;
        boolean full_stat = false;
        ArrayList<String> files = new ArrayList<String>();

        for (int i = 0; i < args.length; i++) {
            switch(args[i]) {
                case "-a":
                    append = true;
                    continue;
                case "-s":
                    short_stat = true;
                    continue;
                case "-f":
                    full_stat = true;
                    continue;
                case "-o":
                    result_path = args[i + 1];
                    i++;
                    continue;
                case "-p":
                    result_prefix = args[i + 1];
                    i++;
                    continue;
                default:
                    files.add(args[i]);
            }
        }

        fileFilter f = new fileFilter(files, result_path, result_prefix);
        f.filter(append, short_stat, full_stat);
    }
}