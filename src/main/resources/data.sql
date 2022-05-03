

-- INSERT INTO USER (ID, LOGIN_ID, PASSWORD, NICKNAME, ACTIVATED, EMAIL, NAME, COLLEGE_CODE, DEPARTMENT, STUDENT_NUM, EMAIL_AUTHENTICATION, DELETED) VALUES (1, 'admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'admin', 1, '11111@naver.com', '어드민', '001', 'd001', 32210311, FALSE, FALSE);

-- 서버 실행과 동시에 실행되는 상황.
INSERT INTO user (ID, LOGIN_ID, PASSWORD, NICKNAME, ACTIVATED, EMAIL, name, COLLEGE_CODE, DEPARTMENT, STUDENT_NUM, EMAIL_AUTHENTICATION, DELETED) VALUES (1, 'admin', '$2a$10$JwoSm1YVIbuY8u1WIS.XMu/G4npvQv60jAzIlu4WcnXIPuEBJz5.C', 'admin', 1, '11111@naver.com', 'admin', '001', 'd001', 32210311, FALSE, FALSE);
INSERT INTO user (ID, LOGIN_ID, PASSWORD, NICKNAME, ACTIVATED, EMAIL, name, COLLEGE_CODE, DEPARTMENT, STUDENT_NUM, EMAIL_AUTHENTICATION, DELETED) VALUES (2, 'user1', '$2a$10$nicQMhS9JcPhrd3E/gc8gOHran0UsHvcIjfUUu9qGA.xD4rbaXbG2', 'nickuser1', 1, '22222@naver.com', 'seilpark', '001', 'd001', 32210312, FALSE, FALSE);
INSERT INTO user (ID, LOGIN_ID, PASSWORD, NICKNAME, ACTIVATED, EMAIL, name, COLLEGE_CODE, DEPARTMENT, STUDENT_NUM, EMAIL_AUTHENTICATION, DELETED) VALUES (3, 'user2', '$2a$10$nicQMhS9JcPhrd3E/gc8gOHran0UsHvcIjfUUu9qGA.xD4rbaXbG2', 'nickuser2', 1, '33333@naver.com', 'syun', '001', 'd001', 32210313, FALSE, FALSE);

INSERT INTO authority (AUTHORITY_NAME) values ('ROLE_USER');
INSERT INTO authority (AUTHORITY_NAME) values ('ROLE_ADMIN');

INSERT INTO user_authority (USER_ID, AUTHORITY_NAME) values (1, 'ROLE_USER');
INSERT INTO user_authority (USER_ID, AUTHORITY_NAME) values (1, 'ROLE_ADMIN');
INSERT INTO user_authority (USER_ID, AUTHORITY_NAME) values (2, 'ROLE_USER');
INSERT INTO user_authority (USER_ID, AUTHORITY_NAME) values (3, 'ROLE_USER');

-- INSERT INTO friend (ID, STATUS, RECIPIENT_ID, REQUESTRO_ID) values (1, )


