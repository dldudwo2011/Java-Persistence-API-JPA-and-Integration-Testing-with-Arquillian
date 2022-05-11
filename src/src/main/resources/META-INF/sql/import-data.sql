create table "OscarReviews"
(
    id                     string
        constraint OSCARREVIEWS_PK
            primary key,
    category               string not null,
    nominee                string not null,
    review                 string not null,
    username               string not null,
    createdDateTime      date,
    lastModifiedDateTime date
)
/

create unique index OSCARREVIEWS_ID_UINDEX
    on "OscarReviews" (id)
/

INSERT INTO OscarReviews(id,category,nominee,review,username,createdDateTime,lastModifiedDateTime) VALUES('1','film','Bong Joon-ho','Parasite!!','Robert Downey Jr',TO_DATE('2021-02-25 08:00:08', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2021-02-25 08:15:08', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO OscarReviews(id,category,nominee,review,username,createdDateTime,lastModifiedDateTime) VALUES('2','actor','Lee Jung-jae','Squid game!!','Youngjae Lee',TO_DATE('2021-02-25 08:00:08', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2021-02-25 08:15:08', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO OscarReviews(id,category,nominee,review,username,createdDateTime,lastModifiedDateTime) VALUES('3','actress','HoYeon Jung','Squid game!!','Youngjae Lee', TO_DATE('2021-02-25 08:00:08', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2021-02-25 08:15:08', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO OscarReviews(id,category,nominee,review,username,createdDateTime,lastModifiedDateTime) VALUES('4','editing','YANG Jin-mo','Parasite!!','Scarlett Johansson',TO_DATE('2021-02-25 08:00:08', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2021-02-25 08:15:08', 'YYYY-MM-DD HH24:MI:SS'));
