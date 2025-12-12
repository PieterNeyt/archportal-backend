create schema if not exists gameservice;
create schema if not exists analyticsservice;
create schema if not exists lobbyservice;
create schema if not exists shopservice;
create schema if not exists communicationservice;
create schema if not exists profileservice;

-- ===== Owners =====
INSERT INTO gameservice.owner (id, game_studio_id)
VALUES ('51d0b9d3-80aa-4d18-9c3b-84b2f8b8d671', '3f071d5d-5d2e-4b5f-9c12-7cf7e902b113'),
       ('56c1596a-ec26-4c5d-aa01-31f7a34b76ad', 'd1ed8db6-2c4a-4b72-9481-7dfcb2a3a912');

-- ===== Studios =====
INSERT INTO gameservice.gamestudio (id, owner_id, name, description, iban)
VALUES ('3f071d5d-5d2e-4b5f-9c12-7cf7e902b113', '51d0b9d3-80aa-4d18-9c3b-84b2f8b8d671', 'HexGrid Games',
        'A board-game studio focused on abstract strategy and elegant, minimalistic designs.', 'NL91ABNA0417164300'),
       ('d1ed8db6-2c4a-4b72-9481-7dfcb2a3a912', '56c1596a-ec26-4c5d-aa01-31f7a34b76ad', 'Timberwolf Studios',
        'Specializes in thematic adventure and family-friendly board games with rich world-building.',
        'NL32RABO0287365401');

-- ===== Games =====
INSERT INTO gameservice.games (id, studio_id, title, description, price, image_url, game_url, genre, max_lobby_size)
VALUES ('f7b56a1e-1c63-4d0c-aa75-3b40e8253e19', '3f071d5d-5d2e-4b5f-9c12-7cf7e902b113', 'GridMaster',
        'An abstract strategy game where players battle for territory dominance on a shrinking grid.', 29.99,
        'https://framerusercontent.com/images/csQYwDfKsYvDh4jwg3mTQVEJ8.png?width=1920&height=1080',
        'https://hub.example.com/games/gridmaster', 'ABSTRACT', 2),
       ('7aefc340-96ed-4bb9-8262-34b50aa55420', '3f071d5d-5d2e-4b5f-9c12-7cf7e902b113', 'Triad Tactics',
        'A quick-play strategy game where players rotate tri-shaped tiles to outmaneuver opponents.', 19.99,
        'https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/3032540/capsule_616x353.jpg?t=1760843504',
        'https://hub.example.com/games/triad-tactics', 'STRATEGY', 6),
       ('9a5f6920-29e8-4d34-82ee-9df5249e8e72', 'd1ed8db6-2c4a-4b72-9481-7dfcb2a3a912', 'Forestbound',
        'A cooperative adventure where players defend an ancient forest from encroaching corruption.', 44.99,
        'https://us1.discourse-cdn.com/flex020/uploads/makecode/original/3X/a/6/a6d432439027cb0df20f1907aa9cfca496add0e4.png',
        'https://hub.example.com/games/forestbound', 'ADVENTURE', 12),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047c', 'd1ed8db6-2c4a-4b72-9481-7dfcb2a3a912', 'Tic-Tac-Toe Deluxe',
        'Speel met kruisjes en gaatjes', 44.99,
        'https://content.tinytap.it/2CF7E204-AE31-418B-A18D-3212C7EDBBC6/coverImage.png?ver=0',
        'http://localhost:5174/ttt/', 'STRATEGY', 2),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047d', 'd1ed8db6-2c4a-4b72-9481-7dfcb2a3a912', 'Tic-Tac-Toe Deluxe compose',
        'Speel met kruisjes en gaatjes', 44.99,
        'https://content.tinytap.it/2CF7E204-AE31-418B-A18D-3212C7EDBBC6/coverImage.png?ver=0', 'http://localhost/ttt/',
        'STRATEGY', 2),
       ('ab842ad3-7426-492f-bfda-4c61689ec2c4', 'd1ed8db6-2c4a-4b72-9481-7dfcb2a3a912', 'Campfire Stories',
        'A family-friendly party game where players build stories using illustrated prompt cards.', 24.99,
        'https://i.kickstarter.com/assets/026/491/434/3d8e598174acdbebfbf2e3348a5b0ff3_original.jpg?anim=false&fit=cover&gravity=auto&height=873&origin=ugc&q=92&v=1568619866&width=1552&sig=w7B94mmyjQcUBHpfytD9pVa5h8Cm9ilkmNnW9hvYoOI%3D',
        'https://hub.example.com/games/campfire-stories', 'FAMILY', 20)
        ,
       ('ab842ad3-7426-492f-bfda-4c61689ec2c5', 'd1ed8db6-2c4a-4b72-9481-7dfcb2a3a912', 'Checkers - compose',
        'Play checkers with the computer or with friends', 24.99,
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQwPrFsAjP-cH2rMWGL53Xs3fTwUvSEn4I9QA&s',
        'http://localhost/checkers/', 'STRATEGY', 2);

