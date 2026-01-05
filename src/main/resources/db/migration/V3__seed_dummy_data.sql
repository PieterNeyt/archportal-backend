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
        'A quick-play strategy game where players rotate tri-shaped tiles to outmaneuver opponents.', 1999.99,
        'https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/3032540/capsule_616x353.jpg?t=1760843504',
        'https://hub.example.com/games/triad-tactics', 'STRATEGY', 6),
       ('9a5f6920-29e8-4d34-82ee-9df5249e8e72', 'd1ed8db6-2c4a-4b72-9481-7dfcb2a3a912', 'Forestbound',
        'A cooperative adventure where players defend an ancient forest from encroaching corruption.', 44.99,
        'https://us1.discourse-cdn.com/flex020/uploads/makecode/original/3X/a/6/a6d432439027cb0df20f1907aa9cfca496add0e4.png',
        'https://hub.example.com/games/forestbound', 'ADVENTURE', 12),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047c', 'd1ed8db6-2c4a-4b72-9481-7dfcb2a3a912', 'Tic-Tac-Toe Deluxe local',
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
       ('ab842ad3-7426-492f-bfda-4c61689ec2c5', 'd1ed8db6-2c4a-4b72-9481-7dfcb2a3a912', 'Checkers',
        'Play checkers with the computer or with friends', 24.99,
        'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQwPrFsAjP-cH2rMWGL53Xs3fTwUvSEn4I9QA&s',
        'http://localhost:5174/checkers/', 'STRATEGY', 2);

INSERT INTO profileservice.profile (id, first_name, last_name, gamer_tag, icon, platform_points, email)
VALUES ('ed7b6a30-a12f-4ae5-9d25-b1ea51c89840', 'Cian', 'Van Acker', 'cian', null, 100000, 'cian.vanacker@student.kdg.be'),
       ('5a129b2c-3016-4616-bca0-99ec7b067b32', 'Axel', 'Peeters', 'axel', null, 10000, 'axel.peeters.1@student.kdg.be'),
       ('58e1a434-0797-4d3d-9140-e846b20b7887', 'Pieter', 'Neyt', 'pieter', null, 100000, 'pieter.neyt@student.kdg.be'),
       ('56c1596a-ec26-4c5d-aa01-31f7a34b76ad', 'Hugo', 'Dor', 'hugo', null, 100000, 'hugo.dor@student.kdg.be');

INSERT INTO profileservice.profile_library (profile_id, id, favorite)
VALUES ('ed7b6a30-a12f-4ae5-9d25-b1ea51c89840', '1b2d89fa-bd59-4873-b568-8df26c3a047d', false),
       ('ed7b6a30-a12f-4ae5-9d25-b1ea51c89840', '1b2d89fa-bd59-4873-b568-8df26c3a047c', true),
       ('5a129b2c-3016-4616-bca0-99ec7b067b32', '1b2d89fa-bd59-4873-b568-8df26c3a047d', false),
       ('5a129b2c-3016-4616-bca0-99ec7b067b32', '1b2d89fa-bd59-4873-b568-8df26c3a047c', false),
       ('58e1a434-0797-4d3d-9140-e846b20b7887', '1b2d89fa-bd59-4873-b568-8df26c3a047d', true),
       ('58e1a434-0797-4d3d-9140-e846b20b7887', '1b2d89fa-bd59-4873-b568-8df26c3a047c', false),
       ('56c1596a-ec26-4c5d-aa01-31f7a34b76ad', '1b2d89fa-bd59-4873-b568-8df26c3a047d', false),
       ('56c1596a-ec26-4c5d-aa01-31f7a34b76ad', '1b2d89fa-bd59-4873-b568-8df26c3a047c', true);

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


-- ===== Analytics SERVICE DATA =====

-- ===== Player Statistics =====
-- Player statistics voor elk profiel
INSERT INTO analyticsservice.player_statistics (player_id, total_time_played, last_played)
VALUES ('ed7b6a30-a12f-4ae5-9d25-b1ea51c89840', 450000, NOW() - INTERVAL '2 hours'),
       ('5a129b2c-3016-4616-bca0-99ec7b067b32', 680000, NOW() - INTERVAL '1 day'),
       ('58e1a434-0797-4d3d-9140-e846b20b7887', 320232, NOW() - INTERVAL '3 days'),
       ('56c1596a-ec26-4c5d-aa01-31f7a34b76ad', 890596, NOW() - INTERVAL '5 hours');

