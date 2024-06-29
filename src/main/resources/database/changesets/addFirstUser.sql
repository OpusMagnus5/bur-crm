INSERT INTO public.users (usr_id, usr_uuid, usr_password, usr_email, usr_first_name, usr_last_name, usr_roles, usr_created_by)
VALUES (-1, gen_random_uuid(), '$2a$12$8du4X5grziCGRvlFAlFs9eUDN5WRjF1p2Nx/WXKeZB2ix60ikXKxK', '1@burdok.com','Administrator',
        'Systemu', 'USER;MANAGER;ADMIN', -1);

INSERT INTO public.users (usr_id, usr_uuid, usr_password, usr_email, usr_first_name, usr_last_name, usr_roles, usr_created_by)
VALUES (-2, gen_random_uuid(), '$2a$18$wocudDgEmG.sZdLL2hMXxu9YBIRrZk3WIiyqCIeptXdjXfsiqNu8m', '2@burdok.com','Synchronizator',
        'Statusu', 'SYSTEM_USER', -1);