INSERT INTO profileservice.profile (id, first_name, last_name, gamer_tag, icon, platform_points, email)
VALUES ('ed7b6a30-a12f-4ae5-9d25-b1ea51c89840', 'Cian', 'Van Acker', 'cian', null, 0, 'cian.vanacker@student.kdg.be'),
       ('5a129b2c-3016-4616-bca0-99ec7b067b32', 'Axel', 'Peeters', 'axel', null, 0, 'axel.peeters.1@student.kdg.be'),
       ('58e1a434-0797-4d3d-9140-e846b20b7887', 'Pieter', 'Neyt', 'pieter', null, 0, 'pieter.neyt@student.kdg.be'),
       ('56c1596a-ec26-4c5d-aa01-31f7a34b76ad', 'Hugo', 'Dor', 'hugo', null, 0, 'hugo.dor@student.kdg.be');

INSERT INTO profileservice.profile_library (game_id, profile_id)
VALUES ('1b2d89fa-bd59-4873-b568-8df26c3a047d', 'ed7b6a30-a12f-4ae5-9d25-b1ea51c89840'),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047c', 'ed7b6a30-a12f-4ae5-9d25-b1ea51c89840'),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047d', '5a129b2c-3016-4616-bca0-99ec7b067b32'),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047c', '5a129b2c-3016-4616-bca0-99ec7b067b32'),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047d', '58e1a434-0797-4d3d-9140-e846b20b7887'),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047c', '58e1a434-0797-4d3d-9140-e846b20b7887'),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047d', '56c1596a-ec26-4c5d-aa01-31f7a34b76ad'),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047c', '56c1596a-ec26-4c5d-aa01-31f7a34b76ad');

INSERT INTO analyticsservice.game_statistics (last_played_at, total_playtime_minutes, game_id, profile_id)
VALUES (NOW(), 120, '1b2d89fa-bd59-4873-b568-8df26c3a047c', 'ed7b6a30-a12f-4ae5-9d25-b1ea51c89840'),
       (NOW(), 95, '1b2d89fa-bd59-4873-b568-8df26c3a047c', '5a129b2c-3016-4616-bca0-99ec7b067b32'),
       (NOW(), 150, '1b2d89fa-bd59-4873-b568-8df26c3a047c', '58e1a434-0797-4d3d-9140-e846b20b7887'),
       (NOW(), 80, '1b2d89fa-bd59-4873-b568-8df26c3a047c', '56c1596a-ec26-4c5d-aa01-31f7a34b76ad'),
       (NOW(), 120, '1b2d89fa-bd59-4873-b568-8df26c3a047d', 'ed7b6a30-a12f-4ae5-9d25-b1ea51c89840'),
       (NOW(), 95, '1b2d89fa-bd59-4873-b568-8df26c3a047d', '5a129b2c-3016-4616-bca0-99ec7b067b32'),
       (NOW(), 150, '1b2d89fa-bd59-4873-b568-8df26c3a047d', '58e1a434-0797-4d3d-9140-e846b20b7887'),
       (NOW(), 80, '1b2d89fa-bd59-4873-b568-8df26c3a047d', '56c1596a-ec26-4c5d-aa01-31f7a34b76ad');

INSERT INTO profileservice.friendship(id, profile_a_id, profile_b_id)
VALUES ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'ed7b6a30-a12f-4ae5-9d25-b1ea51c89840',
        '5a129b2c-3016-4616-bca0-99ec7b067b32'),
       ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab', 'ed7b6a30-a12f-4ae5-9d25-b1ea51c89840',
        '58e1a434-0797-4d3d-9140-e846b20b7887');