-- ===== Game Statistics =====
-- Game statistics voor Cian (ed7b6a30-a12f-4ae5-9d25-b1ea51c89840)
-- Games in library: Tic-Tac-Toe Deluxe compose (favorite), Tic-Tac-Toe Deluxe
INSERT INTO analyticsservice.game_statistics (game_id, player_statistics_id, total_time_played, last_played_at)
VALUES ('1b2d89fa-bd59-4873-b568-8df26c3a047d', 'ed7b6a30-a12f-4ae5-9d25-b1ea51c89840', 180, NOW() - INTERVAL '2 hours'),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047c', 'ed7b6a30-a12f-4ae5-9d25-b1ea51c89840', 270, NOW() - INTERVAL '1 day');

-- Game statistics voor Axel (5a129b2c-3016-4616-bca0-99ec7b067b32)
-- Games in library: Tic-Tac-Toe Deluxe compose, Tic-Tac-Toe Deluxe
INSERT INTO analyticsservice.game_statistics (game_id, player_statistics_id, total_time_played, last_played_at)
VALUES ('1b2d89fa-bd59-4873-b568-8df26c3a047d', '5a129b2c-3016-4616-bca0-99ec7b067b32', 320, NOW() - INTERVAL '1 day'),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047c', '5a129b2c-3016-4616-bca0-99ec7b067b32', 360, NOW() - INTERVAL '2 days');

-- Game statistics voor Pieter (58e1a434-0797-4d3d-9140-e846b20b7887)
-- Games in library: Tic-Tac-Toe Deluxe compose (favorite), Tic-Tac-Toe Deluxe
INSERT INTO analyticsservice.game_statistics (game_id, player_statistics_id, total_time_played, last_played_at)
VALUES ('1b2d89fa-bd59-4873-b568-8df26c3a047d', '58e1a434-0797-4d3d-9140-e846b20b7887', 150, NOW() - INTERVAL '3 days'),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047c', '58e1a434-0797-4d3d-9140-e846b20b7887', 170, NOW() - INTERVAL '4 days');

-- Game statistics voor Hugo (56c1596a-ec26-4c5d-aa01-31f7a34b76ad)
-- Games in library: Tic-Tac-Toe Deluxe compose, Tic-Tac-Toe Deluxe (favorite)
INSERT INTO analyticsservice.game_statistics (game_id, player_statistics_id, total_time_played, last_played_at)
VALUES ('1b2d89fa-bd59-4873-b568-8df26c3a047d', '56c1596a-ec26-4c5d-aa01-31f7a34b76ad', 420, NOW() - INTERVAL '5 hours'),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047c', '56c1596a-ec26-4c5d-aa01-31f7a34b76ad', 470, NOW() - INTERVAL '1 day');

