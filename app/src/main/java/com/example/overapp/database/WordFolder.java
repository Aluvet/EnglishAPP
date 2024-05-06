package com.example.overapp.database;


import org.litepal.crud.LitePalSupport;

public class WordFolder extends LitePalSupport {
//单词本名字
   private int id;

//单词本的名字，备注
    private String name;

    private String remark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
