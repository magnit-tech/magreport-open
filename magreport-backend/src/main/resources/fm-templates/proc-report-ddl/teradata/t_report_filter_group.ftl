<#-- @ftlvariable name="schema" type="java.lang.String" -->
CREATE MULTISET TABLE ${schema}.T_REPORT_FILTER_GROUP,
NO FALLBACK,
NO BEFORE JOURNAL,
NO AFTER JOURNAL,
CHECKSUM = DEFAULT,
DEFAULT MERGEBLOCKRATIO
(
    JOB_ID INTEGER,
    FILTER_GROUP_NAME VARCHAR(255) CHARACTER SET UNICODE CASESPECIFIC,
    PARENT_FILTER_GROUP_NAME VARCHAR(255) CHARACTER SET UNICODE CASESPECIFIC,
    GROUP_OPERATION VARCHAR(255) CHARACTER SET UNICODE CASESPECIFIC
) NO PRIMARY INDEX ;
