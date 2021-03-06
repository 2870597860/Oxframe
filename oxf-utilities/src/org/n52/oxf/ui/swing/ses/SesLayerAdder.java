/**********************************************************************************
 Copyright (C) 2009
 by 52 North Initiative for Geospatial Open Source Software GmbH

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

 Created on: 06.01.2006
 *********************************************************************************/

package org.n52.oxf.ui.swing.ses;
import sun.audio.*;
import java.awt.Component;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;

import net.opengis.om.x10.ObservationDocument;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;
import org.n52.oxf.context.LayerContext;
import org.n52.oxf.feature.IFeatureStore;
import org.n52.oxf.feature.OXFFeature;
import org.n52.oxf.feature.OXFFeatureAttributeDescriptor;
import org.n52.oxf.feature.OXFFeatureCollection;
import org.n52.oxf.feature.OXFFeatureType;
import org.n52.oxf.layer.FeatureServiceLayer;
import org.n52.oxf.owsCommon.ServiceDescriptor;
import org.n52.oxf.render.IFeatureDataRenderer;
import org.n52.oxf.render.IFeaturePicker;
import org.n52.oxf.serviceAdapters.ParameterContainer;
import org.n52.oxf.serviceAdapters.ses.SESAdapter;
import org.n52.oxf.ui.swing.MapCanvas;
import org.n52.oxf.ui.swing.sos.DuXMLDoc;
import org.n52.oxf.ui.swing.tree.ContentTree;
import org.n52.oxf.ui.swing.util.LayerAdder;
import org.n52.oxf.util.EventName;
import org.n52.oxf.util.OMParser;
import org.n52.oxf.util.OXFEvent;
import org.n52.oxf.util.OXFEventException;
import org.opengis.feature.DataType;
import org.opengis.feature.FeatureAttributeDescriptor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * @author Artur Osmanov <artur.osmanov@uni-muenster.de>, Thomas Everding
 * 
 */
public class SesLayerAdder implements HttpHandler{

	private FeatureServiceLayer	layer	= null;
	private OXFFeatureType		fType	= null;
	private Component			owner	= null;
	private MapCanvas			map		= null;
	private ContentTree			tree	= null;
	private String layerName = "";
	
	private static final String	SERVER_CONTEXT	= "/";
	private static final int PORT = 8083;
	
	public static final String SAMPLING_POINT = "SAMPLING_POINT";
	public static final String X_COORD = "X_COORD";
	public static final String Y_COORD = "Y_COORD";
	public static final String FOI_ID = "FOI_ID";
	public static final String sensorID = "sensorID";//添加的
	public static final String RESULT_VALUE = "RESULT_VALUE";
	public static final String OBSERVED_PROPERTY = "OBSERVED_PROPERTY";
	public static final String SAMPLING_TIME = "SAMPLING_TIME";
	private static final Logger LOGGER = Logger.getLogger(SesLayerAdder.class);
	String level=null;//添加的

