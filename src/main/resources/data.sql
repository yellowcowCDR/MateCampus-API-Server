

INSERT INTO USER (ID, LOGIN_ID, PASSWORD, NICKNAME, ACTIVATED, EMAIL, NAME, COLLEGE_CODE, DEPARTMENT, STUDENT_NUM, EMAIL_AUTHENTICATION, DELETED) VALUES (1, 'admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'admin', 1, '11111@naver.com', '어드민', '001', 'd001', 32210311, FALSE, FALSE);
INSERT INTO USER (ID, LOGIN_ID, PASSWORD, NICKNAME, ACTIVATED, EMAIL, NAME, COLLEGE_CODE, DEPARTMENT, STUDENT_NUM, EMAIL_AUTHENTICATION, DELETED) VALUES (2, 'user', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'user', 1, '22222@naver.com', '박세일', '001', 'd001', 32210312, FALSE, FALSE);

INSERT INTO AUTHORITY (AUTHORITY_NAME) values ('ROLE_USER');
INSERT INTO AUTHORITY (AUTHORITY_NAME) values ('ROLE_ADMIN');

INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_NAME) values (1, 'ROLE_USER');
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_NAME) values (1, 'ROLE_ADMIN');
INSERT INTO USER_AUTHORITY (USER_ID, AUTHORITY_NAME) values (2, 'ROLE_USER');
