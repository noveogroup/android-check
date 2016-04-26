<?xml version="1.0" encoding="UTF-8" standalone="no"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:output indent="yes" method="text" />
    <xsl:decimal-format decimal-separator="." grouping-separator="," />

    <xsl:template match="BugCollection">
    Findbugs: <xsl:apply-templates mode="summary" select="." />
        Files: <xsl:apply-templates select="FindBugsSummary/FileStats[@path and @bugCount]" />

    </xsl:template>

    <xsl:template match="FileStats">
        <xsl:variable name="path" select="@path" />
            - <xsl:value-of select="$path" />
                Errors: <xsl:value-of select="@bugCount" />
            <xsl:for-each select="//BugInstance[SourceLine/@sourcepath = $path]">
                <xsl:sort data-type="number" order="ascending" select="SourceLine/@start" />
                    Error Description: <xsl:value-of select="@type" />
                        Line: <xsl:value-of select="LongMessage/text()" />
                        <xsl:value-of select="SourceLine/@start" /> - <xsl:value-of select="SourceLine/@end" />
            </xsl:for-each>
    </xsl:template>

    <xsl:template match="BugCollection" mode="summary">
        <xsl:variable name="fileCount" select="count(FindBugsSummary/FileStats)" />
        <xsl:variable name="errorCount" select="count(BugInstance)" />
        Summary:
            Files: <xsl:value-of select="$fileCount" />
            Errors: <xsl:value-of select="$errorCount" />
    </xsl:template>

</xsl:stylesheet>
