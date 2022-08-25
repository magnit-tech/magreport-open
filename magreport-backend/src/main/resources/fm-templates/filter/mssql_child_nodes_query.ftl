<#-- @ftlvariable name="" type="ru.magnit.magreportbackend.dto.inner.filter.FilterChildNodesRequestData" -->
SELECT
${idFieldName()}, ${nameFieldName()}
FROM ${schemaName()}.${tableName()}
<#if (level() > 0)>
    WHERE
    <#list pathValues().entrySet() as entry>
        ${entry.getKey()} = ${entry.getValue()} <#sep>AND</#sep>
    </#list>
</#if>
<#if (!securitySettings().isEmpty())>
    <#if (level() == 0)> WHERE <#else> AND </#if>
    <#list securitySettings() as securityFilter>
        <#if (securityFilter.filterType().name() == "HIERARCHY")>(${securityFilter.getImpalaSqlStrict()})</#if>
        <#if (securityFilter.filterType().name() == "HIERARCHY_M2M")>(${securityFilter.getImpalaSqlM2M()})</#if>
        <#sep> AND </#sep>
    </#list>
</#if>
GROUP BY
${idFieldName()}, ${nameFieldName()};