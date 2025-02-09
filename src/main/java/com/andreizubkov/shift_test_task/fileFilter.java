package com.andreizubkov.shift_test_task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class fileFilter {
    private File integers;
    private File floats;
    private File strings;
    private File[] inputs;

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

    public String getIntegersPath() {
        return integers.getAbsolutePath();
    }

    public String getFloatsPath() {
        return floats.getAbsolutePath();
    }

    public String getStringsPath() {
        return strings.getAbsolutePath();
    }

    public String[] getInputPath() {
        String[] result = new String[inputs.length];
        for (int i = 0; i < inputs.length; i++) result[i] = inputs[i].getAbsolutePath();
        return result;
    }

    public void filter(boolean append, boolean short_stat, boolean full_stat) {
        FileWriter writeInt = null;
        FileWriter writeFlt = null;
        FileWriter writeStr = null;

        long str_num = 0;
        long str_min = 0;
        long str_max = 0;
        long int_num = 0;
        long int_sum = 0;
        long int_min = 0;
        long int_max = 0;
        long flt_num = 0;
        BigDecimal flt_sum = new BigDecimal("0");
        double flt_min = 0;
        double flt_max = 0;
        long cur_int = 0;
        double cur_flt = 0;
        double avg = 0;

        try {
            BufferedReader[] reader = new BufferedReader[inputs.length];
            for (int i = 0; i < inputs.length; i++) {
                try {
                    reader[i] = new BufferedReader(new FileReader(inputs[i]));
                } catch (FileNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            String s;
            int i = 0;
            while (true) {
                try {
                    if (reader[i] != null && (s = reader[i].readLine()) != null) {
                        if (s.equals("")) continue;
                        try {
                            if (s.contains(".")) {
                                cur_flt = Double.parseDouble(s);
                                if (writeFlt == null) writeFlt = new FileWriter(floats, append);
                                writeFlt.write(s + '\n');
    
                                flt_num++;
                                flt_sum = flt_sum.add(new BigDecimal(s));
                                if (flt_num == 1) {
                                    flt_min = cur_flt;
                                    flt_max = cur_flt;
                                } else {
                                    if (cur_flt < flt_min) flt_min = cur_flt;
                                    if (cur_flt > flt_max) flt_max = cur_flt;
                                }
                            } else {
                                cur_int = Long.parseLong(s);
                                if (writeInt == null) writeInt = new FileWriter(integers, append);
                                writeInt.write(s + '\n');
    
                                int_num++;
                                int_sum += cur_int;
                                if (int_num == 1) {
                                    int_min = cur_int;
                                    int_max = cur_int;
                                } else {
                                    if (cur_int < int_min) int_min = cur_int;
                                    if (cur_int > int_max) int_max = cur_int;
                                }
                            }
                        } catch (NumberFormatException ex) {
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
                        }
                    } else {
                        if (reader[i] != null) {
                            reader[i].close();
                            reader[i] = null;
                        }
                    }
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
                
                if (i == reader.length - 1) {
                    i = 0;
                } else {
                    i++;
                }
                boolean close = true;
                for (int j = 0; j < reader.length; j++) {
                    if (reader[j] != null) close = false;
                }
                if (close == true) break;
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
            if (flt_num != 0) {
                avg = flt_sum.doubleValue() / flt_num;
            } else {
                avg = 0;
            }
            System.out.println("    sum = " + flt_sum + "\n    average = " + avg);
        }
    }
}
