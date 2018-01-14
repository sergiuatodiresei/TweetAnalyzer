package tweetdata;

public class TweetIndustry {

    private String name;
    private double relevance;

    public TweetIndustry(String name, double relevance) {
        this.name = name;
        this.relevance = relevance;
    }

    public String getName() {
        return name;
    }

    public double getRelevance() {
        return relevance;
    }
}
