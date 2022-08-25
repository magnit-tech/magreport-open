<#-- @ftlvariable name="schema" type="java.lang.String" -->
CREATE MULTISET TABLE ${schema}.T_REPORT_FILTER_FIELD,
NO FALLBACK,
NO BEFORE JOURNAL,
NO AFTER JOURNAL,
CHECKSUM = DEFAULT,
DEFAULT MERGEBLOCKRATIO
(
    JOB_ID INTEGER,
    REPORT_FILTER_FIELD_ID INTEGER,
    TUPLE_ID INTEGER,
    FIELD_NAME VARCHAR(255) CHARACTER SET UNICODE CASESPECIFIC,
    DATA_TYPE_FIELD VARCHAR(255) CHARACTER SET UNICODE CASESPECIFIC,
    LEVEL INTEGER
) NO PRIMARY INDEX ;
