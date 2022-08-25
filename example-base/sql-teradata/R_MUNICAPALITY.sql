create view R_MUNICIPALITY
as
locking row for access
select
    region_code,
    region as region_name,
    municipality
FROM
    prd_db_magrep.T_SETTLEMENTS_STATS
GROUP BY
    region_code,
    region,
    municipality;

comment on R_MUNICIPALITY IS 'Муниципальные образования Российской Федерации';
comment on R_MUNICIPALITY.region_code IS 'Код субъекта Российской Федерации';
comment on R_MUNICIPALITY.region_name IS 'Наименование субъекта Российской Федерации';
comment on R_MUNICIPALITY.municipality IS 'Наименование муниципального образования';
