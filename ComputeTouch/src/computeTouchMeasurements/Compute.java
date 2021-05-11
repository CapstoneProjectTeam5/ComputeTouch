package computeTouchMeasurements;

import java.io.*;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

class Compute {
        
        private enum MeasurementType {       
            None, All, Average, BestPerformance
        }
        
        public Compute() throws ParserConfigurationException, SAXException {  
        	xmlGenerator.singleTap();
        	xmlGenerator.doubleTap();
        	xmlGenerator.dragandDrop();
            this.ComputeTapData("singleTap11111111.txt", MeasurementType.All);
            this.ComputeDoubleTapData("doubleTap11111111111.txt", MeasurementType.BestPerformance);
            this.ComputeSingleTouchDragAndDropData("dragandDrop111111111.txt", MeasurementType.BestPerformance);
        }
       
        private final void ComputeSingleTouchDragAndDropData(String outputFileName, MeasurementType measurementType) throws ParserConfigurationException, SAXException {
            try {
            	FileOutputStream fos = new FileOutputStream(new File(outputFileName));
        		OutputStreamWriter osw = new OutputStreamWriter(fos);
        		BufferedWriter bw = new BufferedWriter(osw);
        		
            	bw.write("sgldragdropTime,slgdragdropAccuracy1,sgldragdropAccuracy2,sgldragdropAccuracy,sgldragdropPathAccuracy");
            	bw.newLine();
            	
        		List<TouchAction> actions = TouchIO.ReadFromFile("./DragandDrop.xml");
        		if (measurementType == MeasurementType.All) {
        			for (int i = 0; i < actions.size(); i++) {
        				String s = String.format("%.0f,%.2f,%.2f,%.2f,%.4f\n",
        						actions.get(i).SingleTouchDragAndDropTime(), actions.get(i).SingleTouchDragAndDropAccuracy1(), actions.get(i).SingleTouchDragAndDropAccuracy2(),
        						actions.get(i).SingleTouchDragAndDropAccuracy(), actions.get(i).SingleTouchDragAndDropPathAccuracy());
        				bw.write(s);
        			}
        			for (int i = actions.size() + 1; i <= 5; i++) {
        				String  s = String.format("\n");
                        bw.write(s);
                    }
        		}
        		else if (measurementType == MeasurementType.Average) {
                    double avgTime = 0;
                    double avgOffset1 = 0;
                    double avgOffset2 = 0;
                    double avgOffset = 0;
                    double avgPathAccuracy = 0;
                    for (int i = 0; i < actions.size(); i++) {
                        avgTime = (avgTime + actions.get(i).SingleTouchDragAndDropTime());
                        avgOffset1 = (avgOffset1 + actions.get(i).SingleTouchDragAndDropAccuracy1());
                        avgOffset2 = (avgOffset2 + actions.get(i).SingleTouchDragAndDropAccuracy2());
                        avgOffset = (avgOffset + actions.get(i).SingleTouchDragAndDropAccuracy());
                        avgPathAccuracy = (avgPathAccuracy + actions.get(i).SingleTouchDragAndDropPathAccuracy());
                    }
                
                    if (actions.size() > 1) {
                    	avgTime /= actions.size();
                        avgOffset1 /= actions.size();
                        avgOffset2 /= actions.size();
                        avgOffset /= actions.size();
                        avgPathAccuracy /= actions.size();
                        String s = String.format("%.0f,%.2f,%.2f,%.2f,%.4f\n", avgTime, avgOffset1, avgOffset2, avgOffset, avgPathAccuracy);
                        bw.write(s);
                    }
                    else {
                    	String s = String.format("\n");
                        bw.write(s);
                    }                        
                }
                else if (measurementType == MeasurementType.BestPerformance) {
                    double minTime = Double.MAX_VALUE;
                    double minOffset1 = Double.MAX_VALUE;
                    double minOffset2 = Double.MAX_VALUE;
                    double minOffset = Double.MAX_VALUE;
                    double maxPathAccuracy = Double.MIN_VALUE;
                    for (int i = 0; i < actions.size(); i++) {
                        if (minTime > actions.get(i).SingleTouchDragAndDropTime())
                            minTime = actions.get(i).SingleTouchDragAndDropTime();                          
                        if (minOffset1 > actions.get(i).SingleTouchDragAndDropAccuracy1())
                            minOffset1 = actions.get(i).SingleTouchDragAndDropAccuracy1();                            
                        if (minOffset2 > actions.get(i).SingleTouchDragAndDropAccuracy2())
                            minOffset2 = actions.get(i).SingleTouchDragAndDropAccuracy2();                            
                        if (minOffset > actions.get(i).SingleTouchDragAndDropAccuracy())
                            minOffset = actions.get(i).SingleTouchDragAndDropAccuracy();                           
                        if (maxPathAccuracy < actions.get(i).SingleTouchDragAndDropPathAccuracy())
                            maxPathAccuracy = actions.get(i).SingleTouchDragAndDropPathAccuracy();                            
                    }
                    
                    if (actions.size() > 1) {
                    	String s = String.format("%.0f,%.2f,%.2f,%.2f,%.4f\n", minTime, minOffset1, minOffset2, minOffset, maxPathAccuracy);
                        bw.write(s);
                    }
                    else {
                    	String s = String.format("\n");
                        bw.write(s);
                    }
                }
            	         	
            	bw.flush();
            	bw.close(); fos.close(); osw.close();
            } catch ( IOException e ) {
                System.out.println(e);
            }
            
            System.out.printf("drag and drop task\n");           
        }
     
