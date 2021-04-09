/*
    Radu-Daniel Vatavu, Ph.D.
    University Stefan cel Mare of Suceava
    Suceava 720229, Romania
    vatavu@eed.usv.ro
 
 The academic publication for this work, and what should be used to cite it, is:
 
    Radu-Daniel Vatavu, Gabriel Cramariuc, Doina-Maria Schipor. (2015). 
    Touch Interaction for Children Aged 3 to 6 Years: Experimental Findings and Relationship to Motor Skills.
    International Journal of Human-Computer Studies 74. Elsevier, 54-76
    http://dx.doi.org/10.1016/j.ijhcs.2014.10.007
  
 This software is distributed under the "New BSD License" agreement:
 
 Copyright (c) 2014, Radu-Daniel Vatavu. All rights reserved.
 
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
     * Redistributions of source code must retain the above copyright
       notice, this list of conditions and the following disclaimer.
     * Redistributions in binary form must reproduce the above copyright
       notice, this list of conditions and the following disclaimer in the
       documentation and/or other materials provided with the distribution.
     * Neither the name of the University Stefan cel Mare of Suceava, 
       nor the names of its contributors may be used to endorse or promote products
       derived from this software without specific prior written permission.
 
 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL Radu-Daniel Vatavu 
 BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT 
 OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.
*/

using System;
using System.Collections.Generic;
using System.IO;

namespace ComputeTouchMeasurements
{
    class Program
    {
        private enum MeasurementType
        {
            All = 1,
            Average = 2,
            BestPerformance = 3
        };

        static void Main(string[] args)
        {
            new Program();
            Console.WriteLine("done.");
            Console.Read();
        }

        public Program()
        {
            PrintCopyrightText();

            // please update these paths with the location of your data
            string PHONE_DATA = @"C:\Users\ab892\OneDrive\바탕 화면\캡스톤\Dataset\Dataset\Smartphone";
            string TABLET_DATA = @"C:\Users\ab892\OneDrive\바탕 화면\캡스톤\Dataset\Dataset\Tablet";
            int PHONE = 1;
            int TABLET = 2;

            // the following calls compute all the measures for all tasks and devices in various conditions 
            // (i.e., all performance, average performance, and best performance)
            ComputeTapData(PHONE_DATA, PHONE, "phone-tap.txt", MeasurementType.All);
            ComputeTapData(TABLET_DATA, TABLET, "tablet-tap.txt", MeasurementType.All);
            ComputeDoubleTapData(PHONE_DATA, PHONE, "phone-doubletap.txt", MeasurementType.BestPerformance);
            ComputeDoubleTapData(TABLET_DATA, TABLET, "tablet-doubletap.txt", MeasurementType.BestPerformance);
            ComputeSingleTouchDragAndDropData(PHONE_DATA, PHONE, "phone-singletouchdraganddrop.txt", MeasurementType.BestPerformance);
            ComputeSingleTouchDragAndDropData(TABLET_DATA, TABLET, "tablet-singletouchdraganddrop.txt", MeasurementType.BestPerformance);
            ComputeMultiTouchDragAndDropData(PHONE_DATA, PHONE, "phone-multitouchdraganddrop.txt", MeasurementType.BestPerformance);
            ComputeMultiTouchDragAndDropData(TABLET_DATA, TABLET, "tablet-multitouchdraganddrop.txt", MeasurementType.BestPerformance);
        }

