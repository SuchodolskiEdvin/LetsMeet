INSERT INTO application_user (id, creation_date, modification_date, version, deleted, email, first_name, last_name, password, reset_password_token)
VALUES (1, NOW(), NOW(), 0, false, 'test1', 'test1', 'test1', '$2a$10$UmMpOklhr2TTbxSudKtTc.pIrXpgnglWShq/1PQBero5TvocTnRXW', null),
       (2, NOW(), NOW(), 0, false, 'test2', 'test2', 'test2', '$2a$10$UmMpOklhr2TTbxSudKtTc.pIrXpgnglWShq/1PQBero5TvocTnRXW', null),
       (3, NOW(), NOW(), 0, false, 'test3', 'test3', 'test3', '$2a$10$UmMpOklhr2TTbxSudKtTc.pIrXpgnglWShq/1PQBero5TvocTnRXW', null),
       (4, NOW(), NOW(), 0, false, 'test4', 'test4', 'test4', '$2a$10$UmMpOklhr2TTbxSudKtTc.pIrXpgnglWShq/1PQBero5TvocTnRXW', null);

INSERT INTO meet (id, creation_date, modification_date, version, name, creator_id)
VALUES (1, NOW(), NOW(), 0, 'Test1_Test2', 1),
       (2, NOW(), NOW(), 0, 'Test2_Test3', 2),
       (3, NOW(), NOW(), 0, 'Test4_Test1_Test2_Test3', 4);

INSERT INTO meet_participants (meets_id, participants_id)
VALUES (1, 1), (1, 2), (2, 2), (2, 3), (3, 1), (3, 2), (3, 3), (3, 4);