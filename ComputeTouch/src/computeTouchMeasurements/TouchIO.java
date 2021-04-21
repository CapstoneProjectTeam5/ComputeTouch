package computeTouchMeasurements;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import computeTouchMeasurements.TouchAction.TouchActionType;
 
public class TouchIO {
    //  Reads an XML file and returns the tasks as a list of TouchActions.
    public static List<TouchAction> ReadFromFile(String fileName) throws ParserConfigurationException, SAXException, IOException  { 
    	List<TouchAction> touchActions = new ArrayList<TouchAction>();
    	
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(fileName);
		
		doc.getDocumentElement().normalize();
		
		Element root = doc.getDocumentElement();
		TouchAction action = new TouchAction();
		Stroke stroke = new Stroke();

		NodeList TouchList = root.getElementsByTagName("Touch");	// <Touch> 태그의 내용 가져옴
		Element TouchE;
		
		for (int i = 0; i < TouchList.getLength(); i++) {
			TouchE = (Element) TouchList.item(i);
			action = new TouchAction();
			action.Type = TouchIO.TextToActionType(TouchE.getAttributes().getNamedItem("Type").getNodeValue());
			
			if (action.Type == TouchActionType.Tap || action.Type == TouchActionType.DoubleTap) {
				int x = Integer.parseInt(TouchE.getAttributes().getNamedItem("TargetX").getNodeValue());
				int y = Integer.parseInt(TouchE.getAttributes().getNamedItem("TargetY").getNodeValue());
				
                action.TargetPoint = new Point(x, y);
			}
            if (action.Type == TouchActionType.SingleTouchDragAndDrop)
            {
            	int startX = Integer.parseInt(TouchE.getAttributes().getNamedItem("StartTargetX").getNodeValue());
				int startY = Integer.parseInt(TouchE.getAttributes().getNamedItem("StartTargetY").getNodeValue());
				action.StartPoints.add(new Point(startX, startY));
				
				
				int stopX = Integer.parseInt(TouchE.getAttributes().getNamedItem("StopTargetX").getNodeValue());
				int stopY = Integer.parseInt(TouchE.getAttributes().getNamedItem("StopTargetY").getNodeValue());               
                action.StopPoints.add(new Point(stopX, stopY));
                
            }
            if (action.Type == TouchActionType.MultiTouchDragAndDrop)
            {
            	int startX = Integer.parseInt(TouchE.getAttributes().getNamedItem("StartTargetX1").getNodeValue());
				int startY = Integer.parseInt(TouchE.getAttributes().getNamedItem("StartTargetY1").getNodeValue());
				action.StartPoints.add(new Point(startX, startY));
				
				int stopX = Integer.parseInt(TouchE.getAttributes().getNamedItem("StopTargetX1").getNodeValue());
				int stopY = Integer.parseInt(TouchE.getAttributes().getNamedItem("StopTargetY1").getNodeValue());                
                action.StopPoints.add(new Point(stopX, stopY));
				
				startX = Integer.parseInt(TouchE.getAttributes().getNamedItem("StartTargetX2").getNodeValue());
				startY = Integer.parseInt(TouchE.getAttributes().getNamedItem("StartTargetY2").getNodeValue());
				action.StartPoints.add(new Point(startX, startY));
				
				stopX = Integer.parseInt(TouchE.getAttributes().getNamedItem("StopTargetX2").getNodeValue());
				stopY = Integer.parseInt(TouchE.getAttributes().getNamedItem("StopTargetY2").getNodeValue());              
                action.StopPoints.add(new Point(stopX, stopY));
            }
            
            // <Stroke>
            NodeList StrokeList = TouchE.getElementsByTagName("Stroke");
            Element StrokeE;
            for (int j = 0; j < StrokeList.getLength(); j++) {
            	StrokeE = (Element) StrokeList.item(j);
            	stroke = new Stroke();
            	
            	// <Point>
            	NodeList PointList = StrokeE.getElementsByTagName("Point");
            	for (int k = 0; k < PointList.getLength(); k++) {
            		Element PointE = (Element) PointList.item(k);
            		int x = Integer.parseInt(PointE.getAttributes().getNamedItem("X").getNodeValue());
            		int y = Integer.parseInt(PointE.getAttributes().getNamedItem("Y").getNodeValue());
            		int t = Integer.parseInt(PointE.getAttributes().getNamedItem("T").getNodeValue());
            		stroke.add(new Point(x, y, t));
            	}
            	action.Strokes.add(stroke);	// </Stroke>
            }           
           touchActions.add(action);	// </Touch>
		}
		return touchActions;
    }
    
    //  Converts task name into task id.
    private static TouchActionType TextToActionType(String text) {
        switch (text) {
            case "tap":
                return TouchActionType.Tap;
            case "doubletap":
                return TouchActionType.DoubleTap;
            case "singletouch-draganddrop":
                return TouchActionType.SingleTouchDragAndDrop;
            case "multitouch-draganddrop":
                return TouchActionType.MultiTouchDragAndDrop;
        }
        return TouchActionType.None;
    }
}