        private void ComputeMultiTouchDragAndDropData(string baseFolder, int deviceType, string outputFileName, MeasurementType measurementType)
        {
            int count = 0;
            using (StreamWriter writer = new StreamWriter(outputFileName))
            {
                writer.WriteLine("participant,device,trial,multidragdropTime,multiAccuracy1,multiAccuracy2,multiAccuracy3,multiAccuracy4,multiAccuracy,multiPathAccuracy1,multiPathAccuracy2,multiPathAccuracy");
                string[] folders = Directory.GetDirectories(baseFolder);
                foreach (string folder in folders)
                {
                    string participantName = folder.Substring(folder.LastIndexOf("\\") + 1);
                    List<TouchAction> actions = TouchIO.ReadFromFile(folder + "\\multitouch-draganddrop.xml");
                    count += actions.Count;

                    if (measurementType == MeasurementType.All)
                    {
                        for (int i = 0; i < actions.Count; i++)
                            writer.WriteLine("{0},{1},{2},{3},{4:.00},{5:.00},{6:.00},{7:.00},{8:.00},{9:.0000},{10:.0000},{11:.0000}", participantName, deviceType, i + 1,
                                actions[i].MultiTouchDragAndDropTime,
                                actions[i].MultiTouchDragAndDropAccuracy1, actions[i].MultiTouchDragAndDropAccuracy2, actions[i].MultiTouchDragAndDropAccuracy3, actions[i].MultiTouchDragAndDropAccuracy4, actions[i].MultiTouchDragAndDropAccuracy,
                                actions[i].MultiTouchDragAndDropPathAccuracy1, actions[i].MultiTouchDragAndDropPathAccuracy2, actions[i].MultiTouchDragAndDropPathAccuracy
                            );
                        for (int i = actions.Count + 1; i <= 5; i++)
                            writer.WriteLine("{0},{1},{2},,,,,,,,,", participantName, deviceType, i);
                    }
                    else
                        if (measurementType == MeasurementType.Average)
                        {
                            double avgTime = 0;
                            double avgOffset1 = 0;
                            double avgOffset2 = 0;
                            double avgOffset3 = 0;
                            double avgOffset4 = 0;
                            double avgOffset = 0;
                            double avgPathAccuracy1 = 0;
                            double avgPathAccuracy2 = 0;
                            double avgPathAccuracy = 0;
                            for (int i = 0; i < actions.Count; i++)
                            {
                                avgTime += actions[i].MultiTouchDragAndDropTime;
                                avgOffset1 += actions[i].MultiTouchDragAndDropAccuracy1;
                                avgOffset2 += actions[i].MultiTouchDragAndDropAccuracy2;
                                avgOffset3 += actions[i].MultiTouchDragAndDropAccuracy3;
                                avgOffset4 += actions[i].MultiTouchDragAndDropAccuracy4;
                                avgOffset += actions[i].MultiTouchDragAndDropAccuracy;
                                avgPathAccuracy1 += actions[i].MultiTouchDragAndDropPathAccuracy1;
                                avgPathAccuracy2 += actions[i].MultiTouchDragAndDropPathAccuracy2;
                                avgPathAccuracy += actions[i].MultiTouchDragAndDropPathAccuracy;
                            };
                            if (actions.Count > 0)
                            {
                                avgTime /= actions.Count;
                                avgOffset1 /= actions.Count;
                                avgOffset2 /= actions.Count;
                                avgOffset3 /= actions.Count;
                                avgOffset4 /= actions.Count;
                                avgOffset /= actions.Count;
                                avgPathAccuracy1 /= actions.Count;
                                avgPathAccuracy2 /= actions.Count;
                                avgPathAccuracy /= actions.Count;
                                writer.WriteLine("{0},{1},{2},{3},{4:.00},{5:.00},{6:.00},{7:.00},{8:.00},{9:.0000},{10:.0000},{11:.0000}", participantName, deviceType, 0,
                                    avgTime,
                                    avgOffset1, avgOffset2, avgOffset3, avgOffset4, avgOffset,
                                    avgPathAccuracy1, avgPathAccuracy2, avgPathAccuracy
                                );
                            }
                            else writer.WriteLine("{0},{1},{2},,,,,,,,,", participantName, deviceType, 0);
                        }
                        else
                            if (measurementType == MeasurementType.BestPerformance)
                            {
                                double minTime = double.MaxValue;
                                double minOffset1 = double.MaxValue;
                                double minOffset2 = double.MaxValue;
                                double minOffset3 = double.MaxValue;
                                double minOffset4 = double.MaxValue;
                                double minOffset = double.MaxValue;
                                double maxPathAccuracy1 = double.MinValue;
                                double maxPathAccuracy2 = double.MinValue;
                                double maxPathAccuracy = double.MinValue;
                                for (int i = 0; i < actions.Count; i++)
                                {
                                    if (minTime > actions[i].MultiTouchDragAndDropTime)
                                        minTime = actions[i].MultiTouchDragAndDropTime;
                                    if (minOffset1 > actions[i].MultiTouchDragAndDropAccuracy1)
                                        minOffset1 = actions[i].MultiTouchDragAndDropAccuracy1;
                                    if (minOffset2 > actions[i].MultiTouchDragAndDropAccuracy2)
                                        minOffset2 = actions[i].MultiTouchDragAndDropAccuracy2;
                                    if (minOffset3 > actions[i].MultiTouchDragAndDropAccuracy3)
                                        minOffset3 = actions[i].MultiTouchDragAndDropAccuracy3;
                                    if (minOffset4 > actions[i].MultiTouchDragAndDropAccuracy4)
                                        minOffset4 = actions[i].MultiTouchDragAndDropAccuracy4;
                                    if (minOffset > actions[i].MultiTouchDragAndDropAccuracy)
                                        minOffset = actions[i].MultiTouchDragAndDropAccuracy;
                                    if (maxPathAccuracy1 < actions[i].MultiTouchDragAndDropPathAccuracy1)
                                        maxPathAccuracy1 = actions[i].MultiTouchDragAndDropPathAccuracy1;
                                    if (maxPathAccuracy2 < actions[i].MultiTouchDragAndDropPathAccuracy2)
                                        maxPathAccuracy2 = actions[i].MultiTouchDragAndDropPathAccuracy2;
                                    if (maxPathAccuracy < actions[i].MultiTouchDragAndDropPathAccuracy)
                                        maxPathAccuracy = actions[i].MultiTouchDragAndDropPathAccuracy;
                                };
                                if (actions.Count > 0)
                                    writer.WriteLine("{0},{1},{2},{3},{4:.00},{5:.00},{6:.00},{7:.00},{8:.00},{9:.0000},{10:.0000},{11:.0000}", participantName, deviceType, 0,
                                        minTime,
                                        minOffset1, minOffset2, minOffset3, minOffset4, minOffset,
                                        maxPathAccuracy1, maxPathAccuracy2, maxPathAccuracy
                                    );
                                else writer.WriteLine("{0},{1},{2},,,,,,,,,", participantName, deviceType, 0);
                            }
                }
            }
            Console.WriteLine("multi-touch task ({0}): {1} records", deviceType == 1 ? "phone" : "tablet", count);
        }

