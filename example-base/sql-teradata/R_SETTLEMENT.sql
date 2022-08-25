create view R_SETTLEMENT
as
locking row for access
select
    id as settlement_id,
    region_code,
    region as region_name,
    municipality,
    set_type,
    settlement,
    max(latitude_dms) as latitude_dms,
    max(longitude_dms) as longitude_dms,
    max(latitude_dd) as latitude_dd,
    max(longitude_dd) as longitude_dd,
    max(oktmo) as oktmo
FROM
    T_SETTLEMENTS_STATS
GROUP BY
    settlement_id,
    region_code,
    region,
    municipality,
    set_type,
    settlement;

comment on R_SETTLEMENT IS 'Населенные пукнты Российской Федерации';
comment on R_SETTLEMENT.settlement_id IS 'ID населенного пункта';
comment on R_SETTLEMENT.region_code IS 'Код субъекта Российской Федерации';
comment on R_SETTLEMENT.region_name IS 'Наименование субъекта Российской Федерации';
comment on R_SETTLEMENT.municipality IS 'Наименование муниципального образования';
comment on R_SETTLEMENT.set_type IS 'Тип населенного пункта (город, село, деревня, кожуун, станица и др.)';
comment on R_SETTLEMENT.settlement IS 'Наименование населенного пункта';
comment on R_SETTLEMENT.latitude_dms IS 'Широта населенного пункта в формате DMS';
comment on R_SETTLEMENT.longitude_dms IS 'Долгота населенного пункта в формате DMS';
comment on R_SETTLEMENT.latitude_dd IS 'Широта населенного пункта в формате DD';
comment on R_SETTLEMENT.longitude_dd IS 'Долгота населенного пункта в формате DD';
comment on R_SETTLEMENT.oktmo IS 'Значение кода ОКТМО для населенного пункта';

