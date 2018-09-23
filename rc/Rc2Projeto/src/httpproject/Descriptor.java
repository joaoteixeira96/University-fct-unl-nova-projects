package httpproject;
import java.util.ArrayList;
import java.util.List;

public class Descriptor {
	private int movieTracks;
	private int segmentDuration;
	private List<Track> tracks;
	
	public Descriptor(int movieTracks,int segmentDuration) {
		this.movieTracks = movieTracks;
		this.segmentDuration = segmentDuration;
		this.tracks = new ArrayList<>();
	}


	public int getMovieTracks() {
		return movieTracks;
	}


	public void setMovieTracks(int movieTracks) {
		this.movieTracks = movieTracks;
	}


	public int getSegmentDuration() {
		return segmentDuration;
	}


	public void setSegmentDuration(int segmentDuration) {
		this.segmentDuration = segmentDuration;
	}

	public void addTrack(Track track) {
		this.tracks.add(track);
	}
	public List<Track> getTrack() {
		return tracks;
	}
	
	public Track getTrackByIndex(int index) {
		return tracks.get(index);
	}
}
