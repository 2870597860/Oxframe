/*
 * Copyright (C) 2011
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 * 
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 * 
 * This program is free software; you can redistribute and/or modify it under 
 * the terms of the GNU General Public License version 2 as published by the 
 * Free Software Foundation.
 * 
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 * 
 * Author: Arne Broering
 * Created: 06.09.2011
 */
package org.n52.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * This class provides functionality for setting up loggers.
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class Log {
    
    private static Properties props = null;

    /**
     * @return a ready to use {@link java.util.logging.Logger}.
     */
    public static Logger setUpLogger(Class clazz)
    {
    	return setUpLogger(clazz.getName());
    }
    
    /**
     * @return a ready to use {@link java.util.logging.Logger}.
     */
    public static Logger setUpLogger(String className)
    {
        try {
        	Logger logger = Logger.getLogger(className);
        	
            // load properties for storing log messages
            if (props == null) {
                props = new Properties();
                props.load(Log.class.getResourceAsStream("/logging.properties"));
            }

            // setup logging:
            FileHandler fhTxt = new FileHandler(props.getProperty("log.file.txt"));
            fhTxt.setFormatter(new SimpleFormatter());
            logger.addHandler(fhTxt);
            
            if (props.getProperty("log.file.html") != null) {
            	FileHandler fhHtml = new FileHandler(props.getProperty("log.file.html"));
            	fhHtml.setFormatter(new SimpleFormatter());
            	logger.addHandler(fhHtml);
            }
            
            return logger;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

// This custom formatter formats parts of a log record to a single line
class MyHtmlFormatter extends Formatter {
    // This method is called for every log records
    public String format(LogRecord rec)
    {
        StringBuffer buf = new StringBuffer(1000);
        // Bold any levels >= WARNING
        buf.append("<tr>");
        buf.append("<td>");

        if (rec.getLevel().intValue() >= Level.WARNING.intValue()) {
            buf.append("<b>");
            buf.append(rec.getLevel());
            buf.append("</b>");
        } else {
            buf.append(rec.getLevel());
        }
        buf.append("</td>");
        buf.append("<td>");
        buf.append(calcDate(rec.getMillis()));
        buf.append(' ');
        buf.append(formatMessage(rec));
        buf.append('\n');
        buf.append("<td>");
        buf.append("</tr>\n");
        return buf.toString();
    }

    private String calcDate(long millisecs)
    {
        SimpleDateFormat date_format = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date resultdate = new Date(millisecs);
        return date_format.format(resultdate);
    }

    // This method is called just after the handler using this
    // formatter is created
    public String getHead(Handler h)
    {
        return "<HTML>\n<HEAD>\n" + (new Date()) + "\n</HEAD>\n<BODY>\n<PRE>\n" + "<table border>\n  " + "<tr><th>Time</th><th>Log Message</th></tr>\n";
    }

    // This method is called just after the handler using this
    // formatter is closed
    public String getTail(Handler h)
    {
        return "</table>\n  </PRE></BODY>\n</HTML>\n";
    }
}
