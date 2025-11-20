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
        'https://example.com/images/gridmaster.jpg', 'https://hub.example.com/games/gridmaster', 'ABSTRACT'),
       ('7aefc340-96ed-4bb9-8262-34b50aa55420', '3f071d5d-5d2e-4b5f-9c12-7cf7e902b113', 'Triad Tactics',
        'A quick-play strategy game where players rotate tri-shaped tiles to outmaneuver opponents.', 19.99,
        'https://example.com/images/triad-tactics.jpg', 'https://hub.example.com/games/triad-tactics', 'STRATEGY'),
       ('9a5f6920-29e8-4d34-82ee-9df5249e8e72', 'd1ed8db6-2c4a-4b72-9481-7dfcb2a3a912', 'Forestbound',
        'A cooperative adventure where players defend an ancient forest from encroaching corruption.', 44.99,
        'https://example.com/images/forestbound.jpg', 'https://hub.example.com/games/forestbound', 'ADVENTURE'),
       ('ab842ad3-7426-492f-bfda-4c61689ec2c4', 'd1ed8db6-2c4a-4b72-9481-7dfcb2a3a912', 'Campfire Stories',
        'A family-friendly party game where players build stories using illustrated prompt cards.', 24.99,
        'https://example.com/images/campfire-stories.jpg', 'https://hub.example.com/games/campfire-stories', 'FAMILY');
