package dbObjects;

public class MakoEventFact  {

    public final static String FACT_ID_COL          = "objectId";
    public final static String MAKO_EVENT_ID_COL    = "MakoEventID";
    public final static String CONTENT_COL          = "content";
    public final static String CONTENT_TYPE_COL     = "contentType";
    public final static String URL_COL              = "URL";

    public String factID;
    public String makoEventID;
    public String content;
    public String contentType;
    public String URL;

    public MakoEventFact() {
        this.content = "";
        this.contentType = "";
        this.URL = "";
    }

    public MakoEventFact(String factID, String makoEventID, String content, String contentType, String URL) {
        this.factID = factID;
        this.makoEventID = makoEventID;
        this.content = content;
        this.contentType = contentType;
        this.URL = URL;
    }

    public static String getFactIdCol() { return FACT_ID_COL; }

    public static String getMakoEventIdCol() { return MAKO_EVENT_ID_COL; }

    public static String getContentCol() { return CONTENT_COL; }

    public static String getContentTypeCol() { return CONTENT_TYPE_COL; }

    public static String getUrlCol() { return URL_COL; }

    public String getFactID() { return factID; }

    public void setFactID(String factID) { this.factID = factID; }

    public String getMakoEventID() { return makoEventID; }

    public void setMakoEventID(String makoEventID) { this.makoEventID = makoEventID; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public String getContentType() { return contentType; }

    public void setContentType(String contentType) { this.contentType = contentType; }

    public String getURL() { return URL; }

    public void setURL(String URL) {
        if (URL == null)
            return;
        if (URL.equals(""))
            return;
        this.URL = URL;
    }
}
