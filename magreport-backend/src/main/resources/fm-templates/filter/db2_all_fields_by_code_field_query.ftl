<#-- @ftlvariable name="" type="ru.magnit.magreportbackend.dto.inner.filter.FilterRequestData" -->
SELECT
<#list filter().fields() as field><#--
-->    ${field.fieldName()}<#sep>,</#sep>
</#list><#--
-->FROM ${filter().schemaName()}.${filter().tableName()}
WHERE
${filter().getFieldNameByFieldId(getFieldId())} IN (<#--
--><#list tuples() as tuple><#--
--><#list tuple.values as value><#--
-->${filter().getValue(value)}<#sep>, </#sep><#--
--></#list><#sep>, </#sep><#--
--></#list>)<#--
-->