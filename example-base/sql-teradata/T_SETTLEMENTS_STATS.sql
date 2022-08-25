CREATE MULTISET TABLE T_SETTLEMENTS_STATS
     (
      id INTEGER,
      region_code INTEGER,
      region VARCHAR(256) CHARACTER SET UNICODE CASESPECIFIC,
      municipality VARCHAR(256) CHARACTER SET UNICODE CASESPECIFIC,
      settlement VARCHAR(256) CHARACTER SET UNICODE CASESPECIFIC,
      set_type VARCHAR(256) CHARACTER SET UNICODE CASESPECIFIC,
      population INTEGER,
      children INTEGER,
      latitude_dms VARCHAR(256) CHARACTER SET UNICODE CASESPECIFIC,
      longitude_dms VARCHAR(256) CHARACTER SET UNICODE CASESPECIFIC,
      latitude_dd FLOAT,
      longitude_dd FLOAT,
      oktmo VARCHAR(256) CHARACTER SET UNICODE CASESPECIFIC,
      dadata INTEGER,
      rosstat INTEGER)
PRIMARY INDEX (id);

COMMENT ON T_SETTLEMENTS_STATS IS 'Статистические данные по населению населённых пунктов России';
COMMENT ON T_SETTLEMENTS_STATS.id IS 'id записи';
COMMENT ON T_SETTLEMENTS_STATS.region IS 'Наименование субъекта Российской Федерации';
COMMENT ON T_SETTLEMENTS_STATS.municipality IS 'Наименование муниципального образования';
COMMENT ON T_SETTLEMENTS_STATS.settlement IS 'Наименование населенного пункта';
COMMENT ON T_SETTLEMENTS_STATS.set_type IS 'Тип населенного пункта (город, село, деревня, кожуун, станица и др.)';
COMMENT ON T_SETTLEMENTS_STATS.population IS 'Население населенного пунктах, всего человек';
COMMENT ON T_SETTLEMENTS_STATS.children IS 'Количество детей в возрасте до 18 лет в населенном пункте, человек';
COMMENT ON T_SETTLEMENTS_STATS.latitude_dms IS 'Широта населенного пункта в формате DMS';
COMMENT ON T_SETTLEMENTS_STATS.longitude_dms IS 'Долгота населенного пункта в формате DMS';
COMMENT ON T_SETTLEMENTS_STATS.latitude_dd IS 'Широта населенного пункта в формате DD';
COMMENT ON T_SETTLEMENTS_STATS.longitude_dd IS 'Долгота населенного пункта в формате DD';
COMMENT ON T_SETTLEMENTS_STATS.oktmo IS 'Значение кода ОКТМО для населенного пункта';
COMMENT ON T_SETTLEMENTS_STATS.dadata IS 'Отметка о том, что код ОКТМО был получен с помощью сервиса стандартизации адресов dadata.ru';
COMMENT ON T_SETTLEMENTS_STATS.rosstat IS 'Отметка о том, что код ОКТМО был получен на основе классификатора ОКТМО, размещенного на официальном сайте Росстата';
