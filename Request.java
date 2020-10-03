import java.io.* ;
import java.net.* ;
import java.util.* ;

final class Request implements Runnable {
    final static String CRLF = "\r\n";
    Socket socket;
    
    // Constructor
    public Request(Socket socket) throws Exception {
	this.socket = socket;
    }
    
    // Implement the run() method of the Runnable interface.
    public void run() {
	try {
	    process();
	} catch (Exception e) {
	    System.out.println(e);
	}
    }
    private void process() throws Exception {
	// Get a reference to the socket's input and output streams.
	InputStream is = socket.getInputStream();
	DataOutputStream os = new DataOutputStream(socket.getOutputStream());
	
	// Set up input stream filters.
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    String line;
    String requestType= br.readLine();
    ArrayList<Query> queries= new Arraylist<Query>();
        while ((br.readLine()) != null) {
            // Get message line
            line = br.readLine();

            // Extract the filename from the request line.
            StringTokenizer tokens = new StringTokenizer(line);
            
            requestType=tokens.nextToken(); //Extract req type
            String ISBN;
            ISBN=tokens.nextToken();
        }
        
        //Code extraction of all the SQL fields into strings
        
        //String fileName = tokens.nextToken();
	
        // Prepend a "." so that file request is within the current directory.
        //fileName = "." + fileName ;
	
	/* // Open the requested file.
        FileInputStream fis = null ;
        boolean fileExists = true ;
        try {
	    fis = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
	    fileExists = false ;
        } */

	// Debug info for private use
	System.out.println("Incoming!!!");
	System.out.println(firstLine);
	String headerLine = null;
	while ((headerLine = br.readLine()).length() != 0) {
	    System.out.println(headerLine);
	}
	
        os.close();
        br.close();
        socket.close();
    }
    private class Query{
        String queryType;
        String data;
    }
}