INSERT INTO communicationservice.chat_room(id, title)
VALUES ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Axel'),
       ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab', 'Title');

INSERT INTO communicationservice.chat_room_member(chat_room_id, members)
VALUES ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'ed7b6a30-a12f-4ae5-9d25-b1ea51c89840'),
       ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '5a129b2c-3016-4616-bca0-99ec7b067b32'),
       ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab', 'ed7b6a30-a12f-4ae5-9d25-b1ea51c89840'),
       ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab', '58e1a434-0797-4d3d-9140-e846b20b7887');

INSERT INTO communicationservice.message(id, chat_room_id, sender, text, timestamp)
VALUES ( 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'
       , 'ed7b6a30-a12f-4ae5-9d25-b1ea51c89840', 'Hey Axel, hoe gaat het met je vandaag?'
       , now() - interval '30 minutes'),
       ( 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'
       , '5a129b2c-3016-4616-bca0-99ec7b067b32', 'Hi Cian! Goed, en met jou? Nog plannen voor vanavond?'
       , now() - interval '28 minutes'),
       ( 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaac', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'
       , 'ed7b6a30-a12f-4ae5-9d25-b1ea51c89840', 'Met mij ook prima. Misschien even gamen later?'
       , now() - interval '25 minutes'),
       ( 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaad', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'
       , '5a129b2c-3016-4616-bca0-99ec7b067b32', 'Klinkt goed! Welke game dacht je te spelen?'
       , now() - interval '22 minutes'),
       ( 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaae', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'
       , 'ed7b6a30-a12f-4ae5-9d25-b1ea51c89840', 'Misschien wat Fortnite of Apex, hangt van jou af.'
       , now() - interval '20 minutes'),
       ( 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaf', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'
       , '5a129b2c-3016-4616-bca0-99ec7b067b32', 'Laten we Fortnite doen, lang niet gespeeld!'
       , now() - interval '18 minutes'),
       ( 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaba', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'
       , 'ed7b6a30-a12f-4ae5-9d25-b1ea51c89840', 'Top, dan kunnen we meteen wat squads doen.'
       , now() - interval '15 minutes'),
       ( 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaca', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'
       , '5a129b2c-3016-4616-bca0-99ec7b067b32', 'Perfect, ik zet de stream alvast klaar.'
       , now() - interval '12 minutes'),
       ( 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaada', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'
       , 'ed7b6a30-a12f-4ae5-9d25-b1ea51c89840', 'Nice! Heb je nog iets lekkers om te snacken?'
       , now() - interval '10 minutes'),
       ( 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaea', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'
       , '5a129b2c-3016-4616-bca0-99ec7b067b32', 'Chips en cola, klassiekers 😄', now() - interval '9 minutes'),
       ( 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaafa', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'
       , 'ed7b6a30-a12f-4ae5-9d25-b1ea51c89840', 'Perfect combo! Ready om te starten?', now() - interval '8 minutes'),
       ( 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaadaa', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'
       , '5a129b2c-3016-4616-bca0-99ec7b067b32', 'Yep, ik join nu.', now() - interval '7 minutes'),
       ( 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaadda', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'
       , 'ed7b6a30-a12f-4ae5-9d25-b1ea51c89840', 'Ok, laten we een paar potjes winnen!', now() - interval '6 minutes'),
       ( 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaeaaadea', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'
       , '5a129b2c-3016-4616-bca0-99ec7b067b32', 'Deal! Let’s go 💪', now() - interval '5 minutes'),
       ( 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaeaadda', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'
       , 'ed7b6a30-a12f-4ae5-9d25-b1ea51c89840', 'Haha ja, team work makes the dream work!'
       , now() - interval '4 minutes'),
       ( 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaadea', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'
       , '5a129b2c-3016-4616-bca0-99ec7b067b32', '😂 Precies, we hebben dit!', now() - interval '3 days'),
       ( 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaeda', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'
       , 'ed7b6a30-a12f-4ae5-9d25-b1ea51c89840', 'Start jij de lobby? Ik moet nog even water halen.'
       , now() - interval '2 minutes'),
       ( 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaeea', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'
       , '5a129b2c-3016-4616-bca0-99ec7b067b32', 'Zal ik doen! Snel terug dan 😉', now() - interval '1 minutes'),
       ( 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaefa', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'
       , 'ed7b6a30-a12f-4ae5-9d25-b1ea51c89840', 'Ok, ik ben er bijna. Let’s go!', now() - interval '30 seconds'),
       ( 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaabaaa', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'
       , '5a129b2c-3016-4616-bca0-99ec7b067b32', 'Perfect timing, let’s crush it!', now());



