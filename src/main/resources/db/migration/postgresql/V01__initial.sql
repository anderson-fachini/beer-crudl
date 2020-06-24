create table beer (
  id uuid primary key,
  name varchar(50) not null,
  description varchar(500) not null,
  harmonization varchar(200) not null,
  color varchar(20) not null,
  alcoholic_strength decimal(4,2) not null,
  temperature decimal(4,2) not null,
  ingredients varchar(2000) not null,
  has_image boolean default false,
  image_type varchar(50),
  created_at timestamp not null,
  updated_at timestamp not null
);

alter table beer add constraint uk_beer_name unique (name);
