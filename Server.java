import java.io.* ;
import java.net.* ;
import java.util.* ;

public final class Server {
    public static void main(String argv[]) throws IOException {
	// Get the port number from the command line.

    ArrayList<Bibliography> database=new ArrayList<Bibliography>();


    int port = new Integer(argv[0]).intValue();

	// Establish the listen socket.
	ServerSocket serverSocket = null;
        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            System.exit(1);
        }
	
	// Process  requests in an inf loop
    
	while (true) {
	    // Listen for a TCP connection request.
	    
        Socket connect = null;
        try {
            connect = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
	    
	    // Construct an object to process the request message.
        Request req= new Request(connect);
	    
	    // Create a new thread to process the request.
	    Thread thread = new Thread(req);
	    
	    // Start the thread.
	    thread.start();
	}
    }
    private class Bibliography{
        int ISBN;
        String title;
        ArrayList<String> authors=new ArrayList<String>;
        
        String publisher;
        int year;

    }
    
}