-- ===== Achievements for each game (10 per game) =====
-- Game: GridMaster (f7b56a1e-1c63-4d0c-aa75-3b40e8253e19)
INSERT INTO gameservice.achievements (game_id, id, title, description, external_ach_id, image_url)
VALUES ('f7b56a1e-1c63-4d0c-aa75-3b40e8253e19', '90000000-0000-0000-0000-000000000001', 'Grid Initiate', 'Complete your first match in GridMaster.', 'EXT-F7-01', 'https://media.giphy.com/media/3o7aD2saalBwwftBIY/giphy.gif'),
       ('f7b56a1e-1c63-4d0c-aa75-3b40e8253e19', '90000000-0000-0000-0000-000000000002', 'Edge Walker', 'Win a round by controlling an edge cell.', 'EXT-F7-02', 'https://media.giphy.com/media/l0HUpt2s9Pclgt9Vm/giphy.gif'),
       ('f7b56a1e-1c63-4d0c-aa75-3b40e8253e19', '90000000-0000-0000-0000-000000000003', 'Dominion', 'Control 50% of the board at end-game.', 'EXT-F7-03', 'https://media.giphy.com/media/5GoVLqeAOo6PK/giphy.gif'),
       ('f7b56a1e-1c63-4d0c-aa75-3b40e8253e19', '90000000-0000-0000-0000-000000000004', 'Comeback Kid', 'Win after being behind by 3+ turns.', 'EXT-F7-04', 'https://media.giphy.com/media/26u4b45b8KlgAB7iM/giphy.gif'),
       ('f7b56a1e-1c63-4d0c-aa75-3b40e8253e19', '90000000-0000-0000-0000-000000000005', 'Speedrunner', 'Finish a game in under 2 minutes.', 'EXT-F7-05', 'https://media.giphy.com/media/3o6Zt481isNVuQI1l6/giphy.gif'),
       ('f7b56a1e-1c63-4d0c-aa75-3b40e8253e19', '90000000-0000-0000-0000-000000000006', 'Tactician', 'Win 10 games in a row.', 'EXT-F7-06', 'https://media.giphy.com/media/xT9IgG50Fb7Mi0prBC/giphy.gif'),
       ('f7b56a1e-1c63-4d0c-aa75-3b40e8253e19', '90000000-0000-0000-0000-000000000007', 'First Blood', 'Score the first capture in a match.', 'EXT-F7-07', 'https://media.giphy.com/media/3o6ZsYb0oQmT2k6vZq/giphy.gif'),
       ('f7b56a1e-1c63-4d0c-aa75-3b40e8253e19', '90000000-0000-0000-0000-000000000008', 'Unstoppable', 'Win with a 100% capture rate.', 'EXT-F7-08', 'https://media.giphy.com/media/3oEduQAsYcJKQH2XsI/giphy.gif'),
       ('f7b56a1e-1c63-4d0c-aa75-3b40e8253e19', '90000000-0000-0000-0000-000000000009', 'Collector', 'Collect all special tiles in a game.', 'EXT-F7-09', 'https://media.giphy.com/media/3o6Zt6ML6BklcajjsA/giphy.gif'),
       ('f7b56a1e-1c63-4d0c-aa75-3b40e8253e19', '90000000-0000-0000-0000-00000000000a', 'Legend', 'Reach top 10 leaderboard.', 'EXT-F7-10', 'https://media.giphy.com/media/l3vR85PnGsBwu1PFK/giphy.gif');

-- Game: Triad Tactics (7aefc340-96ed-4bb9-8262-34b50aa55420)
INSERT INTO gameservice.achievements (game_id, id, title, description, external_ach_id, image_url)
VALUES ('7aefc340-96ed-4bb9-8262-34b50aa55420', '90000000-0000-0000-0000-00000000000b', 'Triad Rookie', 'Play your first Triad Tactics match.', 'EXT-7A-01', 'https://media.giphy.com/media/26n6WywJyh39n1pBu/giphy.gif'),
       ('7aefc340-96ed-4bb9-8262-34b50aa55420', '90000000-0000-0000-0000-00000000000c', 'Tile Master', 'Rotate and place 50 tiles.', 'EXT-7A-02', 'https://media.giphy.com/media/3oEjI6SIIHBdRxXI40/giphy.gif'),
       ('7aefc340-96ed-4bb9-8262-34b50aa55420', '90000000-0000-0000-0000-00000000000d', 'Combo King', 'Chain 3 combos in a single turn.', 'EXT-7A-03', 'https://media.giphy.com/media/3o6fJ1BM7mA3n4r4XS/giphy.gif'),
       ('7aefc340-96ed-4bb9-8262-34b50aa55420', '90000000-0000-0000-0000-00000000000e', 'Strategist', 'Win without losing a tile.', 'EXT-7A-04', 'https://media.giphy.com/media/3o7aCVvGxVb4J3PzC8/giphy.gif'),
       ('7aefc340-96ed-4bb9-8262-34b50aa55420', '90000000-0000-0000-0000-00000000000f', 'Speed Tactician', 'Win under 3 minutes.', 'EXT-7A-05', 'https://media.giphy.com/media/3o7aD2saalBwwftBIY/giphy.gif'),
       ('7aefc340-96ed-4bb9-8262-34b50aa55420', '90000000-0000-0000-0000-000000000010', 'Perfect Fit', 'Place 10 perfect fits in one game.', 'EXT-7A-06', 'https://media.giphy.com/media/l0MYt5jPR6QX5pnqM/giphy.gif'),
       ('7aefc340-96ed-4bb9-8262-34b50aa55420', '90000000-0000-0000-0000-000000000011', 'Comeback', 'Win after being 5 tiles down.', 'EXT-7A-07', 'https://media.giphy.com/media/26xBs6wQGO3z9i6vS/giphy.gif'),
       ('7aefc340-96ed-4bb9-8262-34b50aa55420', '90000000-0000-0000-0000-000000000012', 'Collector', 'Gather all triad types in one game.', 'EXT-7A-08', 'https://media.giphy.com/media/3oEjHP8ELRNNlnlLGM/giphy.gif'),
       ('7aefc340-96ed-4bb9-8262-34b50aa55420', '90000000-0000-0000-0000-000000000013', 'Champion', 'Win 50 ranked matches.', 'EXT-7A-09', 'https://media.giphy.com/media/xT9IgG50Fb7Mi0prBC/giphy.gif'),
       ('7aefc340-96ed-4bb9-8262-34b50aa55420', '90000000-0000-0000-0000-000000000014', 'Legend', 'Reach top 100 leaderboard.', 'EXT-7A-10', 'https://media.giphy.com/media/l3vR85PnGsBwu1PFK/giphy.gif');

