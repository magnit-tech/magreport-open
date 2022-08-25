create view R_RF_REGION
as
    locking row for access
    SELECT
        region_code,
        region_name
    FROM
        T_RF_REGION;

comment on R_RF_REGION is 'Справочник субъектов РФ';
comment on R_RF_REGION.region_code is 'Код региона РФ';
comment on R_RF_REGION.region_name is 'Название региона РФ';        