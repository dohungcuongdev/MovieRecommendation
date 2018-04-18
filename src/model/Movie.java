/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author nguyenminh
 */
public class Movie {
    
    private int id;
    private String title;
    private String genres;
    private String img;

    public Movie(String title) {
        this.title = title;
    }

    public Movie(int id, String title, String genres) {
        this.id = id;
        this.title = title;
        this.genres = genres;
    }
    
    public Movie(String id, String title, String genres) {
        this.id = Integer.parseInt(id);
        this.title = title;
        this.genres = genres;
    }
    
    

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
    
    public Movie() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getMId() {
        return id+"";
    }

    public Movie(int id, String title, String genres, String img) {
        this.id = id;
        this.title = title;
        this.genres = genres;
        this.img = img;
    }
    
    public Movie(String id, String title, String genres, String img) {
        this.id = Integer.parseInt(id);
        this.title = title;
        this.genres = genres;
        this.img = img;
    }

    @Override
    public String toString() {
        return "Movie{" + "id=" + id + ", title=" + title + ", genres=" + genres + ", img=" + img + '}';
    }
}