	/**
	 * Adds a new SES layer.
	 * 
	 * @param owner owner
	 * @param map map
	 * @param tree tree
	 * @param layerName layer name
	 * @param layerTitle layer title
	 * @param sesAdapter SES adapter
	 * @param serviceDesc service description
	 * @param renderer renderer
	 * @param featureStore feature store
	 * @param paramCont parameter container
	 */
	public void addSesLayer(Component owner, MapCanvas map, ContentTree tree, String layerName, String layerTitle, SESAdapter sesAdapter,
			ServiceDescriptor serviceDesc, IFeatureDataRenderer renderer, IFeatureStore featureStore, ParameterContainer paramCont) {

		this.owner = owner;
		this.map = map;
		this.tree = tree;
		try {
			// make layer name unique:
			while(map.getLayerContext().contains(layerName)){  
				int layerNumber= Integer.parseInt(layerName.substring(layerName.length() - 2, layerName.length())); 
				layerNumber = layerNumber + 1;
				if (layerNumber < 10) {
					this.layerName = layerName.substring(0, layerName.length() - 2) + "0" + layerNumber;
				}
				else {
					this.layerName = layerName.substring(0, layerName.length() - 2) + layerNumber;
				}
			}

			//build new layer
			layer = new FeatureServiceLayer(sesAdapter, renderer, featureStore, (IFeaturePicker) renderer, serviceDesc, paramCont, this.layerName,
					layerTitle, sesAdapter.getResourceOperationName(), true);
			layer.setIDName(layerName); //layeraName是Notifications00
			
			String fTypeName = "SESNotifications";
			
			FeatureAttributeDescriptor positionAttribute = new OXFFeatureAttributeDescriptor(SAMPLING_POINT, DataType.OBJECT, Point.class, 1, 1,
					"This attribute stores the geometrical position of the notification.");

			FeatureAttributeDescriptor resultValueAttribute = new OXFFeatureAttributeDescriptor(RESULT_VALUE, DataType.OBJECT, Object.class, 1, 1,
					"This attribute stores the value of the notification.");
			
			FeatureAttributeDescriptor resultTypeAttribute = new OXFFeatureAttributeDescriptor(OBSERVED_PROPERTY, DataType.OBJECT, String.class, 1, 1,
			"This attribute stores the value type of the notification.");
			
			FeatureAttributeDescriptor foiIdAttribute = new OXFFeatureAttributeDescriptor(FOI_ID, DataType.OBJECT, Object.class, 1, 1,
			"This attribute stores the feature of interest id of the notification.");
			
			FeatureAttributeDescriptor timeAttribute = new OXFFeatureAttributeDescriptor(SAMPLING_TIME, DataType.OBJECT, Object.class, 1, 1,
			"This attribute stores the location name of the notification.");
			
			List<FeatureAttributeDescriptor> attributeList = new ArrayList<FeatureAttributeDescriptor>();
			attributeList.add(positionAttribute);
			attributeList.add(resultValueAttribute);
			attributeList.add(resultTypeAttribute);
			attributeList.add(foiIdAttribute);
			attributeList.add(timeAttribute);
			fType = new OXFFeatureType(fTypeName, attributeList); //fType是OXFFeatureType类的对象		                                                      

			OXFFeatureCollection fCollection = new OXFFeatureCollection("id_123", null);

			// build new featureCollection
			layer.setFeatureCollection(fCollection);
			layer.setSelectedFeatures(fCollection.toSet());

			LayerAdder.addLayer(map, tree, layer);
			
			//handling of incoming notifications
			this.startHTTPServer();
		}
		catch (OXFException exc) {
			exc.printStackTrace();
		}
		catch (OXFEventException exc) {
			exc.printStackTrace();
		}
	}


