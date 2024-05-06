package com.example.overapp.ItemData;

public class ItemWordFold {
// 驼铃
    private int foldId;

    private String foldName;
    private  String foldRemark;
    private  int wordNum;

//    有参构造其
// 有参构造函数
public ItemWordFold(int foldId, String foldName, String foldRemark, int wordNum) {
    this.foldId = foldId;
    this.foldName = foldName;
    this.foldRemark = foldRemark;
    this.wordNum = wordNum;
}

    public int getFoldId() {
        return foldId;
    }

    public void setFoldId(int foldId) {
        this.foldId = foldId;
    }

    public int getWordNum() {
        return wordNum;
    }

    public void setWordNum(int wordNum) {
        this.wordNum = wordNum;
    }

    public String getFoldName() {
        return foldName;
    }

    public void setFoldName(String foldName) {
        this.foldName = foldName;
    }

    public String getFoldRemark() {
        return foldRemark;
    }

    public void setFoldRemark(String foldRemark) {
        this.foldRemark = foldRemark;
    }
}