INSERT INTO profileservice.friend_requests (sender_id, receiver_id)
VALUES ('56c1596a-ec26-4c5d-aa01-31f7a34b76ad', 'ed7b6a30-a12f-4ae5-9d25-b1ea51c89840');

INSERT INTO communicationservice.notification_settings (profile_id)
VALUES ('ed7b6a30-a12f-4ae5-9d25-b1ea51c89840'),
       ('5a129b2c-3016-4616-bca0-99ec7b067b32'),
       ('56c1596a-ec26-4c5d-aa01-31f7a34b76ad'),
       ('58e1a434-0797-4d3d-9140-e846b20b7887');

INSERT INTO communicationservice.notification_channel_types (profile_id, channel_type)
VALUES ('ed7b6a30-a12f-4ae5-9d25-b1ea51c89840', 'EMAIL'),
       ('ed7b6a30-a12f-4ae5-9d25-b1ea51c89840', 'IN_PLATFORM'),
       ('5a129b2c-3016-4616-bca0-99ec7b067b32', 'EMAIL'),
       ('5a129b2c-3016-4616-bca0-99ec7b067b32', 'IN_PLATFORM'),
       ('56c1596a-ec26-4c5d-aa01-31f7a34b76ad', 'EMAIL'),
       ('56c1596a-ec26-4c5d-aa01-31f7a34b76ad', 'IN_PLATFORM'),
       ('58e1a434-0797-4d3d-9140-e846b20b7887', 'EMAIL'),
       ('58e1a434-0797-4d3d-9140-e846b20b7887', 'IN_PLATFORM');

-- ============================
-- Notifications for CIÁN
-- ============================
INSERT INTO communicationservice.notifications (id, receiver_id, title, body, type, created_at)
VALUES ('11111111-1111-1111-1111-111111111111', 'ed7b6a30-a12f-4ae5-9d25-b1ea51c89840', 'Welkom!',
        'Welkom op het platform, veel speelplezier!', 'SYSTEM', NOW()),
       ('11111111-1111-1111-1111-111111111112', 'ed7b6a30-a12f-4ae5-9d25-b1ea51c89840', 'Nieuwe game beschikbaar',
        'Er is een nieuwe game toegevoegd aan de catalogus.', 'SYSTEM', NOW()),
       ('11111111-1111-1111-1111-111111111113', 'ed7b6a30-a12f-4ae5-9d25-b1ea51c89840', 'Vriend online',
        'Een van je vrienden is nu online.', 'FRIEND_REQUEST', NOW()),
       ('11111111-1111-1111-1111-111111111114', 'ed7b6a30-a12f-4ae5-9d25-b1ea51c89840', 'Achievement behaald',
        'Je hebt een nieuwe achievement verdiend!', 'ACHIEVEMENT', NOW()),
       ('11111111-1111-1111-1111-111111111115', 'ed7b6a30-a12f-4ae5-9d25-b1ea51c89840', 'Reminder',
        'Je hebt al even niet gespeeld, kom eens terug!', 'TURN_REMINDER', NOW()),
       ('11111111-1111-1111-1111-111111111116', 'ed7b6a30-a12f-4ae5-9d25-b1ea51c89840', 'Nieuwsupdate',
        'Er is een nieuwe platform-update beschikbaar.', 'SYSTEM', NOW());

