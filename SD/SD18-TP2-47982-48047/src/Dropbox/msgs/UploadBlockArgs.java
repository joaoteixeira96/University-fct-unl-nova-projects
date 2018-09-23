package Dropbox.msgs;

public class UploadBlockArgs {
	
	final String path;
	final String mode;
	final boolean autorename;
	final boolean mute;
	
	public UploadBlockArgs(String path, String mode, boolean autorename, boolean mute) {
		this.path = path;
		this.mode = mode;
		this.autorename = autorename;
		this.mute = mute;
	}

}
