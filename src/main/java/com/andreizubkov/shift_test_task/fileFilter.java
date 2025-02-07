package com.andreizubkov.shift_test_task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class fileFilter {
    File integers;
    File floats;
    File strings;
    File[] inputs;

    public fileFilter(ArrayList<String> input_path, String result_path, String prefix) {
        inputs = new File[input_path.size()];
        for (int i = 0; i < inputs.length; i++) {
            inputs[i] = new File(input_path.get(i));
        }
        if (result_path == "") {
            integers = new File(System.getProperty("user.dir") + "\\" + prefix + "integers.txt");
            floats = new File(System.getProperty("user.dir") + "\\" + prefix + "floats.txt");
            strings = new File(System.getProperty("user.dir") + "\\" + prefix + "strings.txt");
        } else {
            integers = new File(result_path + "\\" + prefix + "integers.txt");
            floats = new File(result_path + "\\" + prefix + "floats.txt");
            strings = new File(result_path + "\\" + prefix + "strings.txt");
        }
    }

    public void filter(boolean append, boolean short_stat, boolean full_stat) {
        FileWriter writeInt = null;
        FileWriter writeFlt = null;
        FileWriter writeStr = null;

        int str_num = 0;
        int str_min = 0;
        int str_max = 0;
        int int_num = 0;
        int int_sum = 0;
        int int_min = 0;
        int int_max = 0;
        int flt_num = 0;
        double flt_sum = 0;
        double flt_min = 0;
        double flt_max = 0;
        int cur_int = 0;
        double cur_flt = 0;
        double avg = 0;

        try {
            for (int i = 0; i < inputs.length; i++) {
                try (BufferedReader reader = new BufferedReader(new FileReader(inputs[i]))) {
                    String s;
                    while ((s = reader.readLine()) != null) {
                        if (s.equals("")) continue;
                        switch(parse(s)) {
                            case "str":
                                if (writeStr == null) writeStr = new FileWriter(strings, append);
                                writeStr.write(s + '\n');

                                str_num++;
                                if (str_num == 1) {
                                    str_min = s.length();
                                    str_max = s.length();
                                } else {
                                    if (s.length() < str_min) str_min = s.length();
                                    if (s.length() > str_max) str_max = s.length();
                                }
                                break;
                            case "int":
                                if (writeInt == null) writeInt = new FileWriter(integers, append);
                                writeInt.write(s + '\n');

                                int_num++;
                                cur_int = Integer.parseInt(s);
                                int_sum += cur_int;
                                if (int_num == 1) {
                                    int_min = cur_int;
                                    int_max = cur_int;
                                } else {
                                    if (cur_int < int_min) int_min = cur_int;
                                    if (cur_int > int_max) int_max = cur_int;
                                }
                                break;
                            case "flt":
                                if (writeFlt == null) writeFlt = new FileWriter(floats, append);
                                writeFlt.write(s + '\n');

                                flt_num++;
                                cur_flt = Double.parseDouble(s);
                                flt_sum += cur_flt;
                                if (flt_num == 1) {
                                    flt_min = cur_flt;
                                    flt_max = cur_flt;
                                } else {
                                    if (cur_flt < flt_min) flt_min = cur_flt;
                                    if (cur_flt > flt_max) flt_max = cur_flt;
                                }
                                break;
                        }
                    }
                }
                catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
                catch (NumberFormatException ex) {
                    System.out.println("Cast error, stats may be wrong");    
                }
            }
        }
        finally {
            try {
                if (writeStr != null) writeStr.close();
                if (writeInt != null) writeInt.close();
                if (writeFlt != null) writeFlt.close();
            }
            catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

        if (short_stat) {
            System.out.println("\nStatistic:\nStrings:\n    quantity = " + str_num);
            System.out.println("Integers:\n    quantity = " + int_num);
            System.out.println("Floats:\n    quantity = " + flt_num);
        }
        if (full_stat) {
            System.out.println("\nStatistic:\nStrings:\n    quantity = " + str_num + "\n    min = " + str_min + "\n    max = " + str_max);
            System.out.println("Integers:\n    quantity = " + int_num + "\n    min = " + int_min + "\n    max = " + int_max);
            if (int_num != 0) avg = int_sum / int_num;
            System.out.println("    sum = " + int_sum + "\n    average = " + avg);
            System.out.println("Floats:\n   quantity = " + flt_num + "\n    min = " + flt_min + "\n    max = " + flt_max);
            if (flt_num != 0) avg = flt_sum / flt_num;
            System.out.println("    sum = " + flt_sum + "\n    average = " + avg);
        }
    }

    @SuppressWarnings("unused")
    private String parse(String s) {
        boolean isDouble = false;
        boolean isInt = true;
        int dot_counter = 0;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '.') {
                dot_counter++;
                isInt = false;
                isDouble = true;
                if (dot_counter > 1) { return "str"; }
                continue;
            }
            if (!Character.isDigit(s.charAt(i))) { return "str"; }
        }
        
        if (isDouble == true) {
            return "flt";
        } else {
            return "int";
        }
    }
}
