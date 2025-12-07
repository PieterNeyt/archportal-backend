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
        'https://content.tinytap.it/2CF7E204-AE31-418B-A18D-3212C7EDBBC6/coverImage.png?ver=0',
        'http://localhost:5174/ttt/', 'STRATEGY', 2),
       ('1b2d89fa-bd59-4873-b568-8df26c3a047d', 'd1ed8db6-2c4a-4b72-9481-7dfcb2a3a912', 'Tic-Tac-Toe Deluxe compose',
        'Speel met kruisjes en gaatjes', 44.99,
        'https://content.tinytap.it/2CF7E204-AE31-418B-A18D-3212C7EDBBC6/coverImage.png?ver=0', 'http://localhost/ttt/',
        'STRATEGY', 2),
       ('ab842ad3-7426-492f-bfda-4c61689ec2c4', 'd1ed8db6-2c4a-4b72-9481-7dfcb2a3a912', 'Campfire Stories',
        'A family-friendly party game where players build stories using illustrated prompt cards.', 24.99,
        'https://i.kickstarter.com/assets/026/491/434/3d8e598174acdbebfbf2e3348a5b0ff3_original.jpg?anim=false&fit=cover&gravity=auto&height=873&origin=ugc&q=92&v=1568619866&width=1552&sig=w7B94mmyjQcUBHpfytD9pVa5h8Cm9ilkmNnW9hvYoOI%3D',
        'https://hub.example.com/games/campfire-stories', 'FAMILY', 20);

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

INSERT INTO profileservice.profile_friends (profile_id, friend_id)
VALUES ('ed7b6a30-a12f-4ae5-9d25-b1ea51c89840', '5a129b2c-3016-4616-bca0-99ec7b067b32'),
       ('ed7b6a30-a12f-4ae5-9d25-b1ea51c89840', '58e1a434-0797-4d3d-9140-e846b20b7887'),
       ('5a129b2c-3016-4616-bca0-99ec7b067b32', 'ed7b6a30-a12f-4ae5-9d25-b1ea51c89840'),
       ('58e1a434-0797-4d3d-9140-e846b20b7887', 'ed7b6a30-a12f-4ae5-9d25-b1ea51c89840');

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
       