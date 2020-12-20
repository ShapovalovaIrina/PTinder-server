create sequence hibernate_sequence start 1 increment 1;

create table animal_type
(
    id   int8 not null,
    type varchar(255),
    primary key (id)
);
create table favourite_pets
(
    user_id varchar(255) not null,
    pet_id  int8         not null,
    primary key (user_id, pet_id)
);
create table notifications
(
    id      int8    not null,
    is_read boolean not null,
    text    varchar(2048),
    primary key (id)
);
create table pets
(
    pet_id         int8 not null,
    age            int4,
    breed          varchar(255),
    comment        varchar(500),
    gender         int4,
    name           varchar(255),
    purpose        int4,
    animal_type_id int8,
    owner_id       varchar(255),
    primary key (pet_id)
);
create table photos
(
    id     int8 not null,
    photo  bytea,
    pet_id int8 not null,
    primary key (id)
);
create table users
(
    google_id              varchar(255) not null,
    address                varchar(255),
    email                  varchar(255),
    first_name             varchar(255),
    gender                 int4,
    is_contact_info_public boolean      not null,
    last_name              varchar(255),
    middle_name            varchar(255),
    number                 varchar(255),
    photo_url              varchar(255),
    primary key (google_id)
);
alter table if exists favourite_pets
    add constraint favourite_pet_pet_fk foreign key (pet_id) references pets;
alter table if exists favourite_pets
    add constraint favourite_pet_user_fk foreign key (user_id) references users;
alter table if exists pets
    add constraint animal_type_pet_fk foreign key (animal_type_id) references animal_type;
alter table if exists pets
    add constraint pets_user_fk foreign key (owner_id) references users;
alter table if exists photos
    add constraint photos_pet_fk foreign key (pet_id) references pets;