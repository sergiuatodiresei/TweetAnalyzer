# TweetAnalyzer

## http://tweetanalyzer-env.us-east-2.elasticbeanstalk.com/

Examples of tests:

Apple Inc Chief Executive Officer Tim Cook on Monday is expected to announce details of the first product developed under his leadership, a watch that Apple hopes will transform the market of wearable technology #i #love #apple

Hillary Clinton likes to remind Texans that she first came here to ask for their votes in 1972 as a young Democratic campaign worker. Doug Hattaway, a Clinton adviser who worked on Al Gore's presidential campaign in 2000 was in Austin Texas. Texas is crucial to Clinton's hopes of staying in the U.S. presidential race. Clinton travleed to France, Spain, and San Francisco.

I hate titanic 😒😂 #i #love #titanic



## The response is a json and may contain:

### hashtag_sentiment 

The polarity values are:
- **-1**, if the text does not contain hashtags
- **0**, negative
- **2**, neutral
- **4**, positive

### emoji_sentiment

The polarity values are: 
- **-1**, daca textul nu contine emoji
- **0**, negative
- **2**, neutral
- **4**, positive

### text_sentiment

The polarity values are: 
- **0**, negative
- **2**, neutral
- **4**, positive

### ner (Named Entity Recognition)

Contains:

- **text**, the input text from user

- **topics** (the main ideas extracted from the text), a json array with the keys *name* and *confidence*(values between 0 an 1, 1 represents max confidence);

- **social_tags**, a json array with the keys *name* and *importance*. The importance key indicates how centric the topic named by the social tag is to the text as awhole. The importance value can be 1 (very centric), 2 (somewhat centric), or 3 (less centric);

- **industry_tags**, a json array with the keys *name* and *relevance* (values between 0 an 1, 1 represents max relevance);

- **entities**, a json array with the keys *name*, *type* (may have the following values: Anniversary, City, Company, Continent, Country, Editor, EmailAddress, EntertainmentAwardEvent, Facility, FaxNumber,
Holiday, IndustryTerm, Journalist, MarketIndex, MedicalCondition, MedicalTreatment, Movie, MusicAlbum, MusicGroup,
NaturalFeature, OperatingSystem, Organization, Person, PharmaceuticalDrug, PhoneNumber, PoliticalEvent, Position,
Product, ProgrammingLanguage, ProvinceOrState, PublishedMedium, RadioProgram, RadioStation, Region,
SportsEvent, SportsGame, SportsLeague, Technology, TVShow, TVStation, URL), *relevance* (values between 0 an 1, 1 represents max relevance), *confidence* (if the type is "person", cu valori intre 0 si 1, 1 values between 0 an 1, 1 represents max confidence).


# Huge update! v2.0

### If the stats parameter is set ("stats":"1"), the json will also contain tweet statistics, like:


- **stats**, if the tweet is fake or not
- **stats_code**
- **similar_tweets**, if similar tweets have been found. Contains *nr_analyzed_tweets*, *nr_verified_users* (number of similar tweets posted by verified users), *tweets*. *Tweets* contains the tweet text, username, user description, *similarity_entities* score (1 - max similarity),  *similarity_text* score (1 - max similarity). 

We start with a score of 0 (**stats_code**).

If the search text does not have entities, the **stats** is: *We can not tell with precision whether tweet is fake or not.* The score will be -500.

The program queries for similar news (tweets) posted by verified users (text similarity over 0.25).

If 0 tweets are found, the the **stats** is: *We can not tell exactly if tweet is fake or not.* The score (**stats_code**) will be -500.

If 1 tweet is found, then continue with the execution, and at the end the **stats** will specify: *Only one source found.*

For each similar tweet found , 10 points are added (score + = 10).
 
If the similarity score between entities is 1, then 40 points are added (score + = 40). If the similarity score between entities is >= 0.5 and <1, then 10 points are added (score + = 10).  If the similarity score between entities is > = 0.1 and <0.5, then 5 points are added (score + = 5). Otherwise, we do not change the score.

sentiment_text_original, sentiment_text - the original must not differ by more than 2 points than the one found, otherwise score -= 10 and break;

hashtag_sentiment_text_original, hashtag_sentiment_text - if the original is -1, break; otherwise it should not differ by more than 2 points than the one found, otherwise score -= 2.5

emoji_sentiment_text_original, emoji_sentiment_text - if the original is -1, break; otherwise it should not differ by more than 2 points than the one found, otherwise score -= 2.5


If during parsing of the tweets, the score exceeds 150 points, break and the score becomes 100. Also if the score is higher than 100 at the end.

If the score >= 50, then the **stats** will be: *Tweet is not fake. Confidence: score.* **stats_code** = score.

If the score is <50 then the **stats** will be: *Tweet is fake. Confidence: 100-score.* **stats_code** = 100 - score.

If something went wrong, the **stats_code** is -1000.


## Example of test:

Dua Lipa leads the Brits nominations.




