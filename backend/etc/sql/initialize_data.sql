INSERT INTO application_user (id, creation_date, modification_date, version, deleted, email, first_name, last_name, password, reset_password_token)
VALUES (1, NOW(), NOW(), 0, false, 'test', 'test', 'test', '$2a$10$UmMpOklhr2TTbxSudKtTc.pIrXpgnglWShq/1PQBero5TvocTnRXW', null);

INSERT INTO meet (id, creation_date, modification_date, version, name, creator_id)
VALUES (1, NOW(), NOW(), 0, 'Spotkanko', 1);
