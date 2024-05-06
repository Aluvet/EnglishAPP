package com.example.overapp.ItemData;

public class ItemBook {
//    单词书 Id
    private int bookId;
//    单词书名称
    private  String bookName;
//    单词总量
    private  int wordNum;

//    单词书图片
    private  String img;

//    单词书来源

    private  String bookFrom;


    public ItemBook(int bookId, String bookName, int wordNum,String img ,String bookFrom){
        this.bookId =bookId;
        this.bookName =bookName;
        this.wordNum =wordNum;
        this.img =img;
        this.bookFrom =bookFrom;

    }
//    get set封装管理数据
//    getter方法检索

    public int getBookId() {
        return bookId;
    }

    public int getWordNum() {
        return wordNum;
    }

    public String getImg() {
        return img;
    }

    public String getBookFrom() {
        return bookFrom;
    }

    public String getBookName() {
        return bookName;
    }
//    setter方法修改

    public void setBookId(int bookid) {
        this.bookId = bookId;
    }

    public void setBookFrom(String bookFrom) {
        this.bookFrom = bookFrom;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setWordNum(int wordNum) {
        this.wordNum = wordNum;
    }
}
