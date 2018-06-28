package com.example.tarek.bookslisting;



public class BookSrc {

    private String title,description,bookUrl;
    private String img;



    public BookSrc(String title, String description,String imgUrl, String bookUrl ) {
        this.title = title;
        this.description = description;
        this.img = imgUrl;
        this.bookUrl=bookUrl;

    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return description;
    }

    public String getImg() {
        return img;
    }
    public String getBookUrl() {
        return bookUrl;
    }

}
