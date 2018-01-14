package tweetdata;

public class TweetTopics {

    private int confidence;
    private String name;

    public TweetTopics(int confidence, String name) {
        this.confidence = confidence;
        this.name = name;
    }

    public int getConfidence() {
        return confidence;
    }

    public String getName() {
        return name;
    }
}
