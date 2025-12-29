create table analyticsservice.game_statistics
(
    last_played_at         timestamp(6),
    total_playtime_minutes bigint,
    game_id                uuid not null,
    profile_id             uuid not null,
    primary key (game_id, profile_id)
);

alter table analyticsservice.game_statistics
    owner to "user";

create table analyticsservice.game_statistics_achievements
(
    time_unlocked  timestamp(6) not null,
    achievement_id uuid         not null,
    game_id        uuid         not null,
    profile_id     uuid         not null,
    constraint fkk4v5751ifp0x4yt71ctkoexbl
        foreign key (game_id, profile_id) references analyticsservice.game_statistics
);

alter table analyticsservice.game_statistics_achievements
    owner to "user";

create table analyticsservice.game_statistics_winner_records
(
    played_at  timestamp(6) not null,
    game_id    uuid         not null,
    profile_id uuid         not null,
    session_id uuid         not null,
    winner     varchar(255) not null,
    constraint fkg8x5ekhxkglxh1jbu4gbi62fi
        foreign key (game_id, profile_id) references analyticsservice.game_statistics
);

alter table analyticsservice.game_statistics_winner_records
    owner to "user";

create table communicationservice.chat_room
(
    id    uuid         not null
        primary key,
    title varchar(100) not null
);

alter table communicationservice.chat_room
    owner to "user";

create table communicationservice.chat_room_member
(
    chat_room_id uuid not null
        constraint fko6a9v51aal2574fjb1ldlw4di
            references communicationservice.chat_room,
    members      uuid
);

alter table communicationservice.chat_room_member
    owner to "user";

create table communicationservice.message
(
    timestamp    timestamp(6) not null,
    chat_room_id uuid         not null
        constraint fk5i8ac68n051032d9ga7gg6i85
            references communicationservice.chat_room,
    id           uuid         not null
        primary key,
    sender       uuid         not null,
    text         varchar(500) not null
);

alter table communicationservice.message
    owner to "user";

create table communicationservice.notification_settings
(
    profile_id uuid not null
        primary key
);

alter table communicationservice.notification_settings
    owner to "user";

create table communicationservice.notification_channel_types
(
    profile_id   uuid not null
        constraint fk75ovvmffir1mtl2tgci6o0xmh
            references communicationservice.notification_settings,
    channel_type varchar(255)
        constraint notification_channel_types_channel_type_check
            check ((channel_type)::text = ANY
        ((ARRAY ['IN_PLATFORM'::character varying, 'EMAIL'::character varying])::text[]))
    );

alter table communicationservice.notification_channel_types
    owner to "user";

create table communicationservice.notifications
(
    created_at  timestamp(6)  not null,
    id          uuid          not null
        primary key,
    receiver_id uuid          not null,
    body        varchar(2000) not null,
    title       varchar(255)  not null,
    type        varchar(255)  not null
        constraint notifications_type_check
            check ((type)::text = ANY
        ((ARRAY ['CHAT'::character varying, 'SYSTEM'::character varying, 'ACHIEVEMENT'::character varying, 'PARTY_INVITE'::character varying, 'GAME_INVITE'::character varying, 'FRIEND_REQUEST'::character varying, 'TURN_REMINDER'::character varying])::text[]))
    );

alter table communicationservice.notifications
    owner to "user";

create table gameservice.games
(
    max_lobby_size integer        not null,
    price          numeric(19, 2) not null,
    id             uuid           not null
        primary key,
    studio_id      uuid           not null,
    title          varchar(100)   not null,
    description    varchar(255)   not null,
    game_url       varchar(255)   not null,
    genre          varchar(255)   not null
        constraint games_genre_check
            check ((genre)::text = ANY
        ((ARRAY ['STRATEGY'::character varying, 'FAMILY'::character varying, 'PARTY'::character varying, 'COOPERATIVE'::character varying, 'DEDUCTION'::character varying, 'DECK_BUILDING'::character varying, 'WORKER_PLACEMENT'::character varying, 'AREA_CONTROL'::character varying, 'ABSTRACT'::character varying, 'ADVENTURE'::character varying, 'ECONOMIC'::character varying, 'PUZZLE'::character varying, 'WAR'::character varying, 'THEMED'::character varying, 'SOCIAL_DEDUCTION'::character varying, 'ENGINE_BUILDING'::character varying])::text[])),
    image_url      varchar(255)
);

alter table gameservice.games
    owner to "user";

create table gameservice.achievements
(
    game_id         uuid
        constraint fkmm6cv053k2jjggcpcuv3qmpus
            references gameservice.games,
    id              uuid         not null
        primary key,
    title           varchar(100) not null,
    description     varchar(255) not null,
    external_ach_id varchar(255) not null,
    image_url       varchar(255) not null
);

alter table gameservice.achievements
    owner to "user";

create table gameservice.gamestudio
(
    id          uuid         not null
        primary key,
    owner_id    uuid         not null,
    description varchar(255) not null,
    iban        varchar(255) not null,
    name        varchar(255) not null
);

alter table gameservice.gamestudio
    owner to "user";

