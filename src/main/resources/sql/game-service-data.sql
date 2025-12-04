-- ===== Owners =====
INSERT INTO gameservice.owner (id, game_studio_id)
VALUES ('51d0b9d3-80aa-4d18-9c3b-84b2f8b8d671', '3f071d5d-5d2e-4b5f-9c12-7cf7e902b113'),
       ('7f9244b8-b5ce-4479-96f7-5e02a40211ad', 'd1ed8db6-2c4a-4b72-9481-7dfcb2a3a912');

-- ===== Studios =====
INSERT INTO gameservice.gamestudio (id, owner_id, name, description, iban)
VALUES ('3f071d5d-5d2e-4b5f-9c12-7cf7e902b113', '51d0b9d3-80aa-4d18-9c3b-84b2f8b8d671', 'HexGrid Games',
        'A board-game studio focused on abstract strategy and elegant, minimalistic designs.', 'NL91ABNA0417164300'),
       ('d1ed8db6-2c4a-4b72-9481-7dfcb2a3a912', '7f9244b8-b5ce-4479-96f7-5e02a40211ad', 'Timberwolf Studios',
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
        'https://play-lh.googleusercontent.com/zPxLgj5nvl20ahJV7aFC6S5mD8kii5CEEDj25j1P9CYAfXL9sdDuO-8eES0r4DhJHrU',
        'http://localhost:5174/ttt/', 'STRATEGY', 2),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047d', 'd1ed8db6-2c4a-4b72-9481-7dfcb2a3a912', 'Tic-Tac-Toe Deluxe compose',
        'Speel met kruisjes en gaatjes', 44.99,
        'https://play-lh.googleusercontent.com/zPxLgj5nvl20ahJV7aFC6S5mD8kii5CEEDj25j1P9CYAfXL9sdDuO-8eES0r4DhJHrU',
        'http://localhost/ttt/', 'STRATEGY', 2),
       ('ab842ad3-7426-492f-bfda-4c61689ec2c4', 'd1ed8db6-2c4a-4b72-9481-7dfcb2a3a912', 'Campfire Stories',
        'A family-friendly party game where players build stories using illustrated prompt cards.', 24.99,
        'https://i.kickstarter.com/assets/026/491/434/3d8e598174acdbebfbf2e3348a5b0ff3_original.jpg?anim=false&fit=cover&gravity=auto&height=873&origin=ugc&q=92&v=1568619866&width=1552&sig=w7B94mmyjQcUBHpfytD9pVa5h8Cm9ilkmNnW9hvYoOI%3D',
        'https://hub.example.com/games/campfire-stories', 'FAMILY', 20);

INSERT INTO profileservice.profile (id, first_name, last_name, gamer_tag, icon, platform_points, email)
VALUES ('fdb74a46-98e2-403a-bfeb-22fa10d11bf2', 'Cian', 'Van Acker', 'naic56', null, 0, 'cian'),
       ('09bcb315-b8ac-4376-a501-8249e5fd34fb', 'Axel', 'Peeters', 'lexa', null, 0, 'axel'),
       ('09bcb315-b8ac-4376-a501-8249e5fd34fc', 'Pieter', 'Neyt', 'reteip', null, 0, 'pieter'),
       ('09bcb315-b8ac-4376-a501-8249e5fd34fd', 'Hugo', 'Dor', 'oguh', null, 0, 'hugo'),
       ('1aef6aef-44ce-43c7-b52b-cb562eb02e64', 'Hugo', 'Dor', 'BIGHIC', null, 0, 'hugo.dor@student.kdg.be');

INSERT INTO profileservice.profile_library (game_id, profile_id)
VALUES ('1b2d89fa-bd59-4873-b568-8df26c3a047d', 'fdb74a46-98e2-403a-bfeb-22fa10d11bf2');

INSERT INTO profileservice.profile_friends (profile_id, friend_id)
VALUES ('fdb74a46-98e2-403a-bfeb-22fa10d11bf2', '09bcb315-b8ac-4376-a501-8249e5fd34fb'),
       ('fdb74a46-98e2-403a-bfeb-22fa10d11bf2', '09bcb315-b8ac-4376-a501-8249e5fd34fc');

INSERT INTO communicationservice.notification_settings (profile_id)
VALUES ('fdb74a46-98e2-403a-bfeb-22fa10d11bf2');

INSERT INTO communicationservice.notification_channel_types (profile_id, channel_type)
VALUES ('fdb74a46-98e2-403a-bfeb-22fa10d11bf2', 'EMAIL'),
       ('fdb74a46-98e2-403a-bfeb-22fa10d11bf2', 'IN_PLATFORM');


INSERT INTO communicationservice.notification_settings (profile_id)
VALUES ('1aef6aef-44ce-43c7-b52b-cb562eb02e64');

INSERT INTO communicationservice.notification_channel_types (profile_id, channel_type)
VALUES ('1aef6aef-44ce-43c7-b52b-cb562eb02e64', 'IN_PLATFORM');


INSERT INTO communicationservice.notifications (id, reciever_id, title, body, type, created_at)
VALUES
    ('e1a1b2c3-d4f5-6789-abcd-ef0123456789', '1aef6aef-44ce-43c7-b52b-cb562eb02e64', 'Welcome!', 'Thanks for joining our platform.', 'SYSTEM', NOW()), ('a2b3c4d5-e6f7-8901-abcd-234567890abc', '1aef6aef-44ce-43c7-b52b-cb562eb02e64', 'New Message', 'You have received a new chat message.', 'CHAT', NOW()), ('b3c4d5e6-f7a8-9012-bcde-345678901bcd', '1aef6aef-44ce-43c7-b52b-cb562eb02e64', 'Achievement Unlocked', 'You reached Level 5!', 'ACHIEVEMENT', NOW()), ('c4d5e6f7-a8b9-0123-cdef-456789012cde', '1aef6aef-44ce-43c7-b52b-cb562eb02e64', 'Game Invite', 'Your friend invited you to a game.', 'GAME_INVITE', NOW()), ('d5e6f7a8-b9c0-1234-def0-567890123def', '1aef6aef-44ce-43c7-b52b-cb562eb02e64', 'Friend Request', 'John Doe sent you a friend request.', 'FRIEND_REQUEST', NOW()), ('e6f7a8b9-c0d1-2345-ef01-678901234ef0', '1aef6aef-44ce-43c7-b52b-cb562eb02e64', 'Your Turn', 'It’s your turn to play.', 'TURN_REMINDER', NOW()), ('f7a8b9c0-d1e2-3456-f012-789012345f01', '1aef6aef-44ce-43c7-b52b-cb562eb02e64', 'System Update', 'We have updated our terms of service.', 'SYSTEM', NOW());