-- Game: Forestbound (9a5f6920-29e8-4d34-82ee-9df5249e8e72)
INSERT INTO gameservice.achievements (game_id, id, title, description, external_ach_id, image_url)
VALUES ('9a5f6920-29e8-4d34-82ee-9df5249e8e72', '90000000-0000-0000-0000-000000000015', 'Forest Friend', 'Complete the tutorial of Forestbound.', 'EXT-9A-01', 'https://media.giphy.com/media/3o6ZsYk3f3YV6/giphy.gif'),
       ('9a5f6920-29e8-4d34-82ee-9df5249e8e72', '90000000-0000-0000-0000-000000000016', 'Guardian', 'Protect the heart of the forest.', 'EXT-9A-02', 'https://media.giphy.com/media/26ufdipQqU2lhNA4g/giphy.gif'),
       ('9a5f6920-29e8-4d34-82ee-9df5249e8e72', '90000000-0000-0000-0000-000000000017', 'Cooperator', 'Win a 4-player co-op session.', 'EXT-9A-03', 'https://media.giphy.com/media/3o7qE1YN7aBOFPRw8E/giphy.gif'),
       ('9a5f6920-29e8-4d34-82ee-9df5249e8e72', '90000000-0000-0000-0000-000000000018', 'Explorer', 'Find a hidden glade.', 'EXT-9A-04', 'https://media.giphy.com/media/xT0xeJpnrWC4XWblEk/giphy.gif'),
       ('9a5f6920-29e8-4d34-82ee-9df5249e8e72', '90000000-0000-0000-0000-000000000019', 'Lore Seeker', 'Collect 10 lore cards.', 'EXT-9A-05', 'https://media.giphy.com/media/3o6ZsXQd3/giphy.gif'),
       ('9a5f6920-29e8-4d34-82ee-9df5249e8e72', '90000000-0000-0000-0000-00000000001a', 'Unbroken', 'Finish a session with no players down.', 'EXT-9A-06', 'https://media.giphy.com/media/3og0IBsWqX2g/giphy.gif'),
       ('9a5f6920-29e8-4d34-82ee-9df5249e8e72', '90000000-0000-0000-0000-00000000001b', 'Master Forager', 'Collect 100 resources.', 'EXT-9A-07', 'https://media.giphy.com/media/l0MYt5jPR6QX5pnqM/giphy.gif'),
       ('9a5f6920-29e8-4d34-82ee-9df5249e8e72', '90000000-0000-0000-0000-00000000001c', 'Rite of Passage', 'Complete first world.', 'EXT-9A-08', 'https://media.giphy.com/media/3o7aCTPPm4OHfRlsv/giphy.gif'),
       ('9a5f6920-29e8-4d34-82ee-9df5249e8e72', '90000000-0000-0000-0000-00000000001d', 'Forest Legend', 'Unlock all other achievements.', 'EXT-9A-09', 'https://media.giphy.com/media/26BRv0ThflsHCqDrG/giphy.gif'),
       ('9a5f6920-29e8-4d34-82ee-9df5249e8e72', '90000000-0000-0000-0000-00000000001e', 'Champion of the Glade', 'Win 100 co-op sessions.', 'EXT-9A-10', 'https://media.giphy.com/media/l3q2K5jinAlChoCLS/giphy.gif');

