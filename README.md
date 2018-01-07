# TweetAnalyzer

# http://tweetanalyzer-env.us-east-2.elasticbeanstalk.com/

Exemple de teste:

Apple Inc Chief Executive Officer Tim Cook on Monday is expected to announce details of the first product developed under his leadership, a watch that Apple hopes will transform the market of wearable technology #i #love #apple

Hillary Clinton likes to remind Texans that she first came here to ask for their votes in 1972 as a young Democratic campaign worker. Doug Hattaway, a Clinton adviser who worked on Al Gore's presidential campaign in 2000 was in Austin Texas. Texas is crucial to Clinton's hopes of staying in the U.S. presidential race. Clinton travleed to France, Spain, and San Francisco.

I hate titanic 😒😂 #i #love #titanic



## Raspunsul este un json si poate contine:

### hashtag_sentiment 

Poate avea urmatoarele valori: 
- **-1**, daca textul nu contine hashtag-uri
- **0**, sentiment negativ
- **2**, sentiment neutru
- **4**, sentiment pozitiv

### emoji_sentiment

Poate avea urmatoarele valori: 
- **-1**, daca textul nu contine emoji
- **0**, sentiment negativ
- **2**, sentiment neutru
- **4**, sentiment pozitiv

### text_sentiment

Poate avea urmatoarele valori: 
- **0**, sentiment negativ
- **2**, sentiment neutru
- **4**, sentiment pozitiv

### ner (Named Entity Recognition)

Contine:

- **text**, textul primit de la utilizator

- **topics** (ideile principale extrase din text),  un array ce contine json-uri cu cheile *name* si *confidence*(cu valori intre 0 si 1, 1 reprezinta incredere maxima);

- **social_tags**, un array ce contine json-uri cu cheile *name* si *importance*(cu urmatoarele valori: 1 (importanta maxima), 2 (importanta medie), 3 (importanta scazuta));

- **industry_tags**, un array ce contine json-uri cu cheile *name* si *relevance*(cu valori intre 0 si 1, 1 reprezinta relevanta maxima);

- **entities**, un array ce contine json-uri cu cheile *name*, *type* (care poate avea urmatoarele valori: Anniversary, City, Company, Continent, Country, Editor, EmailAddress, EntertainmentAwardEvent, Facility, FaxNumber,
Holiday, IndustryTerm, Journalist, MarketIndex, MedicalCondition, MedicalTreatment, Movie, MusicAlbum, MusicGroup,
NaturalFeature, OperatingSystem, Organization, Person, PharmaceuticalDrug, PhoneNumber, PoliticalEvent, Position,
Product, ProgrammingLanguage, ProvinceOrState, PublishedMedium, RadioProgram, RadioStation, Region,
SportsEvent, SportsGame, SportsLeague, Technology, TVShow, TVStation, URL), *relevance* (cu valori intre 0 si 1, 1 reprezinta relevanta maxima), *confidence* (doar daca type este person, cu valori intre 0 si 1, 1 reprezinta incredere maxima).



