--USERS
insert into users (id, email,first_name,last_name, password)
values ('ba804cb9-fa14-42a5-afaf-be488742fc54', 'admin@example.com', 'James','Bond', '$2a$10$ixzD9TQMeOSF6Ja.XjNqTO4SRW7E1S7nriSc9P/RrdbSPEqEqkw3C'),
       ('0d8fa44c-54fd-4cd0-ace9-2a7da57992de', 'user@example.com', 'Tyler','Durden', '$2a$10$ixzD9TQMeOSF6Ja.XjNqTO4SRW7E1S7nriSc9P/RrdbSPEqEqkw3C')
    ON CONFLICT DO NOTHING;


--ROLES
INSERT INTO role(id, name)
VALUES ('d29e709c-0ff1-4f4c-a7ef-09f656c390f1', 'DEFAULT'),
       ('ab505c92-7280-49fd-a7de-258e618df074', 'USER_MODIFY'),
       ('c6aee32d-8c35-4481-8b3e-a876a39b0c02', 'USER_DELETE'),
       ('4aa14e87-2155-43fa-8d93-e51294cf5bbb', 'ADMIN'),
       ('aed650d9-cae0-4b48-877c-932a7f347786', 'BLOG_READ_WRITE')


    ON CONFLICT DO NOTHING;

--AUTHORITIES
INSERT INTO authority(id, name)
VALUES ('2ebf301e-6c61-4076-98e3-2a38b31daf86', 'DEFAULT'),
       ('76d2cbf6-5845-470e-ad5f-2edb9e09a868', 'USER_MODIFY'),
       ('21c942db-a275-43f8-bdd6-d048c21bf5ab', 'USER_DELETE'),
       ('de1cadd0-09ee-40c7-a4ed-182ec03d4b33', 'BLOG_READ_WRITE')

    ON CONFLICT DO NOTHING;

--assign roles to users
insert into users_role (users_id, role_id)
values ('ba804cb9-fa14-42a5-afaf-be488742fc54', 'd29e709c-0ff1-4f4c-a7ef-09f656c390f1'),
       ('ba804cb9-fa14-42a5-afaf-be488742fc54', '4aa14e87-2155-43fa-8d93-e51294cf5bbb'), -- admin
       ('0d8fa44c-54fd-4cd0-ace9-2a7da57992de', 'd29e709c-0ff1-4f4c-a7ef-09f656c390f1'),
       ('ba804cb9-fa14-42a5-afaf-be488742fc54', 'ab505c92-7280-49fd-a7de-258e618df074'),
       ('ba804cb9-fa14-42a5-afaf-be488742fc54', 'c6aee32d-8c35-4481-8b3e-a876a39b0c02')
    ON CONFLICT DO NOTHING;

--assign authorities to roles
INSERT INTO role_authority(role_id, authority_id)
VALUES ('d29e709c-0ff1-4f4c-a7ef-09f656c390f1', '2ebf301e-6c61-4076-98e3-2a38b31daf86'),
       ('ab505c92-7280-49fd-a7de-258e618df074', '76d2cbf6-5845-470e-ad5f-2edb9e09a868'),
       ('c6aee32d-8c35-4481-8b3e-a876a39b0c02', '21c942db-a275-43f8-bdd6-d048c21bf5ab'),
       ('aed650d9-cae0-4b48-877c-932a7f347786', 'de1cadd0-09ee-40c7-a4ed-182ec03d4b33')

    ON CONFLICT DO NOTHING;

--blogs
INSERT INTO blog(id, category, text, title, author_id)
VALUES ('7a39effb-bcb8-45b2-8154-b67172143b93', 'tech', 'ChatGpt4 will be out soon', 'ChatGpt 4', 'ba804cb9-fa14-42a5-afaf-be488742fc54')

    ON CONFLICT DO NOTHING;