-- Game: Tic-Tac-Toe Deluxe (1b2d89fa-bd59-4873-b568-8df26c3a047c)
INSERT INTO gameservice.achievements (game_id, id, title, description, external_ach_id, image_url)
VALUES ('1b2d89fa-bd59-4873-b568-8df26c3a047c', '90000000-0000-0000-0000-00000000001f', 'First X', 'Play your first Tic-Tac-Toe Deluxe match.', 'EXT-TT-01', 'https://media.giphy.com/media/3oEjI6SIIHBdRxXI40/giphy.gif'),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047c', '90000000-0000-0000-0000-000000000020', 'Perfect Row', 'Win by completing a row.', 'EXT-TT-02', 'https://media.giphy.com/media/xT9IgG50Fb7Mi0prBC/giphy.gif'),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047c', '90000000-0000-0000-0000-000000000021', 'Streak', 'Win 5 games in a row.', 'EXT-TT-03', 'https://media.giphy.com/media/l3vR85PnGsBwu1PFK/giphy.gif'),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047c', '90000000-0000-0000-0000-000000000022', 'Comeback', 'Win after being one move away from losing.', 'EXT-TT-04', 'https://media.giphy.com/media/3o6Zt481isNVuQI1l6/giphy.gif'),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047c', '90000000-0000-0000-0000-000000000023', 'Quick Win', 'Win within 3 moves.', 'EXT-TT-05', 'https://media.giphy.com/media/26ufdipQqU2lhNA4g/giphy.gif'),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047c', '90000000-0000-0000-0000-000000000024', 'Unbeatable', 'Win a game without opponent move.', 'EXT-TT-06', 'https://media.giphy.com/media/3o7aCVvGxVb4J3PzC8/giphy.gif'),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047c', '90000000-0000-0000-0000-000000000025', 'Draw Master', 'Force a draw 10 times.', 'EXT-TT-07', 'https://media.giphy.com/media/3o6fJ1BM7mA3n4r4XS/giphy.gif'),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047c', '90000000-0000-0000-0000-000000000026', 'Board Cleaner', 'Win by filling entire board.', 'EXT-TT-08', 'https://media.giphy.com/media/3o7qE1YN7aBOFPRw8E/giphy.gif'),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047c', '90000000-0000-0000-0000-000000000027', 'Tic Tac Pro', 'Reach level 50.', 'EXT-TT-09', 'https://media.giphy.com/media/3oEduQAsYcJKQH2XsI/giphy.gif'),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047c', '90000000-0000-0000-0000-000000000028', 'Legend', 'Unlock every Tic-Tac-Toe achievement.', 'EXT-TT-10', 'https://media.giphy.com/media/3o6ZsXQd3/giphy.gif');

