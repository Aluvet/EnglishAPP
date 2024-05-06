package com.example.overapp.database;

import org.litepal.crud.LitePalSupport;

public class FolderLinkWord extends LitePalSupport {
//单词本存放单词的id
    private int wordId;
//所shu6单词本
    private int folderId;

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }
}