create table gameservice.owner
(
    game_studio_id uuid not null,
    id             uuid not null
        primary key
);

alter table gameservice.owner
    owner to "user";

create table lobbyservice.game_lobbies
(
    max_players       integer not null,
    game_id           uuid    not null,
    game_lobby_id     uuid    not null
        primary key,
    game_lobby_status varchar(255)
        constraint game_lobbies_game_lobby_status_check
            check ((game_lobby_status)::text = ANY
        ((ARRAY ['OPEN'::character varying, 'FULL'::character varying, 'CLOSED'::character varying])::text[]))
    );

alter table lobbyservice.game_lobbies
    owner to "user";

create table lobbyservice.game_lobby_players
(
    game_lobby_id uuid not null
        constraint fkk9kvbh06785b8wvsq05ljhf1b
            references lobbyservice.game_lobbies,
    player_id     uuid not null
);

alter table lobbyservice.game_lobby_players
    owner to "user";

create table lobbyservice.game_sessions
(
    end_time        timestamp(6),
    start_time      timestamp(6) not null,
    game_lobby_id   uuid
        constraint fks1bmb3r8omk2lv39vvaws3d8u
            references lobbyservice.game_lobbies,
    game_session_id uuid         not null
        primary key,
    player_id       uuid         not null,
    launch_url      varchar(255)
);

alter table lobbyservice.game_sessions
    owner to "user";

create table lobbyservice.party
(
    max_members  integer      not null,
    chat_room_id uuid         not null,
    host_id      uuid         not null,
    id           uuid         not null
        primary key,
    title        varchar(100) not null
);

alter table lobbyservice.party
    owner to "user";

create table lobbyservice.party_invite
(
    id          uuid not null
        primary key,
    party_id    uuid not null
        constraint fkg0d7md7nqv6y8unxo7tfr722f
            references lobbyservice.party,
    receiver_id uuid not null,
    sender_id   uuid not null
);

alter table lobbyservice.party_invite
    owner to "user";

create table lobbyservice.party_member
(
    members  uuid,
    party_id uuid not null
        constraint fkctrpcp93h130dwe6j1jlhf960
            references lobbyservice.party
);

alter table lobbyservice.party_member
    owner to "user";

create table profileservice.friendship
(
    id           uuid not null
        primary key,
    profile_a_id uuid not null,
    profile_b_id uuid not null,
    unique (profile_a_id, profile_b_id)
);

alter table profileservice.friendship
    owner to "user";

create table profileservice.profile
(
    platform_points integer      not null,
    id              uuid         not null
        primary key,
    email           varchar(255) not null,
    first_name      varchar(255) not null,
    gamer_tag       varchar(255) not null
        unique,
    icon            varchar(255),
    last_name       varchar(255) not null
);

alter table profileservice.profile
    owner to "user";

create table profileservice.friend_requests
(
    receiver_id uuid not null
        constraint fk15vobrb7rv66cvbjkrtdqvav
            references profileservice.profile,
    sender_id   uuid not null,
    constraint uq_friend_request_sender_receiver
        primary key (sender_id, receiver_id)
);

alter table profileservice.friend_requests
    owner to "user";

create table profileservice.profile_library
(
    favorite   boolean not null,
    id         uuid    not null,
    profile_id uuid    not null
        constraint fkqcvi1bxqexcua044kqsptultb
            references profileservice.profile
);

alter table profileservice.profile_library
    owner to "user";

create table profileservice.profile_platform_benefits
(
    benefit_id uuid not null,
    profile_id uuid not null
        constraint fk7dedl6qrgn7cmg2kv104ha710
            references profileservice.profile
);

alter table profileservice.profile_platform_benefits
    owner to "user";

create table shopservice.cart
(
    id         uuid not null
        primary key,
    profile_id uuid not null
);

alter table shopservice.cart
    owner to "user";

create table shopservice.cart_items
(
    cart_id uuid not null
        constraint fk99e0am9jpriwxcm6is7xfedy3
            references shopservice.cart,
    game_id uuid not null
);

alter table shopservice.cart_items
    owner to "user";

create table shopservice.orders
(
    completed  boolean,
    id         uuid not null
        primary key,
    profile_id uuid not null,
    payment_id varchar(255)
);

alter table shopservice.orders
    owner to "user";

create table shopservice.order_line
(
    price    numeric(38, 2) not null,
    game_id  uuid           not null,
    id       uuid           not null
        primary key,
    order_id uuid           not null
        constraint fkk9f9t1tmkbq5w27u8rrjbxxg6
            references shopservice.orders
);

alter table shopservice.order_line
    owner to "user";


create table shopservice.benefits
(
    point_cost    integer      not null,
    id            uuid         not null
        primary key,
    configuration varchar(255),
    description   varchar(255),
    name          varchar(255) not null,
    type          varchar(255) not null
        constraint benefits_type_check
            check ((type)::text = ANY
                   ((ARRAY ['USERNAME_COLOR'::character varying, 'GAME_DISCOUNT'::character varying, 'UNIQUE_PROFILE_PICTURE'::character varying])::text[]))
);

alter table shopservice.benefits
    owner to "user";