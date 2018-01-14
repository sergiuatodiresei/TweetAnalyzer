package tweetdata;

public class TweetEntity {

    private String name;
    private String type;
    private double relevance;

    public TweetEntity(String name, String type, double relevance) {
        this.name = name;
        this.type = type;
        this.relevance = relevance;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public double getRelevance() {
        return relevance;
    }
}
