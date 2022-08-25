create view R_SET_TYPE
as
locking row for access
select
    set_type,
    (case
        when set_type = 'г' then 'город'
        when set_type IN ('д', 'д.') then 'деревня'
        when set_type = 'х' then 'хутор'
        when set_type = 'мкр' then 'микрорайон'
        when set_type = 'с' then 'село'
        when set_type = 'п' then 'посёлок'
        when set_type = 'ст-ца' then 'станица'
        when set_type = 'пгт' then 'посёлок городского типа'
        else set_type
        end) as set_type_name;
from
    T_SETTLEMENTS_STATS
group by
    set_type;
    
comment on R_SET_TYPE IS 'Типы населённых пунктов Российской Федерации';
comment on R_SET_TYPE.set_type IS 'Тип населенного пункта (город, село, деревня, кожуун, станица и др.)';
comment on R_SET_TYPE.set_type_name IS 'Расшифровка типа населенного пункта';