        private final void ComputeDoubleTapData(String outputFileName, MeasurementType measurementType) throws ParserConfigurationException, SAXException {
            try {
            	FileOutputStream fos = new FileOutputStream(new File(outputFileName));
        		OutputStreamWriter osw = new OutputStreamWriter(fos);
        		BufferedWriter bw = new BufferedWriter(osw);
        		
            	bw.write("dbltapTime,dbltapTime1,dbltapTime2,dbltapTimeBetween,dbltapAccuracy1,dbltapAccuracy2,dbltapAccuracy\n");
            	
        		List<TouchAction> actions = TouchIO.ReadFromFile("./DoubleTap.xml");
        		if (measurementType == MeasurementType.All) {          		
        			for (int i = 0; i < actions.size(); i++) {
        				String s = String.format("%.0f,%.0f,%.0f,%.0f,%.2f,%.2f,%.2f\n",
        						actions.get(i).DoubleTapTime(), actions.get(i).DoubleTapTime_FirstTap(), actions.get(i).DoubleTapTime_SecondTap(),
        						actions.get(i).DoubleTapTime_InBetweenTaps(), actions.get(i).DoubleTapAccuracy_FirstTap(), actions.get(i).DoubleTapAccuracy_SecondTap(),
        						actions.get(i).DoubleTapAccuracy());
        				bw.write(s);
        			}
        			for (int i = actions.size() + 1; i <= 5; i++) {
        				bw.newLine();
                    }
        		}
        		else if (measurementType == MeasurementType.Average) {
        			double avgDoubleTapTime = 0;
                    double avgDoubleTapTime_FirstTap = 0;
                    double avgDoubleTapTime_SecondTap = 0;
                    double avgDoubleTapTime_InBetweenTaps = 0;
                    double avgDoubleTapAccuracy_FirstTap = 0;
                    double avgDoubleTapAccuracy_SecondTap = 0;
                    double avgDoubleTapAccuracy = 0;
                    for (int i = 0; i < actions.size(); i++)
                    {
                        avgDoubleTapTime += actions.get(i).DoubleTapTime();
                        avgDoubleTapTime_FirstTap += actions.get(i).DoubleTapTime_FirstTap();
                        avgDoubleTapTime_SecondTap += actions.get(i).DoubleTapTime_SecondTap();
                        avgDoubleTapTime_InBetweenTaps += actions.get(i).DoubleTapTime_InBetweenTaps();
                        avgDoubleTapAccuracy_FirstTap += actions.get(i).DoubleTapAccuracy_FirstTap();
                        avgDoubleTapAccuracy_SecondTap += actions.get(i).DoubleTapAccuracy_SecondTap();
                        avgDoubleTapAccuracy += actions.get(i).DoubleTapAccuracy();
                    }
                    if (actions.size() > 0)
                    {
                        avgDoubleTapTime /= actions.size();
                        avgDoubleTapTime_FirstTap /= actions.size();
                        avgDoubleTapTime_SecondTap /= actions.size();
                        avgDoubleTapTime_InBetweenTaps /= actions.size();
                        avgDoubleTapAccuracy_FirstTap /= actions.size();
                        avgDoubleTapAccuracy_SecondTap /= actions.size();
                        avgDoubleTapAccuracy /= actions.size();
                        
                        String s = String.format("%.0f,%.0f,%.0f,%.0f,%.2f,%.2f,%.2f\n",
                                avgDoubleTapTime, avgDoubleTapTime_FirstTap, avgDoubleTapTime_SecondTap, avgDoubleTapTime_InBetweenTaps,
                                avgDoubleTapAccuracy_FirstTap, avgDoubleTapAccuracy_SecondTap, avgDoubleTapAccuracy);
                       bw.write(s);
                    }
                    else {
                    	bw.newLine();
                    }
                }
                else if (measurementType == MeasurementType.BestPerformance) {
                	 double minDoubleTapTime = Double.MAX_VALUE;
                     double minDoubleTapTime_FirstTap = Double.MAX_VALUE;
                     double minDoubleTapTime_SecondTap = Double.MAX_VALUE;
                     double minDoubleTapTime_InBetweenTaps = Double.MAX_VALUE;
                     double minDoubleTapAccuracy_FirstTap = Double.MAX_VALUE;
                     double minDoubleTapAccuracy_SecondTap = Double.MAX_VALUE;
                     double minDoubleTapAccuracy = Double.MAX_VALUE;
                     for (int i = 0; i < actions.size(); i++)
                     {
                         if (minDoubleTapTime > actions.get(i).DoubleTapTime())
                             minDoubleTapTime = actions.get(i).DoubleTapTime();
                         if (minDoubleTapTime_FirstTap > actions.get(i).DoubleTapTime_FirstTap())
                             minDoubleTapTime_FirstTap = actions.get(i).DoubleTapTime_FirstTap();
                         if (minDoubleTapTime_SecondTap > actions.get(i).DoubleTapTime_SecondTap())
                             minDoubleTapTime_SecondTap = actions.get(i).DoubleTapTime_SecondTap();
                         if (minDoubleTapTime_InBetweenTaps > actions.get(i).DoubleTapTime_InBetweenTaps())
                             minDoubleTapTime_InBetweenTaps = actions.get(i).DoubleTapTime_InBetweenTaps();
                         if (minDoubleTapAccuracy_FirstTap > actions.get(i).DoubleTapAccuracy_FirstTap())
                             minDoubleTapAccuracy_FirstTap = actions.get(i).DoubleTapAccuracy_FirstTap();
                         if (minDoubleTapAccuracy_SecondTap > actions.get(i).DoubleTapAccuracy_SecondTap())
                             minDoubleTapAccuracy_SecondTap = actions.get(i).DoubleTapAccuracy_SecondTap();
                         if (minDoubleTapAccuracy > actions.get(i).DoubleTapAccuracy())
                             minDoubleTapAccuracy = actions.get(i).DoubleTapAccuracy();
                     }
                     if (actions.size() > 0) {
                    	 String s = String.format("%.0f,%.0f,%.0f,%.0f,%.2f,%.2f,%.2f\n",
                             minDoubleTapTime, minDoubleTapTime_FirstTap, minDoubleTapTime_SecondTap, minDoubleTapTime_InBetweenTaps,
                             minDoubleTapAccuracy_FirstTap, minDoubleTapAccuracy_SecondTap, minDoubleTapAccuracy);
                    	 bw.write(s);
                     }
                     else {
                    	 bw.newLine();
                     }
                }
                   	
            	bw.flush();
            	bw.close(); fos.close(); osw.close();
            } catch ( IOException e ) {
                System.out.println(e);
            }
            System.out.printf("double tap task\n");
        }
        