	private void startHTTPServer() {
		try {
			//create HTTP server
			HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
			
			//initialize and start server
			server.createContext(SERVER_CONTEXT, this);
			server.setExecutor(null);
			server.start();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param response
	 */
	private void parseNotification(String response){
		try {
			level=SesGUIController.a;
			// get the message
			if (level.equals("Filter Level 1")||level.equals("Filter Level 2")){
				
				int from = response.indexOf("<wsnt:Message>") + 14;
				int to = response.indexOf("</wsnt:Message>");
			 
				String message = response.substring(from, to);
				ObservationDocument doc = ObservationDocument.Factory.parse(message);
				LOGGER.info("下面是通知消息："+doc);
				//System.out.println("===================下面是通知消息==================");
				//System.out.println(doc);    //-------------------------------doc是响应中Message部分
				String a=doc.toString();
				System.out.println("aaaaa=========================================");
				System.out.println(a);
				ParameterContainer paramCon = OMParser.parseOM(doc);  //进行到这里列出了返回通知的参数列表
				refreshNotificationsLayer(paramCon);
			}
			else if(level.equals("Filter Level 3")){
				
				int from = response.indexOf("<om:Observation");
				int to = response.indexOf("</target>");
				String message = response.substring(from, to).trim();
				System.out.println("============"+message);
				ObservationDocument doc = ObservationDocument.Factory.parse(message);
				LOGGER.info("下面是通知消息："+doc);
				//System.out.println("===================下面是通知消息==================");
				//System.out.println(doc);    //-------------------------------doc是响应中Message部分
				String a=doc.toString();
				System.out.println("aaaaa=========================================");
				System.out.println(a);
				ParameterContainer paramCon = OMParser.parseOM(doc);  //进行到这里列出了返回通知的参数列表
				refreshNotificationsLayer(paramCon);
			}
				
			 
		 

		} catch (XmlException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 
	 * @param paramCon
	 */
	private void refreshNotificationsLayer(ParameterContainer paramCon){
		OXFFeatureCollection set = layer.getFeatureCollection();
         
		Random random = new Random();
		
		// Create and add the new point
		int id = random.nextInt(1000000);
		OXFFeature feature = new OXFFeature("id_" + id, fType);
		
		if(paramCon.containsParameterShellWithServiceSidedName(X_COORD)&&
				paramCon.containsParameterShellWithServiceSidedName(Y_COORD)){
			Double x = Double.parseDouble(paramCon.getParameterShellWithCommonName(Y_COORD).getSpecifiedValue().toString());
			Double y = Double.parseDouble(paramCon.getParameterShellWithCommonName(X_COORD).getSpecifiedValue().toString());
			
			Point point = new Point();
			point.setLocation(x, y);
			feature.setAttribute(SAMPLING_POINT, point);
			com.vividsolutions.jts.geom.Point point2 = new GeometryFactory().createPoint(new Coordinate(x, y));
			point2.setSRID(4326);// WGS84
			feature.setGeometry(point2);
		}
		if(paramCon.containsParameterShellWithCommonName(FOI_ID)){
			String locationName = (String)paramCon.getParameterShellWithCommonName(FOI_ID).getSpecifiedValue();
			feature.setAttribute(FOI_ID, locationName);
			 
		}
		//*下面是添加的
		if(paramCon.containsParameterShellWithCommonName(sensorID)){
			String locationName = (String)paramCon.getParameterShellWithCommonName(sensorID).getSpecifiedValue();
			feature.setAttribute(sensorID, locationName);
			 
		}
		//上面是添加的
		if(paramCon.containsParameterShellWithCommonName(RESULT_VALUE)){
			String resultValue = (String)paramCon.getParameterShellWithCommonName(RESULT_VALUE).getSpecifiedValue();
			feature.setAttribute(RESULT_VALUE, resultValue);
		}
		if(paramCon.containsParameterShellWithCommonName(OBSERVED_PROPERTY)){
			String resultType = (String)paramCon.getParameterShellWithCommonName(OBSERVED_PROPERTY).getSpecifiedValue();
			feature.setAttribute(OBSERVED_PROPERTY, resultType);
		}
		if(paramCon.containsParameterShellWithCommonName(SAMPLING_TIME)){
			String time = (String)paramCon.getParameterShellWithCommonName(SAMPLING_TIME).getSpecifiedValue();
			feature.setAttribute(SAMPLING_TIME, time);
		}
		set.add(feature);
		//播放报警声音
		 SoundHelper.PlaySound();
		//弹出窗口显示传感器异常
		JOptionPane.showMessageDialog(null,(String)paramCon.getParameterShellWithCommonName(FOI_ID).getSpecifiedValue()+"Fire Alarm","报警消息",JOptionPane.WARNING_MESSAGE);
		//播放报警声音
		 //SoundHelper.PlaySound();


		// shift up node in tree
		String serviceTitle = layer.getLayerSourceTitle();
		String serviceType = layer.getLayerSourceType();
		DefaultMutableTreeNode node = tree.getLayerStorageNode(serviceTitle, serviceType);
		tree.shiftUpNode(node);

		// show new point on map在地图上显示一个新的点
		layer.getBBox();   //layer是FeatureServiceLayer类的一个对象 
		LayerContext context = map.getLayerContext();
		try {
			context.shiftUp(layer);
			
			context.eventCaught(new OXFEvent(layer, EventName.LAYER_REAL_TIME_REFRESH, null));
		}
		catch (OXFEventException e) {
			e.printStackTrace();
		}
		catch (OXFException e) {
			e.printStackTrace();
		}
		//弹出窗口显示传感器异常
	JOptionPane.showMessageDialog(null,(String)paramCon.getParameterShellWithCommonName(FOI_ID).getSpecifiedValue()+"请注意，监测到火灾！","报警消息",JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void refreshSesNotificationsLayer()
	{

		OXFFeatureCollection set = layer.getFeatureCollection();
         
		Random random = new Random();
		
		// Create and add the new point
		int id = random.nextInt(1000000);
		OXFFeature feature = new OXFFeature("id_" + id, fType);
		
		
		    String position = DuSesXmlDoc.strPosition1;
		    LOGGER.info("位置信息"+position);
		    //System.out.println("==========="+position);
		    int a = position.indexOf(" ");
		    String sx = position.substring(a);
		    String sy = position.substring(0,a);
		    Double dx = Double.parseDouble(sx);
		    Double dy = Double.parseDouble(sy);
		    System.out.println(dx+","+dy);
// 			Double x = 121.46635521659634;		 //31.232777938837923 121.46635521659634
// 			Double y = 31.232777938837923;
			
			Point point = new Point();
			point.setLocation(dx, dy);
			feature.setAttribute(SAMPLING_POINT, point);
			com.vividsolutions.jts.geom.Point point2 = new GeometryFactory().createPoint(new Coordinate(dx, dy));
			point2.setSRID(4326);// WGS84
			feature.setGeometry(point2);
		 
		 
			String locationName =  DuSesXmlDoc.strProcedure1+"/"+DuSesXmlDoc.strProcedure2;  
			feature.setAttribute(FOI_ID, locationName);  
		 	 
	 
		//*下面是添加的
		 
			//String locationName1 = DuSesXmlDoc.strGmlName1;  //-----------------传感器的名字  
			//feature.setAttribute(sensorID, locationName1); 
			
		 
		//上面是添加的
		 
			String resultValue1 =  DuSesXmlDoc.strResult1+"/"+DuSesXmlDoc.strResult2;   //----------------传感器结果
			feature.setAttribute(RESULT_VALUE, resultValue1);
		 
		 
			String resultType1 =DuSesXmlDoc.strObservedProperty1+"/"+DuSesXmlDoc.strObservedProperty2; //--------------观测属性
			feature.setAttribute(OBSERVED_PROPERTY, resultType1);
		 
			String time =DuSesXmlDoc.strbeginPosion; //   
			feature.setAttribute(SAMPLING_TIME, time);  
		 
		set.add(feature);
		//播放报警声音
		 SoundHelper.PlaySound();
		//弹出窗口显示传感器异常
		//JOptionPane.showMessageDialog(null,(String)paramCon.getParameterShellWithCommonName(FOI_ID).getSpecifiedValue()+"传感器显示异常事件","报警消息",JOptionPane.WARNING_MESSAGE);
		//播放报警声音
		 //SoundHelper.PlaySound();


		// shift up node in tree
		String serviceTitle = layer.getLayerSourceTitle();
		String serviceType = layer.getLayerSourceType();
		DefaultMutableTreeNode node = tree.getLayerStorageNode(serviceTitle, serviceType);
		tree.shiftUpNode(node);

		// show new point on map在地图上显示一个新的点
		layer.getBBox();   //layer是FeatureServiceLayer类的一个对象 
		LayerContext context = map.getLayerContext();
		try {
			context.shiftUp(layer);
			
			context.eventCaught(new OXFEvent(layer, EventName.LAYER_REAL_TIME_REFRESH, null));
		}
		catch (OXFEventException e) {
			e.printStackTrace();
		}
		catch (OXFException e) {
			e.printStackTrace();
		}
		//弹出窗口显示传感器异常
	JOptionPane.showMessageDialog(null,"请注意！监测到发生"+DuSesXmlDoc.strNotificationValue,"报警消息",JOptionPane.INFORMATION_MESSAGE);
	}
	
	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		//get request
		InputStream in = httpExchange.getRequestBody();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		
		//read request
		StringBuilder sb = new StringBuilder();
		String line;
		
		while((line = reader.readLine()) != null) {
			sb.append(line);
		}
		
		//parse request
		this.parseNotification(sb.toString());
		
		//build response
		line = "";
		httpExchange.sendResponseHeaders(200, line.length()); //HTTP 200 = OK
		
		//send response and close
		OutputStream out = httpExchange.getResponseBody();
		out.write(line.getBytes());
		out.flush();
		out.close();
	}
	
	
	public static void main(String[] args) {
		new SesLayerAdder().startHTTPServer();
	}
}