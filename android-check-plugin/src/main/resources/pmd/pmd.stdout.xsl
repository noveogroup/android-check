<?xml version="1.0" encoding="UTF-8" standalone="no"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:output indent="yes" method="text" />
    <xsl:decimal-format decimal-separator="." grouping-separator="," />

    <xsl:key name="files" match="file" use="@name" />

    <xsl:template match="pmd">
    PMD: <xsl:apply-templates mode="summary" select="." />
        Files: <xsl:apply-templates select="file[@name and generate-id(.) = generate-id(key('files', @name))]" />

    </xsl:template>

    <xsl:template match="file">
        <xsl:variable name="violationCount" select="count(violation)" />
        - <xsl:value-of select="@name" />
            Violations: <xsl:value-of select="$violationCount" />
            <xsl:for-each select="key('files', @name)/violation">
                <xsl:sort data-type="number" order="ascending" select="@beginline" />
                Violation: <xsl:value-of select="normalize-space(node())" />
                    Location: <xsl:value-of select="@beginline" />:<xsl:value-of select="@begincolumn" /> - <xsl:value-of select="@endline" />:<xsl:value-of select="@endcolumn" />
            </xsl:for-each>
    </xsl:template>

    <xsl:template match="pmd" mode="summary">
        <xsl:variable name="fileCount" select="count(file[@name and generate-id(.) = generate-id(key('files', @name))])" />
        <xsl:variable name="violationCount" select="count(file/violation)" />
        Summary:
            Files: <xsl:value-of select="$fileCount" />
            Violations: <xsl:value-of select="$violationCount" />
    </xsl:template>

</xsl:stylesheet>
