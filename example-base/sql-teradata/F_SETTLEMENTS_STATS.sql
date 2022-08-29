CREATE VIEW F_SETTLEMENTS_STATS
as
locking row for access
SELECT
    id as settlement_id,
    region_code,
    region as region_name,
    municipality,
    settlement,
    set_type,
    population,
    children,
    latitude_dms,
    longitude_dms,
    latitude_dd,
    longitude_dd,
    oktmo
FROM
    T_SETTLEMENTS_STATS;

COMMENT ON F_SETTLEMENTS_STATS IS 'Статистические данные по населению населённых пунктов России';
COMMENT ON F_SETTLEMENTS_STATS.settlement_id IS 'id населённого пункта';
COMMENT ON F_SETTLEMENTS_STATS.region_code IS 'Код субъекта Российской Федерации';
COMMENT ON F_SETTLEMENTS_STATS.region_name IS 'Наименование субъекта Российской Федерации';
COMMENT ON F_SETTLEMENTS_STATS.municipality IS 'Наименование муниципального образования';
COMMENT ON F_SETTLEMENTS_STATS.settlement IS 'Наименование населенного пункта';
COMMENT ON F_SETTLEMENTS_STATS.set_type IS 'Тип населенного пункта (город, село, деревня, кожуун, станица и др.)';
COMMENT ON F_SETTLEMENTS_STATS.population IS 'Население населенного пунктах, всего человек';
COMMENT ON F_SETTLEMENTS_STATS.children IS 'Количество детей в возрасте до 18 лет в населенном пункте, человек';
COMMENT ON F_SETTLEMENTS_STATS.latitude_dms IS 'Широта населенного пункта в формате DMS';
COMMENT ON F_SETTLEMENTS_STATS.longitude_dms IS 'Долгота населенного пункта в формате DMS';
COMMENT ON F_SETTLEMENTS_STATS.latitude_dd IS 'Широта населенного пункта в формате DD';
COMMENT ON F_SETTLEMENTS_STATS.longitude_dd IS 'Долгота населенного пункта в формате DD';
COMMENT ON F_SETTLEMENTS_STATS.oktmo IS 'Значение кода ОКТМО для населенного пункта';

