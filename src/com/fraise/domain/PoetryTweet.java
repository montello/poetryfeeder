package com.fraise.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.logging.Logger;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.com.google.common.base.StringUtil;

@PersistenceCapable
public class PoetryTweet {

    private static final String POETRYFEEDER_MENTION_HEADER = "@poetryfeeder";
    private static final Logger log = Logger.getLogger(PoetryTweet.class.getName());

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private long sourceId;

    // @Persistent
    // private long localId;

    @Persistent
    private String text;

    @Persistent
    private String author;

    @Persistent
    private Date authoredOn;

    @Persistent
    private int textLength;

    @Persistent
    private String language;

    @Persistent
    private String firstLetter;

    public PoetryTweet(long sourceId, String text, String author, Date authoredOn) {
        if (text == null || author == null || authoredOn == null) {
            throw new IllegalArgumentException("All arguments are mandatory.");
        }
        this.sourceId = sourceId;
        this.text = formatPoetryText(text);
        this.author = author;
        this.authoredOn = authoredOn;
        this.firstLetter = String.valueOf(this.text.charAt(0)).toLowerCase();
        this.textLength = StringUtil.string2List(this.text, " ", true).size();
        this.language = detectLanguage(text);
    }

    private String detectLanguage(String text) {
        String language = "en";
        StringBuffer query = new StringBuffer();
        query.append(text);
        try {
            String queryString = query.toString();
            queryString = URLEncoder.encode(queryString, "UTF-8");
            URL url = new URL("http://ajax.googleapis.com/ajax/services/language/detect?v=1.0&q=" + queryString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String l = null;
            while ((l = br.readLine()) != null) {
                String languageMarker = "language\":\"";
                int pos = l.indexOf(languageMarker);
                if (pos > -1) {
                    language = l.substring(pos + languageMarker.length(), pos + languageMarker.length() + 2);
                }
            }
            br.close();

        } catch (MalformedURLException e) {
            log.info("Language detection failed:" + e.getMessage());
        } catch (IOException e) {
            log.info("Language detection failed:" + e.getMessage());
        }
        return language;
    }

    @SuppressWarnings("deprecation")
    private String formatPoetryText(String text) {
        String trimmed = StringUtil.toLowerCase(StringUtil.collapseWhitespace(text)).trim();
        trimmed = StringUtil.trimStart(trimmed.replaceAll(POETRYFEEDER_MENTION_HEADER, ""));
        return trimmed;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("[sourceId:").append(sourceId).append("]").append("[text:").append(text).append("]").append(
                "[author:").append(author).append("]").append("[authoredOn:").append(authoredOn.toString()).append("]")
                .append("[language:").append(language).append("]");
        return buf.toString();
    }

    public Key getKey() {
        return key;
    }

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }

    public Date getAuthoredOn() {
        return authoredOn;
    }

    public int getTextLength() {
        return textLength;
    }

    public String getLanguage() {
        return language;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public long getSourceId() {
        return sourceId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (sourceId ^ (sourceId >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PoetryTweet other = (PoetryTweet) obj;
        if (sourceId != other.sourceId)
            return false;
        return true;
    }

}
