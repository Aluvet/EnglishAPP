package com.example.overapp.ItemData;

public class ItemWordListContent {
//    创建itemwordlistcontent的实体类
    private int wordId;
    private  String wordName;
    private  String wordMean;
    private  boolean isClick;
    private  boolean isSearch;

    public ItemWordListContent(int wordId,String wordName ,String wordMean,boolean isClick,boolean isSearch){
        this.wordId=wordId;
        this.wordName=wordName;
        this.wordMean=wordMean;
        this.isClick=isClick;
        this.isSearch=isSearch;

    }

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public String getWordName() {
        return wordName;
    }

    public void setWordName(String wordName) {
        this.wordName = wordName;
    }

    public String getWordMean() {
        return wordMean;
    }

    public void setWordMean(String wordMean) {
        this.wordMean = wordMean;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public boolean isSearch() {
        return isSearch;
    }

    public void setSearch(boolean search) {
        isSearch = search;
    }
}
