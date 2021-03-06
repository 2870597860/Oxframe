/***************************************************************
 Copyright (C) 2007 by 52 North Initiative for Geospatial Open Source Software GmbH

 Contact: Andreas Wytzisk 
 52 North Initiative for Geospatial Open Source Software GmbH
 Martin-Luther-King-Weg 24
 48155 Muenster, Germany
 info@52north.org

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 version 2 as published by the Free Software Foundation.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; even without the implied WARRANTY OF
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program (see gnu-gpl v2.txt). If not, write to
 the Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 Boston, MA 02111-1307, USA or visit the Free
 Software Foundationís web page, http://www.fsf.org.

 Created on: 01.03.2008
 ***************************************************************/
package org.n52.oxf.rest;


public class ResourceViewCSS {

    private static final String HEADING_BG_COLOR = "#E1FFE1";
    private static final String HEADING_FONT_COLOR = "black";
    
    public static final String TOMCAT_CSS =
        "H1 {font-family:Tahoma,Arial,sans-serif;color:" + HEADING_FONT_COLOR + ";background-color:" + HEADING_BG_COLOR + ";font-size:22px;} " +
        "H2 {font-family:Tahoma,Arial,sans-serif;color:" + HEADING_FONT_COLOR + ";background-color:" + HEADING_BG_COLOR + ";font-size:16px;} " +
        "H3 {font-family:Tahoma,Arial,sans-serif;color:" + HEADING_FONT_COLOR + ";background-color:" + HEADING_BG_COLOR + ";font-size:14px;} " +
        "BODY {font-family:Tahoma,Arial,sans-serif;color:black;background-color:white;} " +
        "B {font-family:Tahoma,Arial,sans-serif;color:" + HEADING_FONT_COLOR + ";background-color:" + HEADING_BG_COLOR + ";} " +
        "P {font-family:Tahoma,Arial,sans-serif;background:white;color:black;font-size:12px;}" +
        "A {color : black;}" +
        "A.name {color : black;}" +
        "HR {color : #525D76;}";


}