        private void ComputeSingleTouchDragAndDropData(string baseFolder, int deviceType, string outputFileName, MeasurementType measurementType)
        {
            int count = 0;
            using (StreamWriter writer = new StreamWriter(outputFileName))
            {
                writer.WriteLine("participant,device,trial,sgldragdropTime,slgdragdropAccuracy1,sgldragdropAccuracy2,sgldragdropAccuracy,sgldragdropPathAccuracy");
                string[] folders = Directory.GetDirectories(baseFolder);
                foreach (string folder in folders)
                {
                    string participantName = folder.Substring(folder.LastIndexOf("\\") + 1);
                    List<TouchAction> actions = TouchIO.ReadFromFile(folder + "\\singletouch-draganddrop.xml");
                    count += actions.Count;

                    if (measurementType == MeasurementType.All)
                    {
                        for (int i = 0; i < actions.Count; i++)
                            writer.WriteLine("{0},{1},{2},{3},{4:.00},{5:.00},{6:.00},{7:.0000}", participantName, deviceType, i + 1,
                                actions[i].SingleTouchDragAndDropTime,
                                actions[i].SingleTouchDragAndDropAccuracy1, actions[i].SingleTouchDragAndDropAccuracy2, actions[i].SingleTouchDragAndDropAccuracy,
                                actions[i].SingleTouchDragAndDropPathAccuracy
                            );
                        for (int i = actions.Count + 1; i <= 5; i++)
                            writer.WriteLine("{0},{1},{2},,,,,", participantName, deviceType, i);
                    }
                    else
                        if (measurementType == MeasurementType.Average)
                        {
                            double avgTime = 0;
                            double avgOffset1 = 0;
                            double avgOffset2 = 0;
                            double avgOffset = 0;
                            double avgPathAccuracy = 0;
                            for (int i = 0; i < actions.Count; i++)
                            {
                                 avgTime += actions[i].SingleTouchDragAndDropTime;
                                 avgOffset1 += actions[i].SingleTouchDragAndDropAccuracy1;
                                 avgOffset2 += actions[i].SingleTouchDragAndDropAccuracy2;
                                 avgOffset += actions[i].SingleTouchDragAndDropAccuracy;
                                 avgPathAccuracy += actions[i].SingleTouchDragAndDropPathAccuracy;
                            }
                            if (actions.Count > 1)
                            {
                                avgTime /= actions.Count;
                                avgOffset1 /= actions.Count;
                                avgOffset2 /= actions.Count;
                                avgOffset /= actions.Count;
                                avgPathAccuracy /= actions.Count;
                                writer.WriteLine("{0},{1},{2},{3},{4:.00},{5:.00},{6:.00},{7:.0000}", participantName, deviceType, 0,
                                    avgTime,
                                    avgOffset1, avgOffset2, avgOffset,
                                    avgPathAccuracy
                                );
                            }
                            else writer.WriteLine("{0},{1},{2},,,,,", participantName, deviceType, 0);
                        }
                        else
                            if (measurementType == MeasurementType.BestPerformance)
                            {
                                double minTime = double.MaxValue;
                                double minOffset1 = double.MaxValue;
                                double minOffset2 = double.MaxValue;
                                double minOffset = double.MaxValue;
                                double maxPathAccuracy = double.MinValue;
                                for (int i = 0; i < actions.Count; i++)
                                {
                                    if (minTime > actions[i].SingleTouchDragAndDropTime)
                                        minTime = actions[i].SingleTouchDragAndDropTime;
                                    if (minOffset1 > actions[i].SingleTouchDragAndDropAccuracy1)
                                        minOffset1 = actions[i].SingleTouchDragAndDropAccuracy1;
                                    if (minOffset2 > actions[i].SingleTouchDragAndDropAccuracy2)
                                        minOffset2 = actions[i].SingleTouchDragAndDropAccuracy2;
                                    if (minOffset > actions[i].SingleTouchDragAndDropAccuracy)
                                        minOffset = actions[i].SingleTouchDragAndDropAccuracy;
                                    if (maxPathAccuracy < actions[i].SingleTouchDragAndDropPathAccuracy)
                                        maxPathAccuracy = actions[i].SingleTouchDragAndDropPathAccuracy;
                                }
                                if (actions.Count > 1)
                                    writer.WriteLine("{0},{1},{2},{3},{4:.00},{5:.00},{6:.00},{7:.0000}", participantName, deviceType, 0,
                                        minTime, minOffset1, minOffset2, minOffset, maxPathAccuracy
                                    );
                                else writer.WriteLine("{0},{1},{2},,,,,", participantName, deviceType, 0);
                            }
                }
            }
            Console.WriteLine("single-touch task ({0}): {1} records", deviceType == 1 ? "phone" : "tablet", count);
        }

