--- pswd: 123456
--- init test profile
insert into profile(id, name, surname, email, password, created_date, profile_role, profile_status, visible)
values (gen_random_uuid(), 'Toshmat', 'Toshmatov', 'toshmat@gmail.com', '$2a$10$sdffcoBgzINSxielojVmhej0LTe6dX5oVFRoPAKOaM6Co2DwZt/6y', now(), 'ROLE_USER', 'ACTIVE', true),
       (gen_random_uuid(), 'Eshmat', 'Eshmatov', 'eshmat@gmail.com', '$2a$10$sdffcoBgzINSxielojVmhej0LTe6dX5oVFRoPAKOaM6Co2DwZt/6y', now(), 'ROLE_USER', 'ACTIVE', true),
       (gen_random_uuid(), 'Bigman', 'Bigmannov', 'bigman@gmail.com', '$2a$10$sdffcoBgzINSxielojVmhej0LTe6dX5oVFRoPAKOaM6Co2DwZt/6y', now(), 'ROLE_USER', 'ACTIVE', true),
       (gen_random_uuid(), 'Afandi', 'Afandiyev', 'afandi@gmail.com', '$2a$10$sdffcoBgzINSxielojVmhej0LTe6dX5oVFRoPAKOaM6Co2DwZt/6y', now(), 'ROLE_USER', 'ACTIVE', true);