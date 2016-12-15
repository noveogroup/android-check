<?xml version="1.0" encoding="UTF-8" standalone="no"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:output indent="yes" method="html" />
    <xsl:decimal-format decimal-separator="." grouping-separator="," />

    <xsl:key name="files" match="file" use="@name" />

    <xsl:template match="pmd">
        <html>
            <head>
                <title>PMD Audit</title>
                <style type="text/css">
                    body {
                        margin-left: 10;
                        margin-right: 10;
                        font:normal 80% arial,helvetica,sanserif;
                        background-color:#ffffff;
                        color:#000000;
                    }
                    .a td { background: #efefef }
                    .b td { background: #ffffff }
                    th, td {
                        text-align: left;
                        vertical-align: top;
                    }
                    th {
                        font-weight:bold;
                        background: #ccc;
                        color: black;
                    }
                    table, th, td {
                        font-size:100%;
                        border: none
                    }
                    table.log tr td, tr th { }
                    h3 {
                        font-size:100%;
                        font-weight:bold;
                        background: #525D76;
                        color: white;
                        text-decoration: none;
                        padding: 5px;
                        margin-right: 2px;
                        margin-left: 2px;
                        margin-bottom: 0;
                    }
                </style>
            </head>
            <body>
                <hr align="left" size="1" width="100%" />

                <xsl:apply-templates mode="summary" select="." />

                <hr align="left" size="1" width="100%" />

                <xsl:apply-templates mode="filelist" select="." />

                <hr align="left" size="1" width="100%" />

                <xsl:apply-templates
                    select="file[@name and generate-id(.) = generate-id(key('files', @name))]" />

                <hr align="left" size="1" width="100%" />
            </body>
        </html>
    </xsl:template>

    <xsl:template match="pmd" mode="filelist">
        <h3>Files</h3>
        <table border="0" cellpadding="5" cellspacing="2" class="log" width="100%">
            <tr>
                <th>Name</th>
                <th>Violations</th>
            </tr>
            <xsl:for-each
                select="file[@name and generate-id(.) = generate-id(key('files', @name))]">
                <xsl:sort data-type="number" order="descending"
                    select="count(key('files', @name)/violation)" />
                <xsl:variable name="violationCount" select="count(violation)" />
                <tr>
                    <xsl:call-template name="alternated-row" />
                    <td>
                        <a href="#f-{@name}">
                            <xsl:value-of select="@name" />
                        </a>
                    </td>
                    <td>
                        <xsl:value-of select="$violationCount" />
                    </td>
                </tr>
            </xsl:for-each>
        </table>
    </xsl:template>

    <xsl:template match="file">
        <a name="f-{@name}" />
        <h3>File <xsl:value-of select="@name" /></h3>
        <table border="0" cellpadding="5" cellspacing="2" class="log" width="100%">
            <tr>
                <th>Violation Description</th>
                <th>Location</th>
            </tr>
            <xsl:for-each select="key('files', @name)/violation">
                <xsl:sort data-type="number" order="ascending" select="@beginline" />
                <tr>
                    <xsl:call-template name="alternated-row" />
                    <td>
                        <a>
                            <xsl:attribute name="href">
                                <xsl:value-of select="@externalInfoUrl" />
                            </xsl:attribute>
                            <xsl:value-of select="node()" />
                        </a>
                    </td>
                    <td>
                        <xsl:value-of select="@beginline" />:<xsl:value-of select="@begincolumn" />
                        -
                        <xsl:value-of select="@endline" />:<xsl:value-of select="@endcolumn" />
                    </td>
                </tr>
            </xsl:for-each>
        </table>
    </xsl:template>

    <xsl:template match="pmd" mode="summary">
        <h3>Summary</h3>
        <xsl:variable name="fileCount"
            select="count(file[@name and generate-id(.) = generate-id(key('files', @name))])" />
        <xsl:variable name="violationCount" select="count(file/violation)" />
        <table border="0" cellpadding="5" cellspacing="2" class="log" width="100%">
            <tr>
                <th>Files</th>
                <th>Violations</th>
            </tr>
            <tr>
                <xsl:call-template name="alternated-row" />
                <td>
                    <xsl:value-of select="$fileCount" />
                </td>
                <td>
                    <xsl:value-of select="$violationCount" />
                </td>
            </tr>
        </table>
    </xsl:template>

    <xsl:template name="alternated-row">
        <xsl:attribute name="class">
            <xsl:if test="position() mod 2 = 1">a</xsl:if>
            <xsl:if test="position() mod 2 = 0">b</xsl:if>
        </xsl:attribute>
    </xsl:template>

</xsl:stylesheet>