        private void ComputeDoubleTapData(string baseFolder, int deviceType, string outputFileName, MeasurementType measurementType)
        {
            int count = 0;
            using (StreamWriter writer = new StreamWriter(outputFileName))
            {
                writer.WriteLine("participant,device,trial,dbltapTime,dbltapTime1,dbltapTime2,dbltapTimeBetween,dbltapAccuracy1,dbltapAccuracy2,dbltapAccuracy");
                string[] folders = Directory.GetDirectories(baseFolder);
                foreach (string folder in folders)
                {
                    string participantName = folder.Substring(folder.LastIndexOf("\\") + 1);
                    List<TouchAction> actions = TouchIO.ReadFromFile(folder + "\\doubletap.xml");
                    count += actions.Count;

                    if (measurementType == MeasurementType.All)
                    {
                        for (int i = 0; i < actions.Count; i++)
                            writer.WriteLine("{0},{1},{2},{3},{4},{5},{6},{7:.00},{8:.00},{9:.00}", participantName, deviceType, i + 1,
                                actions[i].DoubleTapTime, actions[i].DoubleTapTime_FirstTap, actions[i].DoubleTapTime_SecondTap, actions[i].DoubleTapTime_InBetweenTaps,
                                actions[i].DoubleTapAccuracy_FirstTap, actions[i].DoubleTapAccuracy_SecondTap, actions[i].DoubleTapAccuracy
                            );
                        for (int i = actions.Count + 1; i <= 5; i++)
                            writer.WriteLine("{0},{1},{2},,,,,,,", participantName, deviceType, i);
                    }
                    else
                        if (measurementType == MeasurementType.Average)
                        {
                            double avgDoubleTapTime = 0;
                            double avgDoubleTapTime_FirstTap = 0;
                            double avgDoubleTapTime_SecondTap = 0;
                            double avgDoubleTapTime_InBetweenTaps = 0;
                            double avgDoubleTapAccuracy_FirstTap = 0;
                            double avgDoubleTapAccuracy_SecondTap = 0;
                            double avgDoubleTapAccuracy = 0;
                            for (int i = 0; i < actions.Count; i++)
                            {
                                avgDoubleTapTime += actions[i].DoubleTapTime;
                                avgDoubleTapTime_FirstTap += actions[i].DoubleTapTime_FirstTap;
                                avgDoubleTapTime_SecondTap += actions[i].DoubleTapTime_SecondTap;
                                avgDoubleTapTime_InBetweenTaps += actions[i].DoubleTapTime_InBetweenTaps;
                                avgDoubleTapAccuracy_FirstTap += actions[i].DoubleTapAccuracy_FirstTap;
                                avgDoubleTapAccuracy_SecondTap += actions[i].DoubleTapAccuracy_SecondTap;
                                avgDoubleTapAccuracy += actions[i].DoubleTapAccuracy;
                            }
                            if (actions.Count > 0)
                            {
                                avgDoubleTapTime /= actions.Count;
                                avgDoubleTapTime_FirstTap /= actions.Count;
                                avgDoubleTapTime_SecondTap /= actions.Count;
                                avgDoubleTapTime_InBetweenTaps /= actions.Count;
                                avgDoubleTapAccuracy_FirstTap /= actions.Count;
                                avgDoubleTapAccuracy_SecondTap /= actions.Count;
                                avgDoubleTapAccuracy /= actions.Count;
                                writer.WriteLine("{0},{1},{2},{3},{4},{5},{6},{7:.00},{8:.00},{9:.00}", participantName, deviceType, 0,
                                    avgDoubleTapTime, avgDoubleTapTime_FirstTap, avgDoubleTapTime_SecondTap, avgDoubleTapTime_InBetweenTaps,
                                    avgDoubleTapAccuracy_FirstTap, avgDoubleTapAccuracy_SecondTap, avgDoubleTapAccuracy
                                );
                            }
                            else writer.WriteLine("{0},{1},{2},,,,,,,", participantName, deviceType, 0);
                        }
                        else
                            if (measurementType == MeasurementType.BestPerformance)
                            {
                                double minDoubleTapTime = double.MaxValue;
                                double minDoubleTapTime_FirstTap = double.MaxValue;
                                double minDoubleTapTime_SecondTap = double.MaxValue;
                                double minDoubleTapTime_InBetweenTaps = double.MaxValue;
                                double minDoubleTapAccuracy_FirstTap = double.MaxValue;
                                double minDoubleTapAccuracy_SecondTap = double.MaxValue;
                                double minDoubleTapAccuracy = double.MaxValue;
                                for (int i = 0; i < actions.Count; i++)
                                {
                                    if (minDoubleTapTime > actions[i].DoubleTapTime)
                                        minDoubleTapTime = actions[i].DoubleTapTime;
                                    if (minDoubleTapTime_FirstTap > actions[i].DoubleTapTime_FirstTap)
                                        minDoubleTapTime_FirstTap = actions[i].DoubleTapTime_FirstTap;
                                    if (minDoubleTapTime_SecondTap > actions[i].DoubleTapTime_SecondTap)
                                        minDoubleTapTime_SecondTap = actions[i].DoubleTapTime_SecondTap;
                                    if (minDoubleTapTime_InBetweenTaps > actions[i].DoubleTapTime_InBetweenTaps)
                                        minDoubleTapTime_InBetweenTaps = actions[i].DoubleTapTime_InBetweenTaps;
                                    if (minDoubleTapAccuracy_FirstTap > actions[i].DoubleTapAccuracy_FirstTap)
                                        minDoubleTapAccuracy_FirstTap = actions[i].DoubleTapAccuracy_FirstTap;
                                    if (minDoubleTapAccuracy_SecondTap > actions[i].DoubleTapAccuracy_SecondTap)
                                        minDoubleTapAccuracy_SecondTap = actions[i].DoubleTapAccuracy_SecondTap;
                                    if (minDoubleTapAccuracy > actions[i].DoubleTapAccuracy)
                                        minDoubleTapAccuracy = actions[i].DoubleTapAccuracy;
                                }
                                if (actions.Count > 0)
                                    writer.WriteLine("{0},{1},{2},{3},{4},{5},{6},{7:.00},{8:.00},{9:.00}", participantName, deviceType, 0,
                                        minDoubleTapTime, minDoubleTapTime_FirstTap, minDoubleTapTime_SecondTap, minDoubleTapTime_InBetweenTaps,
                                        minDoubleTapAccuracy_FirstTap, minDoubleTapAccuracy_SecondTap, minDoubleTapAccuracy
                                );
                                else writer.WriteLine("{0},{1},{2},,,,,,,", participantName, deviceType, 0);
                            }
                }
            }
            Console.WriteLine("double tap task ({0}): {1} records", deviceType == 1 ? "phone" : "tablet", count);
        }

