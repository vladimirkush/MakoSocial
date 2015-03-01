package dbObjects;

import java.util.Date;

/**
 * Class holds a single comment
 * made by a specific user
 */
public class Comment {
    private String id;
    private Date dateCreated;
    private String author;      // user name
    private String text;        // value of a comment


    private String makoEventID;

    public Comment(){

    }

    public Comment(String id, Date dateCreated, String author, String text) {
        this.id = id;
        this.dateCreated = dateCreated;
        this.author = author;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMakoEventID() {
        return makoEventID;
    }

    public void setMakoEventID(String makoEventID) {
        this.makoEventID = makoEventID;
    }

}
