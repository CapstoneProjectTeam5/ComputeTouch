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

namespace ComputeTouchMeasurements
{
    public enum TouchActionType
    {
        None = 0,
        Tap = 1,
        DoubleTap = 2,
        SingleTouchDragAndDrop = 3,
        MultiTouchDragAndDrop = 4
    };

    public class TouchAction
    {
        public TouchActionType Type;     // action type, e.g., tap
        public List<Stroke> Strokes;     // strokes, may be 1 or 2 strokes for our experiment
        public Point TargetPoint;        // target point for tap and double tap
        public List<Point> StartPoints;  // start point(s) for single-touch and multi-touch drag and drop
        public List<Point> StopPoints;   // stop point(s) for single-touch and multi-touch drag and drop

        public TouchAction()
        {
            Strokes = new List<Stroke>();
            StartPoints = new List<Point>();
            StopPoints = new List<Point>();
        }

        #region multi-touch drag and drop measurements

        /// <summary>
        /// Computes the time of the multi-touch drag and drop task.
        /// </summary>
        public int MultiTouchDragAndDropTime
        {
            get
            {
                return Math.Max(FirstStroke[FirstStroke.Count - 1].T, SecondStroke[SecondStroke.Count - 1].T);
            }
        }

        /// <summary>
        /// Computes the accuracy of the multi-touch task (the starting point perspective, first stroke).
        /// </summary>
        public double MultiTouchDragAndDropAccuracy1
        {
            get
            {
                return EuclideanDistance(FirstStroke[0], StartPoints[0]);
            }
        }

        /// <summary>
        /// Computes the accuracy of the multi-touch task (the ending point perspective, first stroke).
        /// </summary>
        public double MultiTouchDragAndDropAccuracy2
        {
            get
            {
                return EuclideanDistance(FirstStroke[FirstStroke.Count - 1], StopPoints[0]);
            }
        }

        /// <summary>
        /// Computes the accuracy of the multi-touch task (the starting point perspective, second stroke).
        /// </summary>        
        public double MultiTouchDragAndDropAccuracy3
        {
            get
            {
                return EuclideanDistance(SecondStroke[0], StartPoints[1]);
            }
        }

        /// <summary>
        /// Computes the accuracy of the multi-touch task (the ending point perspective, second stroke).
        /// </summary>
        public double MultiTouchDragAndDropAccuracy4
        {
            get
            {
                return EuclideanDistance(SecondStroke[SecondStroke.Count - 1], StopPoints[1]);
            }
        }

        /// <summary>
        /// Computes the overall accuracy of the multi-touch task.
        /// </summary>
        public double MultiTouchDragAndDropAccuracy
        {
            get
            {
                return 0.25 * (MultiTouchDragAndDropAccuracy1 + MultiTouchDragAndDropAccuracy2 + MultiTouchDragAndDropAccuracy3 + MultiTouchDragAndDropAccuracy4);
            }
        }

        /// <summary>
        /// Computes the path accuracy of the multi-touch task (first stroke).
        /// </summary>
        public double MultiTouchDragAndDropPathAccuracy1
        {
            get
            {
                double pathLength = 0;
                for (int i = 0; i < FirstStroke.Count - 1; i++)
                    pathLength += EuclideanDistance(FirstStroke[i], FirstStroke[i + 1]);
                return EuclideanDistance(FirstStroke[0], FirstStroke[FirstStroke.Count - 1]) / pathLength;
            }
        }

        /// <summary>
        /// Computes the path accuracy of the multi-touch task (second stroke).
        /// </summary>
        public double MultiTouchDragAndDropPathAccuracy2
        {
            get
            {
                double pathLength = 0;
                for (int i = 0; i < SecondStroke.Count - 1; i++)
                    pathLength += EuclideanDistance(SecondStroke[i], SecondStroke[i + 1]);
                return EuclideanDistance(SecondStroke[0], SecondStroke[SecondStroke.Count - 1]) / pathLength;
            }
        }

        /// <summary>
        /// Computes the overall path accuracy of the multi-touch task.
        /// </summary>
        public double MultiTouchDragAndDropPathAccuracy
        {
            get
            {
                return 0.5 * (MultiTouchDragAndDropPathAccuracy1 + MultiTouchDragAndDropPathAccuracy2);
            }
        }

        #endregion

        #region single-touch drag and drop measurements

        /// <summary>
        /// Computes the time of the single-touch drag and drop task.
        /// </summary>
        public int SingleTouchDragAndDropTime
        {
            get
            {
                return FirstStroke[FirstStroke.Count - 1].T;
            }
        }

