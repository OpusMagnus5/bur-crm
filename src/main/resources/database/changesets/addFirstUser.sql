INSERT INTO public.users (usr_uuid,
                          usr_password,
                          usr_email,
                          usr_first_name,
                          usr_last_name,
                          usr_roles,
                          usr_created_by)
VALUES (gen_random_uuid(),
        '$2a$10$hFP0QUed/sCyAcBG0xQ2kO5z95l1xVQPQsB.Qq3ADO.NcZFtihygW',
        'szaddaj@gmail.com',
        'Administrator',
        'Systemu',
        'USER;MANAGER;ADMIN',
        1);