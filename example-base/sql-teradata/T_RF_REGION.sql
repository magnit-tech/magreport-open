create table T_RF_REGION
(
    region_code     integer,
    region_name     varchar(256)
)
primary index(region_code);

comment on T_RF_REGION is 'Регионы РФ';
comment on T_RF_REGION.region_code is 'Код региона РФ';
comment on T_RF_REGION.region_name is 'Название региона РФ';