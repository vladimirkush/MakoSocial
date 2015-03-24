package dbObjects;

import android.graphics.Bitmap;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Class holds a row that represents a Mako event
 */
public class MakoEvent {

    public final static String NAME_COL           = "Name";
    public final static String DESCRIPTION_COL    = "Description";
    public final static String NUM_LIKES_COL      = "NumLikes";
    public final static String START_DATE_COL     = "StartDate";
    public final static String RATING_COL         = "Rating";
    public final static String NUM_RATED_COL      = "numRated";
    public final static String PICTURE_COL        = "LargePicture";
    public final static String COMMENTS_COL       = "CommentsArr";
    public final static String SHORD_DESC_COL     = "ShortDescription";
    public final static String CHANNEL            = "channel";

    private String id;
    private String name;
    private float rating;
    private Bitmap picture;
    private Date startDate;
    private int numRated;
    private String description;
    private String shortDescription;
    private String link;
    private String channel;
    private ArrayList<HashMap<String, String>> comments;
    private ArrayList<MakoEventFact> facts;


    public MakoEvent(){
        comments = new ArrayList<HashMap<String, String>>();
        facts = new ArrayList<MakoEventFact>();
    }

    public MakoEvent(String id, String name, float rating,
                     int numRated, Bitmap picture, Date startDate,
                     int likes, String description, String shortDescription,
                     ArrayList<HashMap<String, String>> comments, ArrayList<MakoEventFact> facts) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.picture = picture;
        this.startDate = startDate;
        this.description = description;
        this.shortDescription = shortDescription;
        this.comments = comments;
        this.facts = facts;
        this.numRated = numRated;
    }

    public int getNumRated() { return numRated; }

    public void setNumRated(int numRated) { this.numRated = numRated; }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public ArrayList<MakoEventFact> getFacts() { return facts; }

    public ArrayList<HashMap<String, String>> getComments() {
        return comments;
    }

    public void populateComments(JSONArray jsonArray) throws JSONException {
        if (jsonArray != null) {
            for (int i=0 ; i < jsonArray.length(); i++){

                Gson gson = new Gson();
                String jsonStr = jsonArray.get(i).toString();
                HashMap<String, String> comment = new HashMap<String, String>();

                comment = gson.fromJson(jsonStr, comment.getClass());
                comments.add(comment);
            }
        }
    };

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getNumComments(){
        if(comments!=null)
            return comments.size();
        return 0;
    }

    public void addFact(MakoEventFact fact) {
        facts.add(fact);
    }

    public int getNumFacts(){
        return facts.size();
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
