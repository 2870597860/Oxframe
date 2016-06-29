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
 Software Foundation’s web page, http://www.fsf.org.

 Created on: 01.03.2008
 ***************************************************************/
package org.n52.oxf.rest;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class RestWebService extends HttpServlet {

    /**
     * 
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        OutputStream out = response.getOutputStream();
        response.setContentType("text/plain");
        out.write(new String("POST not yet supported").getBytes());
        out.flush();
        out.close();
    }

    /**
     * 
     */
    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        OutputStream out = response.getOutputStream();
        response.setContentType("text/plain");
        out.write(new String("PUT not yet supported").getBytes());
        out.flush();
        out.close();
    }

    /**
     * 
     */
    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        OutputStream out = response.getOutputStream();
        response.setContentType("text/plain");
        out.write(new String("DELETE not yet supported").getBytes());
        out.flush();
        out.close();
    }

    /**
     * Supporting Method.
     * 
     * @return the resources behind the "servletPath" of a request as a String (without the last '/').<br>
     *         E.g.:
     * 
     * <pre>
     *         http://myHost.com/servletPath/res1/res2/ ==- 'res1/res2'
     *         http://myHost.com/servletPath/res1       ==- 'res1'
     *         http://myHost.com/servletPath/           ==- ''
     *         http://myHost.com/servletPath            ==- ''
     * </pre>
     */
    protected String getResourcesString(String requestURL, String servletPath) {

        // 'i' is the index of the first character after the "/" behind the "servletPath".
        int i = requestURL.indexOf(servletPath) + servletPath.length() + 1;

        if (i < requestURL.length()) {
            String res = requestURL.substring(i);
            if (res.endsWith("/")) {
                res = res.substring(0, res.length() - 1);
            }
            return res;
        }
        
        return "";
        
    }

    /**
     * Supporting Method.
     * 
     * @return the resources behind the "servletPath" of a request as an array of Strings. <br>
     *         E.g.:
     * 
     * <pre>
     *         http://myHost.com/servletPath/res1/res2/ ==- String[] {'res1', 'res2'}
     *         http://myHost.com/servletPath/res1       ==- String[] {'res1'}
     *         http://myHost.com/servletPath/           ==- String[] {}
     *         http://myHost.com/servletPath            ==- String[] {}
     * </pre>
     * 
     */
    protected String[] getResourcesStringArray(String requestURL, String servletPath) {
        String resourcesString = getResourcesString(requestURL, servletPath);

        if (resourcesString.length() == 0) {
            return new String[] {};
        }
        else if (resourcesString.contains("/")) {
            return resourcesString.split("/");
        }
        else {
            return new String[] {resourcesString};
        }
    }

    /**
     * Renders the HTML resource view.
     */
    protected String renderResourcesHtml(String requestURL,
                                         String servletPath,
                                         String resourceType,
                                         String[] availableResources) {
        StringBuffer sb = new StringBuffer("");

        // Render the page header
        sb.append("<html>\r\n");
        sb.append("<head>\r\n");
        sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" />\r\n");
        sb.append("<title>");
        sb.append("Resource Listing for: /" + getResourcesString(requestURL, servletPath));
        sb.append("</title>\r\n");
        sb.append("<STYLE><!--");
        sb.append(ResourceViewCSS.TOMCAT_CSS);
        sb.append("--></STYLE> ");
        sb.append("</head>\r\n");
        sb.append("<body>");
        sb.append("<h1>");
        sb.append("Resource Listing for: /" + getResourcesString(requestURL, servletPath));
        sb.append("</h1>");
        sb.append("<HR size=\"1\" noshade=\"noshade\">");
        sb.append("<table width=\"100%\" cellspacing=\"0\"" + " cellpadding=\"5\" align=\"center\">\r\n");

        // Render the column headings
        sb.append("<tr>\r\n");
        sb.append("<td align=\"left\"><font size=\"+1\"><strong>");
        sb.append(resourceType);
        sb.append("</strong></font></td>\r\n");
        sb.append("</tr>");

        // Render the resources
        boolean shade = true;

        for (String resource : availableResources) {
            sb.append("<tr");
            if (shade)
                sb.append(" bgcolor=\"#eeeeee\"");
            sb.append(">\r\n");
            shade = !shade;

            sb.append("<td align=\"left\">&nbsp;&nbsp;\r\n");
            sb.append("<a href=\"");
            if (requestURL.endsWith("/"))
                sb.append(requestURL + resource);
            else
                sb.append(requestURL + "/" + resource);
            sb.append("\"><tt>");
            sb.append(resource);
            sb.append("</tt></a></td>\r\n");
            sb.append("</tr>\r\n");
        }

        // Render the page footer
        sb.append("</table>\r\n");
        sb.append("<HR size=\"1\" noshade=\"noshade\">");
        sb.append("<h3>52°North&nbsp;&nbsp;-&nbsp;&nbsp;RESTful Web Services</h3>");
        sb.append("</body>\r\n");
        sb.append("</html>\r\n");

        return sb.toString();
    }

}