        private void ComputeTapData(string baseFolder, int deviceType, string outputFileName, MeasurementType measurementType)
        {
            int count = 0; 
            using (StreamWriter writer = new StreamWriter(outputFileName))
            {
                writer.WriteLine("participant,device,trial,tapTime,tapAccuracy");
                string[] folders = Directory.GetDirectories(baseFolder);
                foreach (string folder in folders)
                {
                    string participantName = folder.Substring(folder.LastIndexOf("\\") + 1);
                    List<TouchAction> actions = TouchIO.ReadFromFile(folder + "\\tap.xml");
                    count += actions.Count;

                    if (measurementType == MeasurementType.All)
                    {
                        for (int i = 0; i < actions.Count; i++)
                            writer.WriteLine("{0},{1},{2},{3},{4:.00}", participantName, deviceType, i + 1, actions[i].TapTime, actions[i].TapAccuracy);
                        for (int i = actions.Count + 1; i <= 5; i++)
                            writer.WriteLine("{0},{1},{2},,", participantName, deviceType, i);
                    }
                    else
                    if (measurementType == MeasurementType.Average)
                    {
                        double avgTime = 0;
                        double avgAccuracy = 0;
                        for (int i = 0; i < actions.Count; i++)
                        {
                            avgTime += actions[i].TapTime;
                            avgAccuracy += actions[i].TapAccuracy;
                        }
                        if (actions.Count > 0)
                        {
                            avgTime /= actions.Count;
                            avgAccuracy /= actions.Count;
                            writer.WriteLine("{0},{1},{2},{3},{4:.00}", participantName, deviceType, 0, avgTime, avgAccuracy);
                        }
                        else
                            writer.WriteLine("{0},{1},{2},,", participantName, deviceType, 0);
                    }
                    else
                        if (measurementType == MeasurementType.BestPerformance)
                        {
                            double minTime = double.MaxValue;
                            double minOffset = double.MaxValue;
                            for (int i = 0; i < actions.Count; i++)
                            {
                                if (minTime > actions[i].TapTime) minTime = actions[i].TapTime;
                                if (minOffset > actions[i].TapAccuracy) minOffset = actions[i].TapAccuracy;
                            }
                            if (actions.Count > 0)
                                writer.WriteLine("{0},{1},{2},{3},{4:.00}", participantName, deviceType, 0, minTime, minOffset);
                            else
                                writer.WriteLine("{0},{1},{2},,", participantName, deviceType, 0);
                        }
                }
            }
            Console.WriteLine("tap task ({0}): {1} records", deviceType == 1 ? "phone" : "tablet", count);
        }