        /// <summary>
        /// Computes the accuracy of the single-touch drag and drop task (the starting point perspective).
        /// </summary>
        public double SingleTouchDragAndDropAccuracy1
        {
            get
            {
                return EuclideanDistance(FirstStroke[0], StartPoints[0]);
            }
        }

        /// <summary>
        /// Computes the accuracy of the single-touch drag and drop task (the ending point perspective).
        /// </summary>
        public double SingleTouchDragAndDropAccuracy2
        {
            get
            {
                return EuclideanDistance(FirstStroke[FirstStroke.Count - 1], StopPoints[0]);
            }
        }

        /// <summary>
        /// Computes the overall accuracy of the single-touch drag and drop task.
        /// </summary>
        public double SingleTouchDragAndDropAccuracy
        {
            get
            {
                return 0.5 * (SingleTouchDragAndDropAccuracy1 + SingleTouchDragAndDropAccuracy2);
            }
        }

        /// <summary>
        /// Computes the overall path accuracy of the single-touch drag and drop task.
        /// </summary>
        public double SingleTouchDragAndDropPathAccuracy
        {
            get
            {
                double pathLength = 0;
                for (int i = 0; i < FirstStroke.Count - 1; i++)
                    pathLength += EuclideanDistance(FirstStroke[i], FirstStroke[i + 1]);
                return EuclideanDistance(FirstStroke[0], FirstStroke[FirstStroke.Count - 1]) / pathLength;
            }
        }

        #endregion

        #region double tap measurements

        /// <summary>
        /// Computes the time of the first tap.
        /// </summary>
        public int DoubleTapTime_FirstTap
        {
            get
            {
                return FirstStroke[FirstStroke.Count - 1].T;
            }
        }

        /// <summary>
        /// Computes the time of the second tap.
        /// </summary>
        public int DoubleTapTime_SecondTap
        {
            get
            {
                return SecondStroke[SecondStroke.Count - 1].T - SecondStroke[0].T;
            }
        }

        /// <summary>
        /// Computes the time between taps.
        /// </summary>
        public int DoubleTapTime_InBetweenTaps
        {
            get
            {
                return SecondStroke[0].T - FirstStroke[FirstStroke.Count - 1].T;
            }
        }

        /// <summary>
        /// Computes the overal time of the double tap task.
        /// </summary>
        public int DoubleTapTime
        {
            get
            {
                return SecondStroke[SecondStroke.Count - 1].T;
            }
        }

        /// <summary>
        /// Computes the accuracy of the first tap.
        /// </summary>
        public double DoubleTapAccuracy_FirstTap
        {
            get
            {
                return EuclideanDistance(FirstStroke[FirstStroke.Count - 1], TargetPoint);
            }
        }

        /// <summary>
        /// Computes the accuracy of the second tap.
        /// </summary>        
        public double DoubleTapAccuracy_SecondTap
        {
            get
            {
                return EuclideanDistance(SecondStroke[SecondStroke.Count - 1], TargetPoint);
            }
        }

        /// <summary>
        /// Computes the overal accuracy of the double tap task.
        /// </summary>
        public double DoubleTapAccuracy
        {
            get
            {
                return 0.5 * (DoubleTapAccuracy_FirstTap + DoubleTapAccuracy_SecondTap);
            }
        }

        #endregion

        #region tap task measurements

        /// <summary>
        /// Computes the time of the tap.
        /// </summary>
        public int TapTime
        {
            get
            {
                return FirstStroke[FirstStroke.Count - 1].T;
            }
        }

        /// <summary>
        /// Computes the accuracy of the tap as the distance from the center of the target.
        /// </summary>
        public double TapAccuracy
        {
            get
            {
                return EuclideanDistance(FirstStroke[FirstStroke.Count - 1], TargetPoint);
            }
        }

        #endregion

        #region assisting functions

        /// <summary>
        /// Computes the Euclidean distance between two points in the plane.
        /// </summary>
        private double EuclideanDistance(Point a, Point b)
        {
            return Math.Sqrt((a.X - b.X) * (a.X - b.X) + (a.Y - b.Y) * (a.Y - b.Y));
        }

        /// <summary>
        /// Returns the first stroke of this touch input action.
        /// </summary>
        private Stroke FirstStroke
        {
            get
            {
                return Strokes[0];
            }
        }

        /// <summary>
        /// Returns the second stroke of this touch input action.
        /// </summary>
        private Stroke SecondStroke
        {
            get
            {
                return Strokes[1];
            }
        }

        #endregion
    }
}
