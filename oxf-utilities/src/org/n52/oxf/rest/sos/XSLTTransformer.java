/**********************************************************************************
 Copyright (C) 2007 by 52 North Initiative for Geospatial Open Source Software GmbH

 Contact: Andreas Wytzisk 
 52 North Initiative for Geospatial Open Source Software GmbH
 Martin-Luther-King-Weg 24
 48155 Muenster, Germany
 info@52north.org

 This program is free software; you can redistribute and/or modify it under the
 terms of the GNU General Public License version 2 as published by the Free
 Software Foundation.

 This program is distributed WITHOUT ANY WARRANTY; even without the implied
 WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License along with this 
 program (see gnu-gplv2.txt). If not, write to the Free Software Foundation, Inc., 
 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or visit the Free Software
 Foundation web page, http://www.fsf.org.
 
 Created on: 03.03.2008
 *********************************************************************************/

package org.n52.oxf.rest.sos;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.n52.oxf.util.IOHelper;

public class XSLTTransformer {

    /**
     * @return an XSL transformation of the 'inputText'.
     */
    public static void transform(InputStream xmlInput, InputStream xslInput, OutputStream out) throws TransformerException,
            IOException {

        transform(IOHelper.readText(xmlInput), IOHelper.readText(xslInput), out);
    }

    /**
     * @return an XSL transformation of the 'inputText'.
     */
    public static void transform(File xmlInputFile, File xslFile, OutputStream out) throws TransformerException,
            IOException {

        transform(IOHelper.readText(xmlInputFile), IOHelper.readText(xslFile), out);
    }

    /**
     * @return an XSL transformation of the 'inputText'.
     */
    public static void transform(String xmlInputText, String xslScript, OutputStream out) throws TransformerException {

        Source xmlSource = new StreamSource(new StringReader(xmlInputText));

        Source sheetSource = new StreamSource(new StringReader(xslScript));

        Result outputTarget = new StreamResult(out);

        TransformerFactory factory = TransformerFactory.newInstance();

        Transformer transformer = factory.newTransformer(sheetSource);

        transformer.transform(xmlSource, outputTarget);
    }
}