        private void PrintCopyrightText()
        {
            Console.WriteLine(@"    Radu-Daniel Vatavu, Ph.D.
    University Stefan cel Mare of Suceava
    Suceava 720229, Romania
    vatavu@eed.usv.ro
 
 The academic publication for this work, and what should be used to cite it, is:
 
    Radu-Daniel Vatavu, Gabriel Cramariuc, Doina-Maria Schipor. (2015). 
    Touch Interaction for Children Aged 3 to 6 Years: Experimental Findings and Relationship to Motor Skills.
    International Journal of Human-Computer Studies 74. Elsevier, 54-76
    http://dx.doi.org/10.1016/j.ijhcs.2014.10.007
  
 This software is distributed under the New BSD License agreement:
 
 Copyright (c) 2014, Radu-Daniel Vatavu. All rights reserved.
 
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
     * Redistributions of source code must retain the above copyright
       notice, this list of conditions and the following disclaimer.
     * Redistributions in binary form must reproduce the above copyright
       notice, this list of conditions and the following disclaimer in the
       documentation and/or other materials provided with the distribution.
     * Neither the name of the University Stefan cel Mare of Suceava, 
       nor the names of its contributors may be used to endorse or promote products
       derived from this software without specific prior written permission.
 
 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS AS
 IS AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL Radu-Daniel Vatavu 
 BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT 
 OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.");
        }
    }
}