-- Game: Tic-Tac-Toe Deluxe compose (1b2d89fa-bd59-4873-b568-8df26c3a047d)
INSERT INTO gameservice.achievements (game_id, id, title, description, external_ach_id, image_url)
VALUES ('1b2d89fa-bd59-4873-b568-8df26c3a047d', '90000000-0000-0000-0000-000000000029', 'Compose Starter', 'Play your first compose match.', 'EXT-TTC-01', 'https://media.giphy.com/media/3o6Zt6ML6BklcajjsA/giphy.gif'),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047d', '90000000-0000-0000-0000-00000000002a', 'Harmonizer', 'Win with symmetrical moves.', 'EXT-TTC-02', 'https://media.giphy.com/media/26BRv0ThflsHCqDrG/giphy.gif'),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047d', '90000000-0000-0000-0000-00000000002b', 'Counter', 'Block 10 opponent wins.', 'EXT-TTC-03', 'https://media.giphy.com/media/3o7aCTPPm4OHfRlsv/giphy.gif'),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047d', '90000000-0000-0000-0000-00000000002c', 'Swift', 'Win in under 2 moves.', 'EXT-TTC-04', 'https://media.giphy.com/media/xT0xeJpnrWC4XWblEk/giphy.gif'),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047d', '90000000-0000-0000-0000-00000000002d', 'Persistence', 'Play 100 matches.', 'EXT-TTC-05', 'https://media.giphy.com/media/3o7qE1YN7aBOFPRw8E/giphy.gif'),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047d', '90000000-0000-0000-0000-00000000002e', 'Comeback Artist', 'Win after being one move away from loss.', 'EXT-TTC-06', 'https://media.giphy.com/media/3o6ZsYb0oQmT2k6vZq/giphy.gif'),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047d', '90000000-0000-0000-0000-00000000002f', 'Perfect Game', 'Win with max points.', 'EXT-TTC-07', 'https://media.giphy.com/media/l0MYt5jPR6QX5pnqM/giphy.gif'),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047d', '90000000-0000-0000-0000-000000000030', 'Strategic Mind', 'Win 20 ranked matches.', 'EXT-TTC-08', 'https://media.giphy.com/media/3oEjHP8ELRNNlnlLGM/giphy.gif'),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047d', '90000000-0000-0000-0000-000000000031', 'Composer', 'Unlock all compose achievements.', 'EXT-TTC-09', 'https://media.giphy.com/media/26ufdipQqU2lhNA4g/giphy.gif'),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047d', '90000000-0000-0000-0000-000000000032', 'Compose Legend', 'Reach top 10 in compose leaderboard.', 'EXT-TTC-10', 'https://media.giphy.com/media/l3q2K5jinAlChoCLS/giphy.gif');

-- Game: Campfire Stories (ab842ad3-7426-492f-bfda-4c61689ec2c4)
INSERT INTO gameservice.achievements (game_id, id, title, description, external_ach_id, image_url)
VALUES ('ab842ad3-7426-492f-bfda-4c61689ec2c4', '90000000-0000-0000-0000-000000000033', 'Storyteller', 'Submit your first story prompt.', 'EXT-AB-01', 'https://media.giphy.com/media/3o6Zt481isNVuQI1l6/giphy.gif'),
       ('ab842ad3-7426-492f-bfda-4c61689ec2c4', '90000000-0000-0000-0000-000000000034', 'Campfire Veteran', 'Play 20 sessions.', 'EXT-AB-02', 'https://media.giphy.com/media/3o7aD2saalBwwftBIY/giphy.gif'),
       ('ab842ad3-7426-492f-bfda-4c61689ec2c4', '90000000-0000-0000-0000-000000000035', 'Improv Master', 'Win a storytelling round.', 'EXT-AB-03', 'https://media.giphy.com/media/26ufdipQqU2lhNA4g/giphy.gif'),
       ('ab842ad3-7426-492f-bfda-4c61689ec2c4', '90000000-0000-0000-0000-000000000036', 'Character Builder', 'Create 10 characters.', 'EXT-AB-04', 'https://media.giphy.com/media/l0MYt5jPR6QX5pnqM/giphy.gif'),
       ('ab842ad3-7426-492f-bfda-4c61689ec2c4', '90000000-0000-0000-0000-000000000037', 'Legendary Tale', 'Receive 50 likes on a story.', 'EXT-AB-05', 'https://media.giphy.com/media/3o6ZsXQd3/giphy.gif'),
       ('ab842ad3-7426-492f-bfda-4c61689ec2c4', '90000000-0000-0000-0000-000000000038', 'Campfire Circle', 'Invite 5 friends to a session.', 'EXT-AB-06', 'https://media.giphy.com/media/3o7qE1YN7aBOFPRw8E/giphy.gif'),
       ('ab842ad3-7426-492f-bfda-4c61689ec2c4', '90000000-0000-0000-0000-000000000039', 'Prompt Collector', 'Collect 50 prompt cards.', 'EXT-AB-07', 'https://media.giphy.com/media/3o7aCTPPm4OHfRlsv/giphy.gif'),
       ('ab842ad3-7426-492f-bfda-4c61689ec2c4', '90000000-0000-0000-0000-00000000003a', 'Night Owl', 'Play a session after midnight.', 'EXT-AB-08', 'https://media.giphy.com/media/26BRv0ThflsHCqDrG/giphy.gif'),
       ('ab842ad3-7426-492f-bfda-4c61689ec2c4', '90000000-0000-0000-0000-00000000003b', 'Campfire Champion', 'Win 100 sessions.', 'EXT-AB-09', 'https://media.giphy.com/media/l3vR85PnGsBwu1PFK/giphy.gif'),
       ('ab842ad3-7426-492f-bfda-4c61689ec2c4', '90000000-0000-0000-0000-00000000003c', 'Master Storyteller', 'Unlock all Campfire achievements.', 'EXT-AB-10', 'https://media.giphy.com/media/xT9IgG50Fb7Mi0prBC/giphy.gif');

