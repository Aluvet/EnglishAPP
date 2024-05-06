package com.example.overapp.ItemData;
//在匹配中获取位置与id的数据
public class IdAndePositionAnalyse {
    private int wordId;

    private int position;

    public IdAndePositionAnalyse(int wordId, int position) {
        this.wordId = wordId;
        this.position = position;
    }

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
