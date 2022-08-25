<#-- @ftlvariable name="" type="ru.magnit.magreportbackend.dto.inner.filter.FilterValueListRequestData" -->
SELECT TOP ${maxCount()}
${idFieldName()}, ${codeFieldName()}, ${nameFieldName()}
FROM ${schemaName()}.${tableName()}
WHERE ${filterFieldName()} <#if isCaseSensitive()>(CS)<#else>(NOT CS)</#if> LIKE
<#if likenessType() == "STARTS">
'${searchValue()}%'
<#elseif likenessType() == "CONTAINS">
'%${searchValue()}%'
<#elseif likenessType() == "ENDS">
'%${searchValue()}'
</#if>
<#if isCaseSensitive()>(CS)<#else>(NOT CS)</#if>