-- Game: Checkers - compose (ab842ad3-7426-492f-bfda-4c61689ec2c5)
INSERT INTO gameservice.achievements (game_id, id, title, description, external_ach_id, image_url)
VALUES ('ab842ad3-7426-492f-bfda-4c61689ec2c5', '90000000-0000-0000-0000-00000000003d', 'First Capture', 'Capture your first piece in Checkers.', 'EXT-AC-01', 'https://media.giphy.com/media/3o6ZsYb0oQmT2k6vZq/giphy.gif'),
       ('ab842ad3-7426-492f-bfda-4c61689ec2c5', '90000000-0000-0000-0000-00000000003e', 'King Me', 'King a piece.', 'EXT-AC-02', 'https://media.giphy.com/media/3o7qE1YN7aBOFPRw8E/giphy.gif'),
       ('ab842ad3-7426-492f-bfda-4c61689ec2c5', '90000000-0000-0000-0000-00000000003f', 'Flawless', 'Win without losing a piece.', 'EXT-AC-03', 'https://media.giphy.com/media/3o7aCTPPm4OHfRlsv/giphy.gif'),
       ('ab842ad3-7426-492f-bfda-4c61689ec2c5', '90000000-0000-0000-0000-000000000040', 'Comeback', 'Win after being down 3 pieces.', 'EXT-AC-04', 'https://media.giphy.com/media/26ufdipQqU2lhNA4g/giphy.gif'),
       ('ab842ad3-7426-492f-bfda-4c61689ec2c5', '90000000-0000-0000-0000-000000000041', 'Double Jump', 'Perform a double jump capture.', 'EXT-AC-05', 'https://media.giphy.com/media/3o6Zt481isNVuQI1l6/giphy.gif'),
       ('ab842ad3-7426-492f-bfda-4c61689ec2c5', '90000000-0000-0000-0000-000000000042', 'Stalemate', 'Force a stalemate.', 'EXT-AC-06', 'https://media.giphy.com/media/xT0xeJpnrWC4XWblEk/giphy.gif'),
       ('ab842ad3-7426-492f-bfda-4c61689ec2c5', '90000000-0000-0000-0000-000000000043', 'Capture Master', 'Capture 50 pieces.', 'EXT-AC-07', 'https://media.giphy.com/media/3o6ZsXQd3/giphy.gif'),
       ('ab842ad3-7426-492f-bfda-4c61689ec2c5', '90000000-0000-0000-0000-000000000044', 'Tactician', 'Win using only defensive moves.', 'EXT-AC-08', 'https://media.giphy.com/media/3o7aD2saalBwwftBIY/giphy.gif'),
       ('ab842ad3-7426-492f-bfda-4c61689ec2c5', '90000000-0000-0000-0000-000000000045', 'Endgame Pro', 'Win a game in under 10 moves.', 'EXT-AC-09', 'https://media.giphy.com/media/l3vR85PnGsBwu1PFK/giphy.gif'),
       ('ab842ad3-7426-492f-bfda-4c61689ec2c5', '90000000-0000-0000-0000-000000000046', 'Checkers Legend', 'Unlock all Checkers achievements.', 'EXT-AC-10', 'https://media.giphy.com/media/3oEjHP8ELRNNlnlLGM/giphy.gif');

