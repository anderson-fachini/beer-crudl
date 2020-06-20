create table beer (
  id uuid primary key,
  name varchar(50) not null,
  color varchar(50) not null,
  alcoholic_strength decimal(4,2) not null,
  temperature decimal(4,2) not null,
  ingredients varchar(2000) not null,
  has_image boolean default false,
  image_type varchar(50),
  created_at timestamp not null,
  updated_at timestamp not null
);
