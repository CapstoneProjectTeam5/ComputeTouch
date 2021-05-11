package computeTouchMeasurements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.*;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class xmlGenerator {	
	public static void singleTap() {
		// TODO Auto-generated method stub
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			//root element
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("TouchCollection");
			doc.appendChild(rootElement);
			
			List<Element> touch = new ArrayList<>();
			touch.add(doc.createElement("Touch"));
			touch.add(doc.createElement("Touch"));
			touch.add(doc.createElement("Touch"));
			//Touch Target
			for(int i = 0 ;i < 3; i++)
				rootElement.appendChild(touch.get(i)); 

			//attribute			
			touch.get(0).setAttribute("TargetY","358");
			touch.get(0).setAttribute("TargetX","137");
			touch.get(0).setAttribute("Type","tap");
			
			touch.get(1).setAttribute("TargetY","358");
			touch.get(1).setAttribute("TargetX","532");
			touch.get(1).setAttribute("Type","tap");
			
			touch.get(2).setAttribute("TargetY","358");
			touch.get(2).setAttribute("TargetX","924");
			touch.get(2).setAttribute("Type","tap");
			
			//Stroke
			List<Element> stroke = new ArrayList<>();
			stroke.add(doc.createElement("Stroke"));
			stroke.add(doc.createElement("Stroke"));
			stroke.add(doc.createElement("Stroke"));

			for(int i =0 ;i<3;i++)
				touch.get(i).appendChild(stroke.get(i));
			
			
			String[] splitedStr = null;
			try {
				
				BufferedReader in = new BufferedReader(new FileReader("./SingleTap.txt"));
				
				String s;
				int k = 0;
				
				while((s = in.readLine())!=null) {
					
					if(s.trim().length()==0) {
						if(k<2)
							k++;
					}
					else {
						splitedStr = null;
						splitedStr = s.split("\s");
						for (int i = 0; i < splitedStr.length; i++) {
							splitedStr[i] = splitedStr[i].trim();
						}
						
						Element point = doc.createElement("Point");
						for (int i = 0; i < splitedStr.length; i++) {
							int idx = splitedStr[i].indexOf("=");
							String value = splitedStr[i].substring(idx+1);
							if(i == 0)
								point.setAttribute("X", value);
							else if(i==1)
								point.setAttribute("Y", value);
							else
								point.setAttribute("T",value);
						}
						stroke.get(k).appendChild(point);

					}
				}
				in.close();
			}catch(IOException e) {
				System.err.println(e);
				System.exit(1);
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			
			FileOutputStream fos = null;
			File file = null;
			try {
				file = new File("./SingleTap.xml");
				fos = new FileOutputStream(file);
				StreamResult result = new StreamResult(fos);
				transformer.transform(source, result);
			}catch(Exception e) {
				return;
			}finally {
				try {
					if(fos!=null)
						fos.close();
				}catch(IOException ignore) {
					
				}
			}

			StreamResult result = new StreamResult(System.out);
			transformer.transform(source, result);
		}catch(ParserConfigurationException pce)
		{
			pce.printStackTrace();
		}catch(TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

	public static void doubleTap(){
		// TODO Auto-generated method stub
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			//root element
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("TouchCollection");
			doc.appendChild(rootElement);
			
			List<Element> touch = new ArrayList<>();
			touch.add(doc.createElement("Touch"));
			touch.add(doc.createElement("Touch"));
			touch.add(doc.createElement("Touch"));
			//Touch Target
			for(int i = 0 ;i < 3; i++)
				rootElement.appendChild(touch.get(i)); 

			//attribute			
			touch.get(0).setAttribute("TargetY","341");
			touch.get(0).setAttribute("TargetX","140");
			touch.get(0).setAttribute("Type","doubletap");
			
			touch.get(1).setAttribute("TargetY","344");
			touch.get(1).setAttribute("TargetX","535");
			touch.get(1).setAttribute("Type","doubletap");
			
			touch.get(2).setAttribute("TargetY","341");
			touch.get(2).setAttribute("TargetX","931");
			touch.get(2).setAttribute("Type","doubletap");
			
			//Stroke
			List<Element> stroke = new ArrayList<>();
			stroke.add(doc.createElement("Stroke"));
			stroke.add(doc.createElement("Stroke"));
			stroke.add(doc.createElement("Stroke"));

			for(int i =0 ;i<3;i++)
				touch.get(i).appendChild(stroke.get(i));
			
			
			String[] splitedStr = null;
			try {
				
				BufferedReader in = new BufferedReader(new FileReader("./DoubleTap.txt"));
				
				String s;
				int k = 0;
				while((s = in.readLine())!=null) {
					
					if(s.trim().length()==0) {
						if(k<4)
							k++;
					}
					else {
						splitedStr = null;
						splitedStr = s.split("\s");
						for (int i = 0; i < splitedStr.length; i++) {
							splitedStr[i] = splitedStr[i].trim();
						}
						
						Element point = doc.createElement("Point");
						for (int i = 0; i < splitedStr.length; i++) {
							int idx = splitedStr[i].indexOf("=");
							String value = splitedStr[i].substring(idx+1);
							if(i == 0)
								point.setAttribute("X", value);
							else if(i==1)
								point.setAttribute("Y", value);
							else
								point.setAttribute("T",value);
						}
						stroke.get(k / 2).appendChild(point);

					}
				}
				in.close();
			}catch(IOException e) {
				System.err.println(e);
				System.exit(1);
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			
			FileOutputStream fos = null;
			File file = null;
			try {
				file = new File("./DoubleTap.xml");
				fos = new FileOutputStream(file);
				StreamResult result = new StreamResult(fos);
				transformer.transform(source, result);
			}catch(Exception e) {
				return;
			}finally {
				try {
					if(fos!=null)
						fos.close();
				}catch(IOException ignore) {
					
				}
			}

			StreamResult result = new StreamResult(System.out);
			transformer.transform(source, result);
		}catch(ParserConfigurationException pce)
		{
			pce.printStackTrace();
		}catch(TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
	
	public static void dragandDrop(){
		// TODO Auto-generated method stub
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			//root element
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("TouchCollection");
			doc.appendChild(rootElement);
			
			List<Element> touch = new ArrayList<>();
			touch.add(doc.createElement("Touch"));
			touch.add(doc.createElement("Touch"));
			touch.add(doc.createElement("Touch"));
			//Touch Target
			for(int i = 0 ;i < 3; i++)
				rootElement.appendChild(touch.get(i)); 

			//attribute			
			touch.get(0).setAttribute("StopTargetY","188");
			touch.get(0).setAttribute("StopTargetX","955");
			touch.get(0).setAttribute("StartTargetY","185");
			touch.get(0).setAttribute("StartTargetX","146");
			touch.get(0).setAttribute("Type","singletouch-draganddrop");
			
			touch.get(1).setAttribute("StopTargetY","341");
			touch.get(1).setAttribute("StopTargetX","97");
			touch.get(1).setAttribute("StartTargetY","338");
			touch.get(1).setAttribute("StartTargetX","888");
			touch.get(1).setAttribute("Type","singletouch-draganddrop");
			
			touch.get(2).setAttribute("StopTargetY","488");
			touch.get(2).setAttribute("StopTargetX","949");
			touch.get(2).setAttribute("StartTargetY","497");
			touch.get(2).setAttribute("StartTargetX","140");
			touch.get(2).setAttribute("Type","singletouch-draganddrop");
			
			//Stroke
			List<Element> stroke = new ArrayList<>();
			stroke.add(doc.createElement("Stroke"));
			stroke.add(doc.createElement("Stroke"));
			stroke.add(doc.createElement("Stroke"));

			for(int i =0 ;i<3;i++)
				touch.get(i).appendChild(stroke.get(i));
			
			
			String[] splitedStr = null;
			try {
				
				BufferedReader in = new BufferedReader(new FileReader("./DragandDrop.txt"));
				
				String s;
				int k = 0;
				while((s = in.readLine())!=null) {
					
					if(s.trim().length()==0) {
						if(k<2)
							k++;
					}
					else {
						splitedStr = null;
						splitedStr = s.split("\s");
						for (int i = 0; i < splitedStr.length; i++) {
							splitedStr[i] = splitedStr[i].trim();
						}
						
						Element point = doc.createElement("Point");
						for (int i = 0; i < splitedStr.length; i++) {
							int idx = splitedStr[i].indexOf("=");
							String value = splitedStr[i].substring(idx+1);
							if(i == 0)
								point.setAttribute("X", value);
							else if(i==1)
								point.setAttribute("Y", value);
							else
								point.setAttribute("T",value);
						}
						stroke.get(k).appendChild(point);

					}
				}
				in.close();
			}catch(IOException e) {
				System.err.println(e);
				System.exit(1);
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			
			FileOutputStream fos = null;
			File file = null;
			try {
				file = new File("./DragandDrop.xml");
				fos = new FileOutputStream(file);
				StreamResult result = new StreamResult(fos);
				transformer.transform(source, result);
			}catch(Exception e) {
				return;
			}finally {
				try {
					if(fos!=null)
						fos.close();
				}catch(IOException ignore) {
					
				}
			}

			StreamResult result = new StreamResult(System.out);
			transformer.transform(source, result);
		}catch(ParserConfigurationException pce)
		{
			pce.printStackTrace();
		}catch(TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
}