-- ===== Assign achievements unlocked to users (2-5 per user per owned game) =====
-- Note: Only create unlocked achievements for profiles that actually own the game(s). Hugo (56c1596a-ec26-4c5d-aa01-31f7a34b76ad) owns the studio with several games, so we assign unlocked achievements only for Hugo.

-- HUGO (56c1596a-ec26-4c5d-aa01-31f7a34b76ad) - owner of several games
INSERT INTO analyticsservice.game_statistics_achievements (time_unlocked, achievement_id, game_id, player_statistics_id)
VALUES (NOW() - INTERVAL '30 days', '90000000-0000-0000-0000-00000000001f', '1b2d89fa-bd59-4873-b568-8df26c3a047c', '56c1596a-ec26-4c5d-aa01-31f7a34b76ad'),
       (NOW() - INTERVAL '28 days', '90000000-0000-0000-0000-000000000023', '1b2d89fa-bd59-4873-b568-8df26c3a047c', '56c1596a-ec26-4c5d-aa01-31f7a34b76ad'),
       (NOW() - INTERVAL '25 days', '90000000-0000-0000-0000-000000000028', '1b2d89fa-bd59-4873-b568-8df26c3a047c', '56c1596a-ec26-4c5d-aa01-31f7a34b76ad'),
       (NOW() - INTERVAL '14 days', '90000000-0000-0000-0000-000000000029', '1b2d89fa-bd59-4873-b568-8df26c3a047d', '56c1596a-ec26-4c5d-aa01-31f7a34b76ad'),
       (NOW() - INTERVAL '10 days', '90000000-0000-0000-0000-00000000002a', '1b2d89fa-bd59-4873-b568-8df26c3a047d', '56c1596a-ec26-4c5d-aa01-31f7a34b76ad'),
       (NOW() - INTERVAL '9 days',  '90000000-0000-0000-0000-00000000002b', '1b2d89fa-bd59-4873-b568-8df26c3a047d', '56c1596a-ec26-4c5d-aa01-31f7a34b76ad'),
       (NOW() - INTERVAL '7 days',  '90000000-0000-0000-0000-00000000002c', '1b2d89fa-bd59-4873-b568-8df26c3a047d', '56c1596a-ec26-4c5d-aa01-31f7a34b76ad'),
       (NOW() - INTERVAL '3 days',  '90000000-0000-0000-0000-000000000032', '1b2d89fa-bd59-4873-b568-8df26c3a047d', '56c1596a-ec26-4c5d-aa01-31f7a34b76ad');

INSERT INTO profileservice.profile_sections (profile_id, type, visibility)
SELECT p.id, s.type, 'FRIENDS'
FROM profileservice.profile p
         CROSS JOIN (
    VALUES
        ('GAMES'),
        ('FAVORITES'),
        ('STATISTICS'),
        ('FRIENDS'),
        ('ACHIEVEMENTS')
) AS s(type);


-- benefits
INSERT INTO shopservice.benefits (id, type, name, description, point_cost, configuration)
VALUES ('11111111-1111-1111-1111-111111111111', 'USERNAME_COLOR', 'Orange username',
        'Give your username a orange colour', 5000, '#EC7D3C'),
       ('22222222-2222-2222-2222-222222222222', 'USERNAME_COLOR', 'Green username',
        'Give your username a green colour', 5000, '#00FF00'),
       ('33333333-3333-3333-3333-333333333333', 'GAME_DISCOUNT', '10% discount',
        'Get 10% dicount on your next purchase', 2500, '10%'),
       ('44444444-4444-4444-4444-444444444444', 'GAME_DISCOUNT', '25% discount',
        'Get 25% dicount on your next purchase', 5000, '25%'),
       ('55555555-5555-5555-5555-555555555555', 'UNIQUE_PROFILE_PICTURE', 'Archportal avatar',
        'Get a custom archportal avatar', 3000, 'https://hd2.tudocdn.net/1255807?w=1200&h=900'),
       ('66666666-6666-6666-6666-666666666666', 'UNIQUE_PROFILE_PICTURE', 'King avatar',
        'Get a custom king profile picture', 6000, 'https://prod-img.standaard.be/public/nieuws/2dfsyh-koning-filip.jpg/alternates/THREE_TWO_1620/koning-filip.jpg');
