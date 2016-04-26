<?xml version="1.0" encoding="UTF-8" standalone="no"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:output indent="yes" method="text" />
    <xsl:decimal-format decimal-separator="." grouping-separator="," />

    <xsl:key name="files" match="file" use="@name" />

    <xsl:template match="checkstyle">
    Checkstyle: <xsl:apply-templates mode="summary" select="." />
        Files: <xsl:apply-templates select="file[@name and generate-id(.) = generate-id(key('files', @name))]" />

    </xsl:template>

    <xsl:template match="file">
        <xsl:variable name="errorCount" select="count(error)" />
            - <xsl:value-of select="@name" />
                Errors: <xsl:value-of select="$errorCount" />
                <xsl:for-each select="key('files', @name)/error">
                    <xsl:sort data-type="number" order="ascending" select="@line" />
                    Error Description: <xsl:value-of select="@message" />
                        Line: <xsl:value-of select="@line" />
                </xsl:for-each>
    </xsl:template>

    <xsl:template match="checkstyle" mode="summary">
        <xsl:variable name="fileCount" select="count(file[@name and generate-id(.) = generate-id(key('files', @name))])" />
        <xsl:variable name="errorCount" select="count(file/error)" />
        Summary:
            Files: <xsl:value-of select="$fileCount" />
            Errors: <xsl:value-of select="$errorCount" />
    </xsl:template>

</xsl:stylesheet>
