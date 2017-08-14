<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
        <head>
            <script category="text/javascript" src="https://www.gstatic.com/charts/loader.js">
            </script>
            <script category="text/javascript">
                google.charts.load('current', { packages: ["orgchart"] });
                    google.charts.setOnLoadCallback(drawChart);

                    function drawChart() {
                        var data = new google.visualization.DataTable();
                        data.addColumn('string', 'Title');
                        data.addColumn('string', 'Manager');
                        data.addColumn('string', 'Description');

                        var rows = [];
                        <xsl:apply-templates/>
                        data.addRows(rows);

                        var chart = new google.visualization.OrgChart(document.getElementById('chart_div'));
                        chart.draw(data, { allowHtml: true });
                    }
            </script>
        </head>
        <body style="background-color:#01190C;">
            <div id="chart_div">
            </div>
        </body>
    </xsl:template>
    <xsl:template match="TaskGroup">
        rows.push(['<xsl:value-of select="@Name" />','<xsl:value-of select="parent::*/@Name" />','']);
        <xsl:apply-templates/>
    </xsl:template>
    <xsl:template match="Task">
        rows.push(['<xsl:value-of select="@Name" />','<xsl:value-of select="parent::*/@Name" />','<xsl:value-of select="@Description" />']);
    </xsl:template>

</xsl:stylesheet>