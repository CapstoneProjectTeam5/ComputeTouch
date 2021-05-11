package computeTouchMeasurements;

import java.io.*;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class Program {
        
        private enum MeasurementType {       
            None, All, Average, BestPerformance
        }
        
        public static void main(String[] args) throws ParserConfigurationException, SAXException {
            //new Program();
        	new Compute();
            System.out.println("done.");
            Scanner in = new Scanner(System.in);
            String s = in.nextLine();
            System.out.println(s);
            in.close();
        }
        
        public Program() throws ParserConfigurationException, SAXException {
            this.PrintCopyrightText();
            //  please update these paths with the location of your data
            String PHONE_DATA = "./Smartphone";
            String TABLET_DATA = "./Tablet";
            int PHONE = 1;
            int TABLET = 2;
            //  the following calls compute all the measures for all tasks and devices in various conditions 
            //  (i.e., all performance, average performance, and best performance)
            this.ComputeTapData(PHONE_DATA, PHONE, "phone-tap.txt", MeasurementType.All);
            this.ComputeTapData(TABLET_DATA, TABLET, "tablet-tap.txt", MeasurementType.All);
            this.ComputeDoubleTapData(PHONE_DATA, PHONE, "phone-doubletap.txt", MeasurementType.BestPerformance);
            this.ComputeDoubleTapData(TABLET_DATA, TABLET, "tablet-doubletap.txt", MeasurementType.BestPerformance);
            this.ComputeSingleTouchDragAndDropData(PHONE_DATA, PHONE, "phone-singletouchdraganddrop.txt", MeasurementType.BestPerformance);
            this.ComputeSingleTouchDragAndDropData(TABLET_DATA, TABLET, "tablet-singletouchdraganddrop.txt", MeasurementType.BestPerformance);
            this.ComputeMultiTouchDragAndDropData(PHONE_DATA, PHONE, "phone-multitouchdraganddrop.txt", MeasurementType.BestPerformance);
            this.ComputeMultiTouchDragAndDropData(TABLET_DATA, TABLET, "tablet-multitouchdraganddrop.txt", MeasurementType.BestPerformance);
        }
        
        private final void ComputeMultiTouchDragAndDropData(String baseFolder, int deviceType, String outputFileName, MeasurementType measurementType) throws ParserConfigurationException, SAXException {
            int count = 0;
            
            try {
        		FileOutputStream fos = new FileOutputStream(new File(outputFileName));
        		OutputStreamWriter osw = new OutputStreamWriter(fos);
        		BufferedWriter bw = new BufferedWriter(osw);
        		
            	bw.write("participant,device,trial,multidragdropTime,multiAccuracy1,multiAccuracy2,multiAccuracy3,multiAccuracy4,multiAccuracy,multiPathAccuracy1,multiPathAccuracy2,multiPathAccuracy");
            	bw.newLine();
            	
            	File path = new File(baseFolder);
            	File[] fList = path.listFiles();
            	
            	for (int fi = 0; fi < fList.length; fi++) {
            		String participantName = fList[fi].getName();
            		List<TouchAction> actions = TouchIO.ReadFromFile(fList[fi] + "/multitouch-draganddrop.xml");
            		count += actions.size();
            		 
            		if (measurementType == MeasurementType.All) {
	                    for (int i = 0; (i < actions.size()); i++) {
	                        String s = String.format("%s,%d,%d,%.0f,%.2f,%.2f,%.2f,%.2f,%.2f,%.4f,%.4f,%.4f\n", participantName, deviceType, (i + 1),
	                        		actions.get(i).MultiTouchDragAndDropTime(), actions.get(i).MultiTouchDragAndDropAccuracy1(), actions.get(i).MultiTouchDragAndDropAccuracy2(), 
	                        		actions.get(i).MultiTouchDragAndDropAccuracy3(), actions.get(i).MultiTouchDragAndDropAccuracy4(),
	                        		actions.get(i).MultiTouchDragAndDropAccuracy(), actions.get(i).MultiTouchDragAndDropPathAccuracy1(),
	                        		actions.get(i).MultiTouchDragAndDropPathAccuracy2(), actions.get(i).MultiTouchDragAndDropPathAccuracy());
	                        bw.write(s);
	                    }
	                    for (int i = actions.size() + 1; i <= 5; i++) {
	                        String s = String.format("%s,%d,%d,,,,,,,,,\n", participantName, deviceType, i);
	                        bw.write(s);
	                    }
	                }
	                else if (measurementType == MeasurementType.Average) {
	                    double avgTime = 0;
	                    double avgOffset1 = 0;
	                    double avgOffset2 = 0;
	                    double avgOffset3 = 0;
	                    double avgOffset4 = 0;
	                    double avgOffset = 0;
	                    double avgPathAccuracy1 = 0;
	                    double avgPathAccuracy2 = 0;
	                    double avgPathAccuracy = 0;
	                    for (int i = 0; i < actions.size(); i++) {
	                    	 avgTime += actions.get(i).MultiTouchDragAndDropTime();
                             avgOffset1 += actions.get(i).MultiTouchDragAndDropAccuracy1();
                             avgOffset2 += actions.get(i).MultiTouchDragAndDropAccuracy2();
                             avgOffset3 += actions.get(i).MultiTouchDragAndDropAccuracy3();
                             avgOffset4 += actions.get(i).MultiTouchDragAndDropAccuracy4();
                             avgOffset += actions.get(i).MultiTouchDragAndDropAccuracy();
                             avgPathAccuracy1 += actions.get(i).MultiTouchDragAndDropPathAccuracy1();
                             avgPathAccuracy2 += actions.get(i).MultiTouchDragAndDropPathAccuracy2();
                             avgPathAccuracy += actions.get(i).MultiTouchDragAndDropPathAccuracy();
	                    }
	                    if (actions.size() > 0) {
	                    	avgTime /= actions.size();
                            avgOffset1 /= actions.size();
                            avgOffset2 /= actions.size();
                            avgOffset3 /= actions.size();
                            avgOffset4 /= actions.size();
                            avgOffset /= actions.size();
                            avgPathAccuracy1 /= actions.size();
                            avgPathAccuracy2 /= actions.size();
                            avgPathAccuracy /= actions.size();
                            
                            String s = String.format("%s,%d,%d,%.0f,%.2f,%.2f,%.2f,%.2f,%.2f,%.4f,%.4f,%.4f\n", participantName, deviceType, 0,
                                avgTime,
                                avgOffset1, avgOffset2, avgOffset3, avgOffset4, avgOffset,
                                avgPathAccuracy1, avgPathAccuracy2, avgPathAccuracy);
                            bw.write(s);
	                    }
	                    else {
	                        String s = String.format("%s,%d,%d,,,,,,,,,\n", participantName, deviceType, 0);
	                        bw.write(s);
	                    }
	                }
	                else if (measurementType == MeasurementType.BestPerformance) {
	                    double minTime = Double.MAX_VALUE;
	                    double minOffset1 = Double.MAX_VALUE;
	                    double minOffset2 = Double.MAX_VALUE;
	                    double minOffset3 = Double.MAX_VALUE;
	                    double minOffset4 = Double.MAX_VALUE;
	                    double minOffset = Double.MAX_VALUE;
	                    double maxPathAccuracy1 = Double.MIN_VALUE;
	                    double maxPathAccuracy2 = Double.MIN_VALUE;
	                    double maxPathAccuracy = Double.MIN_VALUE;
	                    
	                    for (int i = 0; i < actions.size(); i++) {
	                        if (minTime > actions.get(i).MultiTouchDragAndDropTime())
	                            minTime = actions.get(i).MultiTouchDragAndDropTime();
	                        if (minOffset1 > actions.get(i).MultiTouchDragAndDropAccuracy1())
	                            minOffset1 = actions.get(i).MultiTouchDragAndDropAccuracy1();
	                        if (minOffset2 > actions.get(i).MultiTouchDragAndDropAccuracy2())
	                            minOffset2 = actions.get(i).MultiTouchDragAndDropAccuracy2();
	                        if (minOffset3 > actions.get(i).MultiTouchDragAndDropAccuracy3())
	                            minOffset3 = actions.get(i).MultiTouchDragAndDropAccuracy3();
	                        if (minOffset4 > actions.get(i).MultiTouchDragAndDropAccuracy4())
	                            minOffset4 = actions.get(i).MultiTouchDragAndDropAccuracy4();	                        
	                        if (minOffset > actions.get(i).MultiTouchDragAndDropAccuracy())
	                            minOffset = actions.get(i).MultiTouchDragAndDropAccuracy();
	                        if (maxPathAccuracy1 < actions.get(i).MultiTouchDragAndDropPathAccuracy1())
	                            maxPathAccuracy1 = actions.get(i).MultiTouchDragAndDropPathAccuracy1();
	                        if (maxPathAccuracy2 < actions.get(i).MultiTouchDragAndDropPathAccuracy2())
	                            maxPathAccuracy2 = actions.get(i).MultiTouchDragAndDropPathAccuracy2();
	                        if (maxPathAccuracy < actions.get(i).MultiTouchDragAndDropPathAccuracy())
	                            maxPathAccuracy = actions.get(i).MultiTouchDragAndDropPathAccuracy();
	                    }
	                    
	                    if (actions.size() > 0) {
	                        String s = String.format("%s,%d,%d,%.0f,%.2f,%.2f,%.2f,%.2f,%.2f,%.4f,%.4f,%.4f\n", participantName, deviceType, 0, minTime, minOffset1, minOffset2, minOffset3, minOffset4, minOffset, maxPathAccuracy1, maxPathAccuracy2, maxPathAccuracy);
	                        bw.write(s);
	                    }
	                    else {
	                        String s = String.format("%s,%d,%d,,,,,,,,,\n", participantName, deviceType, 0);
	                        bw.write(s);
	                    }
	                }
            	}
            	bw.flush();
            	bw.close(); fos.close(); osw.close();
            } catch ( IOException e ) {
                    System.out.println(e);
            }
            System.out.printf("multi-touch task (%s): %d records\n", deviceType == 1 ? "phone" : "tablet", count);
        }
        
        private final void ComputeSingleTouchDragAndDropData(String baseFolder, int deviceType, String outputFileName, MeasurementType measurementType) throws ParserConfigurationException, SAXException {
            int count = 0;
            
            try {
            	FileOutputStream fos = new FileOutputStream(new File(outputFileName));
        		OutputStreamWriter osw = new OutputStreamWriter(fos);
        		BufferedWriter bw = new BufferedWriter(osw);
        		
            	bw.write("participant,device,trial,sgldragdropTime,slgdragdropAccuracy1,sgldragdropAccuracy2,sgldragdropAccuracy,sgldragdropPathAccuracy");
            	bw.newLine();
            	
            	File path = new File(baseFolder);
            	File[] fList = path.listFiles();
            	
            	for (int fi = 0; fi < fList.length; fi++) {
            		String participantName = fList[fi].getName();
            		List<TouchAction> actions = TouchIO.ReadFromFile(fList[fi] + "/singletouch-draganddrop.xml");
            		count += actions.size();
            		if (measurementType == MeasurementType.All) {
            			for (int i = 0; i < actions.size(); i++) {
            				String s = String.format("%s,%d,%d,%.0f,%.2f,%.2f,%.2f,%.4f\n", participantName, deviceType, (i + 1),
            						actions.get(i).SingleTouchDragAndDropTime(), actions.get(i).SingleTouchDragAndDropAccuracy1(), actions.get(i).SingleTouchDragAndDropAccuracy2(),
            						actions.get(i).SingleTouchDragAndDropAccuracy(), actions.get(i).SingleTouchDragAndDropPathAccuracy());
            				bw.write(s);
            			}
            			for (int i = actions.size() + 1; i <= 5; i++) {
            				String  s = String.format("%s,%d,%d,,,,,\n", participantName, deviceType, i);
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
                            String s = String.format("%s,%d,%d,%.0f,%.2f,%.2f,%.2f,%.4f\n", participantName, deviceType, 0, avgTime, avgOffset1, avgOffset2, avgOffset, avgPathAccuracy);
                            bw.write(s);
                        }
                        else {
                        	String s = String.format("%s,%d,%d,,,,,\n", participantName, deviceType, 0);
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
                        	String s = String.format("%s,%d,%d,%.0f,%.2f,%.2f,%.2f,%.4f\n", participantName, deviceType, 0, minTime, minOffset1, minOffset2, minOffset, maxPathAccuracy);
                            bw.write(s);
                        }
                        else {
                        	String s = String.format("%s,%d,%d,,,,,\n", participantName, deviceType, 0);
                            bw.write(s);
                        }
                    }
            	}         	
            	bw.flush();
            	bw.close(); fos.close(); osw.close();
            } catch ( IOException e ) {
                System.out.println(e);
            }
            
            System.out.printf("single-touch task (%s): %d records\n", deviceType == 1 ? "phone" : "tablet", count);           
        }
     
        private final void ComputeDoubleTapData(String baseFolder, int deviceType, String outputFileName, MeasurementType measurementType) throws ParserConfigurationException, SAXException {
            int count = 0;
            
            try {
            	FileOutputStream fos = new FileOutputStream(new File(outputFileName));
        		OutputStreamWriter osw = new OutputStreamWriter(fos);
        		BufferedWriter bw = new BufferedWriter(osw);
        		
            	bw.write("participant,device,trial,dbltapTime,dbltapTime1,dbltapTime2,dbltapTimeBetween,dbltapAccuracy1,dbltapAccuracy2,dbltapAccuracy\n");
            	
            	File path = new File(baseFolder);
            	File[] fList = path.listFiles();
            	
            	for (int fi = 0; fi < fList.length; fi++) {
            		String participantName = fList[fi].getName();
            		List<TouchAction> actions = TouchIO.ReadFromFile((fList[fi] + "/doubletap.xml"));
            		count += actions.size();
            		if (measurementType == MeasurementType.All) {          		
            			for (int i = 0; i < actions.size(); i++) {
            				String s = String.format("%s,%d,%d,%.0f,%.0f,%.0f,%.0f,%.2f,%.2f,%.2f\n", participantName, deviceType, (i + 1),
            						actions.get(i).DoubleTapTime(), actions.get(i).DoubleTapTime_FirstTap(), actions.get(i).DoubleTapTime_SecondTap(),
            						actions.get(i).DoubleTapTime_InBetweenTaps(), actions.get(i).DoubleTapAccuracy_FirstTap(), actions.get(i).DoubleTapAccuracy_SecondTap(),
            						actions.get(i).DoubleTapAccuracy());
            				bw.write(s);
            			}
            			for (int i = actions.size() + 1; i <= 5; i++) {
            				String  s = String.format("%s,%d,%d,,,,,,,\n", participantName, deviceType, i);
                            bw.write(s);
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
                            
                            String s = String.format("%s,%d,%d,%.0f,%.0f,%.0f,%.0f,%.2f,%.2f,%.2f\n", participantName, deviceType, 0,
                                    avgDoubleTapTime, avgDoubleTapTime_FirstTap, avgDoubleTapTime_SecondTap, avgDoubleTapTime_InBetweenTaps,
                                    avgDoubleTapAccuracy_FirstTap, avgDoubleTapAccuracy_SecondTap, avgDoubleTapAccuracy);
                           bw.write(s);
                        }
                        else {
                        	String s = String.format("%s,%d,%d,,,,,,,\n", participantName, deviceType, 0);
                        	bw.write(s);
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
                        	 String s = String.format("%s,%d,%d,%.0f,%.0f,%.0f,%.0f,%.2f,%.2f,%.2f\n", participantName, deviceType, 0,
                                 minDoubleTapTime, minDoubleTapTime_FirstTap, minDoubleTapTime_SecondTap, minDoubleTapTime_InBetweenTaps,
                                 minDoubleTapAccuracy_FirstTap, minDoubleTapAccuracy_SecondTap, minDoubleTapAccuracy);
                        	 bw.write(s);
                         }
                         else {
                        	 String s = String.format("%s,%d,%d,,,,,,,\n", participantName, deviceType, 0);
                        	 bw.write(s);
                         }
                    }
                }    	
            	bw.flush();
            	bw.close(); fos.close(); osw.close();
            } catch ( IOException e ) {
                System.out.println(e);
            }
            System.out.printf("double tap task (%s): %d records\n", deviceType == 1 ? "phone" : "tablet", count);   
        }
        
        private final void ComputeTapData(String baseFolder, int deviceType, String outputFileName, MeasurementType measurementType) throws ParserConfigurationException, SAXException {
            int count = 0;
            
            try {
            	FileOutputStream fos = new FileOutputStream(new File(outputFileName));
        		OutputStreamWriter osw = new OutputStreamWriter(fos);
        		BufferedWriter bw = new BufferedWriter(osw);
        		
            	bw.write("participant,device,trial,tapTime,tapAccuracy\n");
            	
            	File path = new File(baseFolder);
            	File[] fList = path.listFiles();
            	
            	for (int fi = 0; fi < fList.length; fi++) {
            		String participantName = fList[fi].getName();
            		List<TouchAction> actions = TouchIO.ReadFromFile((fList[fi] + "/tap.xml"));
            		count += actions.size();
            		if (measurementType == MeasurementType.All) {
            			for (int i = 0; i < actions.size(); i++) {
            				String s = String.format("%s,%d,%d,%.0f,%.2f\n", participantName, deviceType, i + 1, actions.get(i).TapTime(), actions.get(i).TapAccuracy());
            				bw.write(s);
            			}
            			for (int i = actions.size() + 1; i <= 5; i++) {
            				String  s = String.format("%s,%d,%d,,\n", participantName, deviceType, i);
                            bw.write(s);
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
                            
                            String s = String.format("%s,%d,%d,%.0f,%.2f\n", participantName, deviceType, 0, avgTime, avgAccuracy);
                            bw.write(s);
                        }
                        else {
                        	String s = String.format("%s,%d,%d,,\n", participantName, deviceType, 0);
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
                        	String s = String.format("%s,%d,%d,%.0f,%.2f\n", participantName, deviceType, 0, minTime, minOffset);
                        	bw.write(s);
                        }
                        else {
                            String s= String.format("%s,%d,%d,,\n", participantName, deviceType, 0);
                            bw.write(s);
                        }
                    }
                }    	
            	bw.flush();
            	bw.close(); fos.close(); osw.close();
            } catch ( IOException e ) {
                System.out.println(e);
            }
            
            System.out.printf("tap task (%s): %d records\n", deviceType == 1 ? "phone" : "tablet", count);           
        }
        
        private final void PrintCopyrightText() {
            System.out.println("    Radu-Daniel Vatavu, Ph.D.\n    University Stefan cel Mare of Suceava\n    Suceava 720229, Romania\n " +
                "   vatavu@eed.usv.ro\n \n The academic publication for this work, and what should be used to cite it, " +
                "is:\n \n    Radu-Daniel Vatavu, Gabriel Cramariuc, Doina-Maria Schipor. (2015). \n    Touch Interaction" +
                " for Children Aged 3 to 6 Years: Experimental Findings and Relationship to Motor Skills.\n    Interna" +
                "tional Journal of Human-Computer Studies 74. Elsevier, 54-76\n    http://dx.doi.org/10.1016/j.ijhcs.2" +
                "014.10.007\n  \n This software is distributed under the New BSD License agreement:\n \n Copyright (c) 20" +
                "14, Radu-Daniel Vatavu. All rights reserved.\n \n Redistribution and use in source and binary forms, w" +
                "ith or without\n modification, are permitted provided that the following conditions are met:\n     * R" +
                "edistributions of source code must retain the above copyright\n       notice, this list of conditions" +
                " and the following disclaimer.\n     * Redistributions in binary form must reproduce the above copyri" +
                "ght\n       notice, this list of conditions and the following disclaimer in the\n       documentation " +
                "and/or other materials provided with the distribution.\n     * Neither the name of the University Ste" +
                "fan cel Mare of Suceava, \n       nor the names of its contributors may be used to endorse or promote" +
                " products\n       derived from this software without specific prior written permission.\n \n THIS SOFTW" +
                "ARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS AS\n IS AND ANY EXPRESS OR IMPLIED WARRANTI" +
                "ES, INCLUDING, BUT NOT LIMITED TO,\n THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PART" +
                "ICULAR\n PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL Radu-Daniel Vatavu \n BE LIABLE FOR ANY DIRECT, IND" +
                "IRECT, INCIDENTAL, SPECIAL, \n EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PR" +
                "OCUREMENT \n OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS \n INTERRUPTI" +
                "ON) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, \n STRICT LIABILITY, OR TORT " +
                "(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY\n OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADV" +
                "ISED OF THE POSSIBILITY OF\n SUCH DAMAGE.");
        }
    }