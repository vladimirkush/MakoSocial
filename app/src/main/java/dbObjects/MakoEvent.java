package dbObjects;

import java.util.ArrayList;
import java.util.Date;

/**
 * Class holds a row that represents a Mako event
 */
public class MakoEvent {
    private String id;
    private String name;
    private double rating;
    private String picture; // resource path
    private Date startDate;
    private int likes;
    private String description;
    private ArrayList<Comment> comments;

    public MakoEvent(String id, String name, double rating, String picture, Date startDate,
                     int likes, String description, ArrayList<Comment> comments) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.picture = picture;
        this.startDate = startDate;
        this.likes = likes;
        this.description = description;
        this.comments = comments;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