        private final void ComputeTapData(String outputFileName, MeasurementType measurementType) throws ParserConfigurationException, SAXException {
            try {
            	FileOutputStream fos = new FileOutputStream(new File(outputFileName));
        		OutputStreamWriter osw = new OutputStreamWriter(fos);
        		BufferedWriter bw = new BufferedWriter(osw);
        		
            	bw.write("tapTime,tapAccuracy\n");
            	
            		List<TouchAction> actions = TouchIO.ReadFromFile("./SingleTap.xml");
        		if (measurementType == MeasurementType.All) {
        			for (int i = 0; i < actions.size(); i++) {
        				String s = String.format("%.0f,%.2f\n", actions.get(i).TapTime(), actions.get(i).TapAccuracy());
        				bw.write(s);
        			}
        			for (int i = actions.size() + 1; i <= 5; i++) {
        				bw.newLine();
                    }
        		}
        		else if (measurementType == MeasurementType.Average) {
        			double avgTime = 0;
                    double avgAccuracy = 0;
                    for (int i = 0; i < actions.size(); i++)
                    {
                        avgTime += actions.get(i).TapTime();
                        avgAccuracy += actions.get(i).TapAccuracy();
                    }
                    if (actions.size() > 0)
                    {
                        avgTime /= actions.size();
                        avgAccuracy /= actions.size();
                        
                        String s = String.format("%.0f,%.2f\n", avgTime, avgAccuracy);
                        bw.write(s);
                    }
                    else {
                    	String s = String.format("\n");
                    	bw.write(s);
                    }
                }
                else if (measurementType == MeasurementType.BestPerformance) {
                	double minTime = Double.MAX_VALUE;
                    double minOffset = Double.MAX_VALUE;
                    for (int i = 0; i < actions.size(); i++)
                    {
                        if (minTime > actions.get(i).TapTime()) 
                        	minTime = actions.get(i).TapTime();
                        if (minOffset > actions.get(i).TapAccuracy()) 
                        	minOffset = actions.get(i).TapAccuracy();
                    }
                    if (actions.size() > 0) {
                    	String s = String.format("%.0f,%.2f\n", minTime, minOffset);
                    	bw.write(s);
                    }
                    else {
                        String s= String.format("\n");
                        bw.write(s);
                    }
                }
                 	
            	bw.flush();
            	bw.close(); fos.close(); osw.close();
            } catch ( IOException e ) {
                System.out.println(e);
            }
            
            System.out.printf("tap task\n");           
        }
        
    }