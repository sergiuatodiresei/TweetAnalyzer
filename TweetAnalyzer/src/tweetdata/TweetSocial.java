package tweetdata;

public class TweetSocial {

    private String importance;
    private String name;

    public TweetSocial(String importance, String name) {
        this.importance = importance;
        this.name = name;
    }

    public String getImportance() {
        return importance;
    }

    public String getName() {
        return name;
    }
}
