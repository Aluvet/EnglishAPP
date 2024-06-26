package com.example.overapp.database;

import org.litepal.crud.LitePalSupport;

public class Phrase extends LitePalSupport {
//短语
    // 中文短语
    private String chsPhrase;

    // 英语短语
    private String enPhrase;

    // 归属单词
    private int wordId;

    public String getChsPhrase() {
        return chsPhrase;
    }

    public void setChsPhrase(String chsPhrase) {
        this.chsPhrase = chsPhrase;
    }

    public String getEnPhrase() {
        return enPhrase;
    }

    public void setEnPhrase(String enPhrase) {
        this.enPhrase = enPhrase;
    }

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }
}