-- ============================
-- Notifications for AXEL
-- ============================
INSERT INTO communicationservice.notifications (id, receiver_id, title, body, type, created_at)
VALUES ('22222222-2222-2222-2222-222222222221', '5a129b2c-3016-4616-bca0-99ec7b067b32', 'Welkom!',
        'Welkom op het platform, veel speelplezier!', 'SYSTEM', NOW()),
       ('22222222-2222-2222-2222-222222222222', '5a129b2c-3016-4616-bca0-99ec7b067b32', 'Nieuwe game beschikbaar',
        'Er is een nieuwe game toegevoegd aan de catalogus.', 'SYSTEM', NOW()),
       ('22222222-2222-2222-2222-222222222223', '5a129b2c-3016-4616-bca0-99ec7b067b32', 'Vriend online',
        'Een van je vrienden is nu online.', 'FRIEND_REQUEST', NOW()),
       ('22222222-2222-2222-2222-222222222224', '5a129b2c-3016-4616-bca0-99ec7b067b32', 'Achievement behaald',
        'Je hebt een nieuwe achievement verdiend!', 'ACHIEVEMENT', NOW()),
       ('22222222-2222-2222-2222-222222222225', '5a129b2c-3016-4616-bca0-99ec7b067b32', 'Reminder',
        'Je hebt al even niet gespeeld, kom eens terug!', 'TURN_REMINDER', NOW()),
       ('22222222-2222-2222-2222-222222222226', '5a129b2c-3016-4616-bca0-99ec7b067b32', 'Nieuwsupdate',
        'Er is een nieuwe platform-update beschikbaar.', 'SYSTEM', NOW());

-- ============================
-- Notifications for PIETER
-- ============================
INSERT INTO communicationservice.notifications (id, receiver_id, title, body, type, created_at)
VALUES ('33333333-3333-3333-3333-333333333331', '58e1a434-0797-4d3d-9140-e846b20b7887', 'Welkom!',
        'Welkom op het platform, veel speelplezier!', 'SYSTEM', NOW()),
       ('33333333-3333-3333-3333-333333333332', '58e1a434-0797-4d3d-9140-e846b20b7887', 'Nieuwe game beschikbaar',
        'Er is een nieuwe game toegevoegd aan de catalogus.', 'SYSTEM', NOW()),
       ('33333333-3333-3333-3333-333333333333', '58e1a434-0797-4d3d-9140-e846b20b7887', 'Vriend online',
        'Een van je vrienden is nu online.', 'FRIEND_REQUEST', NOW()),
       ('33333333-3333-3333-3333-333333333334', '58e1a434-0797-4d3d-9140-e846b20b7887', 'Achievement behaald',
        'Je hebt een nieuwe achievement verdiend!', 'ACHIEVEMENT', NOW()),
       ('33333333-3333-3333-3333-333333333335', '58e1a434-0797-4d3d-9140-e846b20b7887', 'Reminder',
        'Je hebt al even niet gespeeld, kom eens terug!', 'TURN_REMINDER', NOW()),
       ('33333333-3333-3333-3333-333333333336', '58e1a434-0797-4d3d-9140-e846b20b7887', 'Nieuwsupdate',
        'Er is een nieuwe platform-update beschikbaar.', 'SYSTEM', NOW());

-- ============================
-- Notifications for HUGO
-- ============================
INSERT INTO communicationservice.notifications (id, receiver_id, title, body, type, created_at)
VALUES ('44444444-4444-4444-4444-444444444441', '56c1596a-ec26-4c5d-aa01-31f7a34b76ad', 'Welkom!',
        'Welkom op het platform, veel speelplezier!', 'SYSTEM', NOW()),
       ('44444444-4444-4444-4444-444444444442', '56c1596a-ec26-4c5d-aa01-31f7a34b76ad', 'Nieuwe game beschikbaar',
        'Er is een nieuwe game toegevoegd aan de catalogus.', 'SYSTEM', NOW()),
       ('44444444-4444-4444-4444-444444444443', '56c1596a-ec26-4c5d-aa01-31f7a34b76ad', 'Vriend online',
        'Een van je vrienden is nu online.', 'FRIEND_REQUEST', NOW()),
       ('44444444-4444-4444-4444-444444444444', '56c1596a-ec26-4c5d-aa01-31f7a34b76ad', 'Achievement behaald',
        'Je hebt een nieuwe achievement verdiend!', 'ACHIEVEMENT', NOW()),
       ('44444444-4444-4444-4444-444444444445', '56c1596a-ec26-4c5d-aa01-31f7a34b76ad', 'Reminder',
        'Je hebt al even niet gespeeld, kom eens terug!', 'TURN_REMINDER', NOW()),
       ('44444444-4444-4444-4444-444444444446', '56c1596a-ec26-4c5d-aa01-31f7a34b76ad', 'Nieuwsupdate',
        'Er is een nieuwe platform-update beschikbaar.', 'SYSTEM', NOW());
