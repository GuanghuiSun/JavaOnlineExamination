package com.Public.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class TestPaper implements Serializable {
    private static final long serialVersionUID = 42L;
    private Map<String,String> firstItem;
    private ArrayList<String> secondItem;

    public TestPaper(){}

    /**
     * 组卷
     * @param firstItem 客观题
     * @param secondItem 主观题
     */
    public TestPaper(Map<String, String> firstItem, ArrayList<String> secondItem) {
        this.firstItem = firstItem;
        this.secondItem = secondItem;
    }

    public Map<String, String> getFirstItem() {
        return firstItem;
    }

    public void setFirstItem(Map<String, String> firstItem) {
        this.firstItem = firstItem;
    }

    public ArrayList<String> getSecondItem() {
        return secondItem;
    }

    public void setSecondItem(ArrayList<String> secondItem) {
        this.secondItem = secondItem;
    }
}
