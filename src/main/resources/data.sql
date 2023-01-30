-- Authority 생성
INSERT INTO authority (AUTHORITY_NAME) values ('ROLE_USER');
INSERT INTO authority (AUTHORITY_NAME) values ('ROLE_ADMIN');

-- Interest 생성
INSERT INTO interest (ID, INTEREST_CODE) values (1, 0);
INSERT INTO interest (ID, INTEREST_CODE) values (2, 1);
INSERT INTO interest (ID, INTEREST_CODE) values (3, 2);
INSERT INTO interest (ID, INTEREST_CODE) values (4, 3);
INSERT INTO interest (ID, INTEREST_CODE) values (5, 4);
INSERT INTO interest (ID, INTEREST_CODE) values (6, 5);
INSERT INTO interest (ID, INTEREST_CODE) values (7, 6);
INSERT INTO interest (ID, INTEREST_CODE) values (8, 7);
INSERT INTO interest (ID, INTEREST_CODE) values (9, 8);
INSERT INTO interest (ID, INTEREST_CODE) values (10, 9);
INSERT INTO interest (ID, INTEREST_CODE) values (11, 10);
INSERT INTO interest (ID, INTEREST_CODE) values (12, 11);
INSERT INTO interest (ID, INTEREST_CODE) values (13, 12);
INSERT INTO interest (ID, INTEREST_CODE) values (14, 13);
INSERT INTO interest (ID, INTEREST_CODE) values (15, 14);
INSERT INTO interest (ID, INTEREST_CODE) values (16, 15);
INSERT INTO interest (ID, INTEREST_CODE) values (17, 16);
INSERT INTO interest (ID, INTEREST_CODE) values (18, 17);
INSERT INTO interest (ID, INTEREST_CODE) values (19, 18);
INSERT INTO interest (ID, INTEREST_CODE) values (20, 19);
INSERT INTO interest (ID, INTEREST_CODE) values (21, 20);
INSERT INTO interest (ID, INTEREST_CODE) values (22, 21);
INSERT INTO interest (ID, INTEREST_CODE) values (23, 22);
INSERT INTO interest (ID, INTEREST_CODE) values (24, 23);
INSERT INTO interest (ID, INTEREST_CODE) values (25, 24);
INSERT INTO interest (ID, INTEREST_CODE) values (26, 25);
INSERT INTO interest (ID, INTEREST_CODE) values (27, 26);
INSERT INTO interest (ID, INTEREST_CODE) values (28, 27);
INSERT INTO interest (ID, INTEREST_CODE) values (29, 28);
INSERT INTO interest (ID, INTEREST_CODE) values (30, 29);
INSERT INTO interest (ID, INTEREST_CODE) values (31, 30);
INSERT INTO interest (ID, INTEREST_CODE) values (32, 31);
INSERT INTO interest (ID, INTEREST_CODE) values (33, 32);
INSERT INTO interest (ID, INTEREST_CODE) values (34, 33);
INSERT INTO interest (ID, INTEREST_CODE) values (35, 34);
INSERT INTO interest (ID, INTEREST_CODE) values (36, 35);
INSERT INTO interest (ID, INTEREST_CODE) values (37, 36);
INSERT INTO interest (ID, INTEREST_CODE) values (38, 37);
INSERT INTO interest (ID, INTEREST_CODE) values (39, 38);
INSERT INTO interest (ID, INTEREST_CODE) values (40, 39);
INSERT INTO interest (ID, INTEREST_CODE) values (41, 40);
INSERT INTO interest (ID, INTEREST_CODE) values (42, 41);
INSERT INTO interest (ID, INTEREST_CODE) values (43, 42);
INSERT INTO interest (ID, INTEREST_CODE) values (44, 43);
INSERT INTO interest (ID, INTEREST_CODE) values (45, 44);
INSERT INTO interest (ID, INTEREST_CODE) values (46, 45);
INSERT INTO interest (ID, INTEREST_CODE) values (47, 46);
INSERT INTO interest (ID, INTEREST_CODE) values (48, 47);
INSERT INTO interest (ID, INTEREST_CODE) values (49, 48);
INSERT INTO interest (ID, INTEREST_CODE) values (50, 49);
INSERT INTO interest (ID, INTEREST_CODE) values (51, 50);
INSERT INTO interest (ID, INTEREST_CODE) values (52, 51);
INSERT INTO interest (ID, INTEREST_CODE) values (53, 52);
INSERT INTO interest (ID, INTEREST_CODE) values (54, 53);
INSERT INTO interest (ID, INTEREST_CODE) values (55, 54);
INSERT INTO interest (ID, INTEREST_CODE) values (56, 55);
INSERT INTO interest (ID, INTEREST_CODE) values (57, 56);
INSERT INTO interest (ID, INTEREST_CODE) values (58, 57);
INSERT INTO interest (ID, INTEREST_CODE) values (59, 58);
INSERT INTO interest (ID, INTEREST_CODE) values (60, 59);
INSERT INTO interest (ID, INTEREST_CODE) values (61, 60);
INSERT INTO interest (ID, INTEREST_CODE) values (62, 61);
INSERT INTO interest (ID, INTEREST_CODE) values (63, 62);
INSERT INTO interest (ID, INTEREST_CODE) values (64, 63);

