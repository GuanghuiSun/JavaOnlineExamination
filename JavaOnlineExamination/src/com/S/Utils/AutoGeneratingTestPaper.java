package com.S.Utils;

import com.Public.pojo.TestPaper;

import java.io.*;
import java.util.*;

public class AutoGeneratingTestPaper {
    private static BufferedReader br1 = null;
    private static BufferedReader br2 = null;

    /**
     * 自动组卷
     * @return
     */
    public static TestPaper getPaper(int num1, int num2) {
        try {
            br1 = new BufferedReader(new FileReader("./src/com/S/Files/题库/客观题.txt"));
            br2 = new BufferedReader(new FileReader("./src/com/S/Files/题库/主观题.txt"));

            //客观题
            //没有办法做到完全随机
            Map<String, String> allItem1 = new HashMap<>();//所有的题目信息
            String answer ;
            String question = "";
            String line = br1.readLine();
            while (line != null) {
                if ("A".equals(line) || "B".equals(line) || "C".equals(line) || "D".equals(line)) {
                    answer = line;
                    allItem1.put(question, answer);
                    question = "";
                } else {
                    question += line;
                    question += "-";
                }
                line = br1.readLine();
            }
            Set<String> keys = allItem1.keySet();
            ArrayList<String> container = new ArrayList<>();//用于打乱顺序的容器
            for(String key : keys){
                container.add(key);
            }
            Collections.shuffle(container);
            Map<String, String> item1 = new HashMap<>();//最终大哥客观题部分
            //将打乱顺序后的集合，选取制定个题目写入map中
            for(int i = 0; i < num1; i++){
                String key = container.get(i);
                String value = allItem1.get(key);
                item1.put(key,value);
            }
            //主观题，随机产生题目
            ArrayList<String> item2 = new ArrayList<>();//用于打乱顺序的容器
            String q;
            while ((q = br2.readLine()) != null ) {
                item2.add(q);
            }
            Collections.shuffle(item2);
            ArrayList<String> item = new ArrayList<>();//打乱后的主观题
            for(int i = 0; i < num2; i++){
                item.add(item2.get(i));
            }
            return new TestPaper(item1, item);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br1 != null) {
                try {
                    br1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (br2 != null) {
                try {
                    br2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

}
