<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns="http://earth.google.com/kml/2.1"
    xmlns:om="http://www.opengis.net/om/0.0" 
    xmlns:gml="http://www.opengis.net/gml" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:sa="http://www.opengis.net/sampling/0.0" 
    gml:id="oc6905" 
    xsi:schemaLocation="http://www.opengis.net/om/0.0 http://mars.uni-muenster.de/swerep1.0/swe/trunk/om/0.0.0/observationSpecializations.xsd">
    
    <xsl:output version="1.0" method="xml" encoding="UTF-8" />
    
    <xsl:template match="/">
        <kml>
            <Document>
                <open>0</open>
                
                <!-- ~~~~~~~~~~~~~~~~~~~~~~~~ O&M 0.0 ~~~~~~~~~~~~~~~~~~~~~~~ -->
                
                <!-- ~~~~~~~~~~~~~~~~~~~~~~~~ Generic Observation ~~~~~~~~~~~~~~~~~~~~~~~ -->
                
                <xsl:for-each select="//gml:featureMember">
                    <Style>
                        <xsl:attribute name="id">FOI_STYLE_<xsl:value-of select="sa:Station/@gml:id" /></xsl:attribute>
                        <BalloonStyle>
                            <bgColor>ffffffff</bgColor>
                            <text>
                                <![CDATA[
<b><font color="#CC0000" size="+3">$[name]</font></b>
<br/><br/>
<font face="Courier">$[description]</font>
<br/><br/>
<img src="@DIAGRAM_URL@/" width="500" height="500" />
]]>
                            </text>
                        </BalloonStyle>
                    </Style>
                </xsl:for-each>
                
                <xsl:for-each select="//gml:featureMember">
                    <Placemark>
                        <name><xsl:value-of select="sa:Station/gml:name"/></name>
                        <description>Feature Of Interest: '<xsl:value-of select="sa:Station/gml:name" />' (id := '<xsl:value-of select="sa:Station/@gml:id" />')</description>
                        <styleUrl>#FOI_STYLE_<xsl:value-of select="sa:Station/@gml:id" /></styleUrl>
                        <Point>
                            <coordinates>
                                <xsl:value-of select="substring-before(sa:Station/sa:position/gml:Point/gml:pos, string(' '))"/>,<xsl:value-of select="substring-after(sa:Station/sa:position/gml:Point/gml:pos, string(' '))"/>
                            </coordinates>
                        </Point>
                    </Placemark>
                </xsl:for-each>
                
                <!-- ~~~~~~~~~~~~~~~~~~~~~~~~ Measurement ~~~~~~~~~~~~~~~~~~~~~~~ -->
                
                <xsl:for-each select="//om:Measurement">
                    <Style>
                        <xsl:attribute name="id">MEASUREMENT_STYLE_<xsl:value-of select="@gml:id" /></xsl:attribute>
                        <BalloonStyle>
                            <bgColor>ffffffff</bgColor>
                            <text>
<![CDATA[
<b><font color="#CC0000" size="+3">$[name]</font></b>
<br/><br/>
<font face="Courier">$[description]</font>
<br/><br/>
Timestamp: ]]><xsl:value-of select="om:samplingTime/gml:TimeInstant/gml:timePosition"/>
<![CDATA[
<br/><br/>
Sensor ID: ]]><xsl:value-of select="om:procedure/@xlink:href"/>
<![CDATA[
<br/><br/>
Observed Property: ]]><xsl:value-of select="om:observedProperty/@xlink:href"/>
                            </text>
                        </BalloonStyle>
                    </Style>
                </xsl:for-each>
                
                <xsl:for-each select="//om:Measurement">
                    <Placemark>
                    <name><xsl:value-of select="@gml:id"/></name>
                        <description>Measurement '<xsl:value-of select="@gml:id"/>' measured at FOI: '<xsl:value-of select="om:featureOfInterest/sa:Station/gml:name" />'</description>
                        <styleUrl>#MEASUREMENT_STYLE_<xsl:value-of select="@gml:id" /></styleUrl>
                        <Point>
                            <coordinates>
                                <xsl:value-of select="substring-before(om:featureOfInterest/sa:Station/sa:position/gml:Point/gml:pos, string(' '))"/>,<xsl:value-of select="substring-after(om:featureOfInterest/sa:Station/sa:position/gml:Point/gml:pos, string(' '))"/>
                            </coordinates>
                        </Point>
                    </Placemark>
                </xsl:for-each>
                
            </Document>
        </kml>
    </xsl:template>

</xsl:stylesheet>
