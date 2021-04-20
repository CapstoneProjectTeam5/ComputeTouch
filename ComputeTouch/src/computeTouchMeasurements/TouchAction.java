package computeTouchMeasurements;

import java.util.ArrayList;
import java.util.List;


public class TouchAction {
	public TouchActionType Type;
	
	public enum TouchActionType {
		None, Tap, DoubleTap, SingleTouchDragAndDrop, MultiTouchDragAndDrop
	}
	
	//  action type, e.g., tap
	public List<Stroke> Strokes;

	//  strokes, may be 1 or 2 strokes for our experiment
	public Point TargetPoint;

	//  target point for tap and double tap
	public List<Point> StartPoints;

	//  start point(s) for single-touch and multi-touch drag and drop
	public List<Point> StopPoints;

	//  stop point(s) for single-touch and multi-touch drag and drop
	public TouchAction() {
		this.Strokes = new ArrayList<>();
		this.StartPoints = new ArrayList<>();
		this.StopPoints = new ArrayList<>();
	}
	
	// region "multi-touch drag and drop measurements"
//	public final Stroke FirstStroke = this.Strokes.get(0);
//	public final Stroke SecondStroke() = this.Strokes.get(1);
	
	public final Stroke FirstStroke() {
		return this.Strokes.get(0);
	}
	
	public final Stroke SecondStroke() {
		return this.Strokes.get(1);
	}
	
	public final double MultiTouchDragAndDropTime() {
	
		return Math.max(FirstStroke().get(FirstStroke().size() - 1).T, SecondStroke().get(SecondStroke().size() - 1).T);		
	}

	///  <summary>
	///  Computes the accuracy of the multi-touch task (the starting point perspective, first stroke).
	///  </summary>
	public final double MultiTouchDragAndDropAccuracy1() {
		return this.EuclideanDistance(FirstStroke().get(0), this.StartPoints.get(0));
	}

	///  <summary>
	///  Computes the accuracy of the multi-touch task (the ending point perspective, first stroke).
	///  </summary>
	public final double MultiTouchDragAndDropAccuracy2() {
		return this.EuclideanDistance(FirstStroke().get(FirstStroke().size() - 1), this.StopPoints.get(0));
	}

	///  <summary>
	///  Computes the accuracy of the multi-touch task (the starting point perspective, second stroke).
	///  </summary>        
	public final double MultiTouchDragAndDropAccuracy3() {
		return this.EuclideanDistance(SecondStroke().get(0), this.StartPoints.get(1));
	}

	///  <summary>
	///  Computes the accuracy of the multi-touch task (the ending point perspective, second stroke).
	///  </summary>
	public final double MultiTouchDragAndDropAccuracy4() {
		return this.EuclideanDistance(SecondStroke().get(SecondStroke().size() - 1), this.StopPoints.get(1));
	}

	///  <summary>
	///  Computes the overall accuracy of the multi-touch task.
	///  </summary>
	public final double MultiTouchDragAndDropAccuracy() {
		return 0.25 * (this.MultiTouchDragAndDropAccuracy1() + this.MultiTouchDragAndDropAccuracy2() + this.MultiTouchDragAndDropAccuracy3() + this.MultiTouchDragAndDropAccuracy4());
	}

	///  <summary>
	///  Computes the path accuracy of the multi-touch task (first stroke).
	///  </summary>
	public final double MultiTouchDragAndDropPathAccuracy1 (){
		double pathLength = 0;
		for (int i = 0; i < (FirstStroke().size() - 1); i++) {
			pathLength = (pathLength + this.EuclideanDistance(FirstStroke().get(i), FirstStroke().get(i+1)));
		}

		return (this.EuclideanDistance(FirstStroke().get(0), FirstStroke().get(FirstStroke().size() - 1)) / pathLength);
	}

	///  <summary>
	///  Computes the path accuracy of the multi-touch task (second stroke).
	///  </summary>
	public final double MultiTouchDragAndDropPathAccuracy2() {
		double pathLength = 0;
		for (int i = 0; i < (SecondStroke().size() - 1); i++) {
			pathLength += this.EuclideanDistance(SecondStroke().get(i), SecondStroke().get(i+1));
		}

		return this.EuclideanDistance(SecondStroke().get(0), SecondStroke().get(SecondStroke().size()-1)) / pathLength;
	}

