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
using System.Collections.Generic;
using System.IO;
using System.Xml;

namespace ComputeTouchMeasurements
{
    public class TouchIO
    {
        /// <summary>
        /// Reads an XML file and returns the tasks as a list of TouchActions.
        /// </summary>
        public static List<TouchAction> ReadFromFile(string fileName)
        {
            List<TouchAction> touchActions = new List<TouchAction>();
            using (XmlTextReader reader = new XmlTextReader(File.OpenText(fileName)))
            {
                TouchAction action = new TouchAction();
                Stroke stroke = new Stroke();
                while (reader.Read())
                {
                    switch (reader.Name.ToUpper())
                    {
                        case "TOUCH":
                            if (reader.NodeType == XmlNodeType.Element)
                            {
                                action = new TouchAction();
                                action.Type = TextToActionType(reader["Type"]);
                                if (action.Type == TouchActionType.Tap || action.Type == TouchActionType.DoubleTap)
                                    action.TargetPoint = new Point() { X = int.Parse(reader["TargetX"]), Y = int.Parse(reader["TargetY"]) };
                                if (action.Type == TouchActionType.SingleTouchDragAndDrop)
                                {
                                    action.StartPoints.Add(new Point() { X = int.Parse(reader["StartTargetX"]), Y = int.Parse(reader["StartTargetY"]) });
                                    action.StopPoints.Add(new Point() { X = int.Parse(reader["StopTargetX"]), Y = int.Parse(reader["StopTargetY"]) });
                                }
                                if (action.Type == TouchActionType.MultiTouchDragAndDrop)
                                {
                                    action.StartPoints.Add(new Point() { X = int.Parse(reader["StartTargetX1"]), Y = int.Parse(reader["StartTargetY1"]) });
                                    action.StartPoints.Add(new Point() { X = int.Parse(reader["StartTargetX2"]), Y = int.Parse(reader["StartTargetY2"]) });
                                    action.StopPoints.Add(new Point() { X = int.Parse(reader["StopTargetX1"]), Y = int.Parse(reader["StopTargetY1"]) });
                                    action.StopPoints.Add(new Point() { X = int.Parse(reader["StopTargetX2"]), Y = int.Parse(reader["StopTargetY2"]) });
                                }
                            }
                            else
                                if (reader.NodeType == XmlNodeType.EndElement)
                                    touchActions.Add(action);
                            break;
                        case "STROKE":
                            if (reader.NodeType == XmlNodeType.Element)
                                stroke = new Stroke();
                            else
                                if (reader.NodeType == XmlNodeType.EndElement)
                                    action.Strokes.Add(stroke);
                            break;
                        case "POINT":
                            stroke.Add(new Point() { X = int.Parse(reader["X"]), Y = int.Parse(reader["Y"]), T = int.Parse(reader["T"]) });
                            break;
                    }
                }
            }
            return touchActions;
        }

        /// <summary>
        /// Converts task name into task id.
        /// </summary>
        private static TouchActionType TextToActionType(string text)
        {
            switch (text)
            {
                case "tap": return TouchActionType.Tap;
                case "doubletap": return TouchActionType.DoubleTap;
                case "singletouch-draganddrop": return TouchActionType.SingleTouchDragAndDrop;
                case "multitouch-draganddrop": return TouchActionType.MultiTouchDragAndDrop;
            }
            return TouchActionType.None;
        }
    }
}
