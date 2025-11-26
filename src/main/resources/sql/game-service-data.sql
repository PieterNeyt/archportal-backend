-- ===== Owners =====
INSERT INTO gameservice.owner (id, first_name, last_name, email)
VALUES ('51d0b9d3-80aa-4d18-9c3b-84b2f8b8d671', 'Alice', 'Van Houten', 'alice.vh@example.com'),
       ('7f9244b8-b5ce-4479-96f7-5e02a40211ad', 'Bob', 'Timber', 'bob.timber@example.com');

-- ===== Studios =====
INSERT INTO gameservice.gamestudio (id, owner_id, name, description, iban)
VALUES ('3f071d5d-5d2e-4b5f-9c12-7cf7e902b113', '51d0b9d3-80aa-4d18-9c3b-84b2f8b8d671', 'HexGrid Games',
        'A board-game studio focused on abstract strategy and elegant, minimalistic designs.', 'NL91ABNA0417164300'),
       ('d1ed8db6-2c4a-4b72-9481-7dfcb2a3a912', '7f9244b8-b5ce-4479-96f7-5e02a40211ad', 'Timberwolf Studios',
        'Specializes in thematic adventure and family-friendly board games with rich world-building.',
        'NL32RABO0287365401');

-- ===== Games =====
INSERT INTO gameservice.games (id, studio_id, title, description, price, image_url, game_url, genre)
VALUES ('f7b56a1e-1c63-4d0c-aa75-3b40e8253e19', '3f071d5d-5d2e-4b5f-9c12-7cf7e902b113', 'GridMaster',
        'An abstract strategy game where players battle for territory dominance on a shrinking grid.', 29.99,
        'https://framerusercontent.com/images/csQYwDfKsYvDh4jwg3mTQVEJ8.png?width=1920&height=1080', 'https://hub.example.com/games/gridmaster', 'ABSTRACT'),
       ('7aefc340-96ed-4bb9-8262-34b50aa55420', '3f071d5d-5d2e-4b5f-9c12-7cf7e902b113', 'Triad Tactics',
        'A quick-play strategy game where players rotate tri-shaped tiles to outmaneuver opponents.', 19.99,
        'https://shared.akamai.steamstatic.com/store_item_assets/steam/apps/3032540/capsule_616x353.jpg?t=1760843504', 'https://hub.example.com/games/triad-tactics', 'STRATEGY'),
       ('9a5f6920-29e8-4d34-82ee-9df5249e8e72', 'd1ed8db6-2c4a-4b72-9481-7dfcb2a3a912', 'Forestbound',
        'A cooperative adventure where players defend an ancient forest from encroaching corruption.', 44.99,
        'https://us1.discourse-cdn.com/flex020/uploads/makecode/original/3X/a/6/a6d432439027cb0df20f1907aa9cfca496add0e4.png', 'https://hub.example.com/games/forestbound', 'ADVENTURE'),
        ('1b2d89fa-bd59-4873-b568-8df26c3a047c', 'd1ed8db6-2c4a-4b72-9481-7dfcb2a3a912', 'Tic-Tac-Toe Deluxe',
        'Speel met kruisjes en gaatjes', 44.99,
        'https://play-lh.googleusercontent.com/zPxLgj5nvl20ahJV7aFC6S5mD8kii5CEEDj25j1P9CYAfXL9sdDuO-8eES0r4DhJHrU', 'http://localhost:5173/', 'STRATEGY'),
       ('ab842ad3-7426-492f-bfda-4c61689ec2c4', 'd1ed8db6-2c4a-4b72-9481-7dfcb2a3a912', 'Campfire Stories',
        'A family-friendly party game where players build stories using illustrated prompt cards.', 24.99,
        'https://i.kickstarter.com/assets/026/491/434/3d8e598174acdbebfbf2e3348a5b0ff3_original.jpg?anim=false&fit=cover&gravity=auto&height=873&origin=ugc&q=92&v=1568619866&width=1552&sig=w7B94mmyjQcUBHpfytD9pVa5h8Cm9ilkmNnW9hvYoOI%3D', 'https://hub.example.com/games/campfire-stories', 'FAMILY');
