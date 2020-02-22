package utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class AlternativeConsole {

	private PrintStream originalOut,originalErr;
	
	private ByteArrayOutputStream out,err;
	private boolean outClosed, errClosed;

	public AlternativeConsole() {
		// out
		this.originalOut = System.out;
		this.out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));
		this.outClosed = false;
		// err
		this.originalErr = System.err;
		this.err = new ByteArrayOutputStream();
		System.setErr( new PrintStream(err));
		this.errClosed = false;
	}
	
	public void reset() {
    	try {
			out.flush();
			out.close();
	    	System.setOut(originalOut);
			this.outClosed = true;
	    	err.flush();
	    	err.close();
	    	System.setErr(originalErr);
			this.errClosed = true;
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
	}

	public String getOutString() {
		if(outClosed) {
			throw new RuntimeException("Output print stream is already closed");
		}
		return out.toString();
	}
	public String getErrString() {
		if(errClosed) {
			throw new RuntimeException("Error print stream is already closed");
		}
		return err.toString();
	}
	
}
