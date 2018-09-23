package httpproject;
import java.util.ArrayList;
import java.util.List;

public class Track {
	private int videoTracker;
	private String contentType;
	private int avgBandwidth;
	private List<Segment> segments;
	
	public Track(int videoTracker,String contentType, int avgBandwidth) {
		this.videoTracker = videoTracker;
		this.contentType = contentType;
		this.avgBandwidth = avgBandwidth;
		this.segments = new ArrayList<>();
	}
	
	public int getVideoTracker() {
		return videoTracker;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public int getAvgBandwidth() {
		return avgBandwidth;
	}
	public void addSegment(Segment segment) {
		segments.add(segment);
	}
	public Segment getSegmentByIndex(int index) {
		return segments.get(index);
	}

}
