package com.example.overapp.ItemData;

public class ItemShowWord {

    private int wordId;

    private String word;

    private String wordMean;

    private boolean isStar;

    public ItemShowWord(int wordId, String word, String wordMean, boolean isStar) {
        this.wordId = wordId;
        this.word = word;
        this.wordMean = wordMean;
        this.isStar = isStar;
    }

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWordMean() {
        return wordMean;
    }

    public void setWordMean(String wordMean) {
        this.wordMean = wordMean;
    }

    public boolean isStar() {
        return isStar;
    }

    public void setStar(boolean star) {
        isStar = star;
    }

}
