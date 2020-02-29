package com.example.notes;

public class Notes {

    private long id;
    private String title;
    private String description;
    private String date;
    private String time;

    // Constructors
    Notes(){

    }

    Notes(String title , String description, String time , String date){
        this.title = title;
        this.description = description;
        this.time = time;
        this.date = date;

    }

    Notes(long id,String title , String description, String time , String date){
        this.id= id;
        this.title = title;
        this.description = description;
        this.time = time;
        this.date = date;
    }



    // Getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
