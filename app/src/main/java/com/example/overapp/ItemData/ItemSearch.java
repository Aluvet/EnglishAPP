package com.example.overapp.ItemData;

public class ItemSearch {
//    成员变量
    private int wordId;
    private String wordName;
    private String wordSound;
    private String wordMean;
//    有参构造器
    public ItemSearch(int wordId,String wordName ,String wordSound ,String wordMean){
//        成员等于传入
        this.wordId=wordId;
        this.wordName =wordName;
        this.wordSound=wordSound;
        this .wordMean =wordMean;

    }
//    设置get set方法


    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public String getWordMean() {
        return wordMean;
    }

    public void setWordMean(String wordMean) {
        this.wordMean = wordMean;
    }

    public String getWordName() {
        return wordName;
    }

    public void setWordName(String wordName) {
        this.wordName = wordName;
    }

    public String getWordSound() {
        return wordSound;
    }

    public void setWordSound(String wordSound) {
        this.wordSound = wordSound;
    }
}