	///  <summary>
	///  Computes the overall path accuracy of the multi-touch task.
	///  </summary>
	public final double MultiTouchDragAndDropPathAccuracy() {
		return (0.5 * (this.MultiTouchDragAndDropPathAccuracy1() + this.MultiTouchDragAndDropPathAccuracy2()));
	}
	//endregion
	//region "single-touch drag and drop measurements"

	public final double SingleTouchDragAndDropTime() {
		return FirstStroke().get(FirstStroke().size() - 1).T;
	}

	///  <summary>
	///  Computes the accuracy of the single-touch drag and drop task (the starting point perspective).
	///  </summary>
	public final double SingleTouchDragAndDropAccuracy1() {
		return this.EuclideanDistance(FirstStroke().get(0), this.StartPoints.get(0));
	}

	///  <summary>
	///  Computes the accuracy of the single-touch drag and drop task (the ending point perspective).
	///  </summary>
	public final double SingleTouchDragAndDropAccuracy2 (){
		return this.EuclideanDistance(FirstStroke().get(FirstStroke().size() - 1), this.StopPoints.get(0));
	}

	///  <summary>
	///  Computes the overall accuracy of the single-touch drag and drop task.
	///  </summary>
	public final double SingleTouchDragAndDropAccuracy() {
		return 0.5 * (this.SingleTouchDragAndDropAccuracy1() + this.SingleTouchDragAndDropAccuracy2());
	}

	///  <summary>
	///  Computes the overall path accuracy of the single-touch drag and drop task.
	///  </summary>
	public final double SingleTouchDragAndDropPathAccuracy() {
		double pathLength = 0;
		for (int i = 0; i < (FirstStroke().size() - 1); i++) {
			pathLength += this.EuclideanDistance(FirstStroke().get(i), FirstStroke().get(i+1));
		}

		return (this.EuclideanDistance(FirstStroke().get(0), FirstStroke().get(FirstStroke().size() - 1)) / pathLength);
	}
	//endregion
	//region "double tap measurements"

	public final double DoubleTapTime_FirstTap (){
		return FirstStroke().get(FirstStroke().size() - 1).T;
	}

	///  <summary>
	///  Computes the time of the second tap.
	///  </summary>
	public final double DoubleTapTime_SecondTap (){
		return (SecondStroke().get(SecondStroke().size() - 1).T - SecondStroke().get(0).T);
	}

	///  <summary>
	///  Computes the time between taps.
	///  </summary>
	public final double DoubleTapTime_InBetweenTaps() {
		return (SecondStroke().get(0).T - FirstStroke().get(FirstStroke().size() - 1).T);
	}

	///  <summary>
	///  Computes the overal time of the double tap task.
	///  </summary>
	public final double DoubleTapTime() {
		return SecondStroke().get(SecondStroke().size() - 1).T;
	}

	///  <summary>
	///  Computes the accuracy of the first tap.
	///  </summary>
	public final double DoubleTapAccuracy_FirstTap() {
		return this.EuclideanDistance(FirstStroke().get(FirstStroke().size() - 1), this.TargetPoint);
	}

	///  <summary>
	///  Computes the accuracy of the second tap.
	///  </summary>        
	public final double DoubleTapAccuracy_SecondTap() {
		return this.EuclideanDistance(SecondStroke().get(SecondStroke().size() - 1), this.TargetPoint);
	}

	///  <summary>
	///  Computes the overall accuracy of the double tap task.
	///  </summary>
	public final double DoubleTapAccuracy() {
		return (0.5 * (this.DoubleTapAccuracy_FirstTap() + this.DoubleTapAccuracy_SecondTap()));
	}
	//endregion
	//region "tap task measurements"

	public final double TapTime (){
		return FirstStroke().get(FirstStroke().size() - 1).T;
	}

	///  <summary>
	///  Computes the accuracy of the tap as the distance from the center of the target.
	///  </summary>
	public final double TapAccuracy() {
		return this.EuclideanDistance(FirstStroke().get(FirstStroke().size() - 1), this.TargetPoint);
	}
	//endregion
	//region "assisting functions"

	private final double EuclideanDistance(Point a, Point b) {
		return Math.sqrt((((a.X - b.X) * (a.X - b.X)) + ((a.Y - b.Y) * (a.Y - b.Y))));
	}

	///  <summary>
	///  Returns the first stroke of this touch input action.
	///  </summary>
	

	// endregion
}