-- PostCategory 생성
INSERT INTO post_category (ID, POST_CATEGORY_CODE) values (1, 0);
INSERT INTO post_category (ID, POST_CATEGORY_CODE) values (2, 1);
INSERT INTO post_category (ID, POST_CATEGORY_CODE) values (3, 2);
INSERT INTO post_category (ID, POST_CATEGORY_CODE) values (4, 3);
INSERT INTO post_category (ID, POST_CATEGORY_CODE) values (5, 4);
INSERT INTO post_category (ID, POST_CATEGORY_CODE) values (6, 5);
INSERT INTO post_category (ID, POST_CATEGORY_CODE) values (7, 6);
INSERT INTO post_category (ID, POST_CATEGORY_CODE) values (8, 7);
INSERT INTO post_category (ID, POST_CATEGORY_CODE) values (9, 8);
INSERT INTO post_category (ID, POST_CATEGORY_CODE) values (10, 9);
INSERT INTO post_category (ID, POST_CATEGORY_CODE) values (11, 10);
-- VoiceCategory 생성
INSERT INTO voice_category (ID, VOICE_CATEGORY_CODE) values (1, 0);
INSERT INTO voice_category (ID, VOICE_CATEGORY_CODE) values (2, 1);
INSERT INTO voice_category (ID, VOICE_CATEGORY_CODE) values (3, 2);
INSERT INTO voice_category (ID, VOICE_CATEGORY_CODE) values (4, 3);
INSERT INTO voice_category (ID, VOICE_CATEGORY_CODE) values (5, 4);
INSERT INTO voice_category (ID, VOICE_CATEGORY_CODE) values (6, 5);
INSERT INTO voice_category (ID, VOICE_CATEGORY_CODE) values (7, 6);

-- CREATEAT(필요하면): 2022-05-03 20:40:15.943059

-- college 생성
INSERT INTO COLLEGE (COLLEGE_NAME, CAMPUS_NAME, ADDRESS, COLLEGE_SEQ) values ('빈값', '빈값', '빈값', -1);
INSERT INTO COLLEGE (COLLEGE_NAME, CAMPUS_NAME, ADDRESS, COLLEGE_SEQ) values ('한국대학교', '본교', '세종특별자치시 갈매로 408, 14동', 0);

--major 생성
INSERT INTO MAJOR (MAJOR_SEQ, NAME) values (-1, '빈값');
INSERT INTO MAJOR (MAJOR_SEQ, NAME) values (174, '물리교육과');