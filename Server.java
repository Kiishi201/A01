

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


public final class Server {
    static CopyOnWriteArrayList<Bibliography> database = new CopyOnWriteArrayList<Bibliography>();// Multithread safe
    // arraylist of type
    // bibliogrpahy
	//static ArrayList<Bibliography> database = new ArrayList<Bibliography>();// Multithread safe

    public static void main(String argv[]) throws IOException {
        // Get the port number from the command line.

       
    	int port = Integer.parseInt(argv[0]);// port nuactmber extr
    	System.out.println("port: "+port);
        //int port=80;
        // Establish the listen socket.
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);// socket creation
            System.out.println("Socket created with port");
            //System.out.println(port);
        } catch (IOException e) {
            System.err.println("Port busy already"); // error message
        }

        // Process requests in an inf loop

        while (true) {
            // Listen for a TCP connection request.

            Socket connect = null;
            try {
                connect = serverSocket.accept();
                
            } catch (IOException e) {
                System.err.println("Accept failed.");// Error message
                System.out.println(e);
                System.exit(1);
            }
            System.out.println("Socket Accept success");

            // Construct an object to process the request message.
            Request req;
            try {
                req = new Server.Request(connect);
                //req = new Server().new Request(connect);
                System.out.println("Request success!");
                // Create a new thread to process the request.
                Thread thread = new Thread(req);
                System.out.println("Thread created");
            
                // Start the thread.
                thread.start();
                System.out.println("Thread ran");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                System.out.println("Request/thread creation failed: "+ e);
                e.printStackTrace();
            }
            
            
            //serverSocket.close();
        }
    }
    
         static class Request implements Runnable {
            static Socket socket;
            // Constructor
            public Request(Socket socketPassed) throws Exception {
            this.socket = socketPassed;
            System.out.println("Socket passed");
            }
            
            // Implement the run() method of the Runnable interface.
            public void run() {
            	System.out.println("Run() called");
                
                try {
                	InputStream is = socket.getInputStream();
            		//DataOutputStream os = new DataOutputStream(socket.getOutputStream());
                	PrintWriter os = new PrintWriter(socket.getOutputStream(), true);
            		BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    process(is, os, br);
                    while(!(socket.isClosed())) {
                    	process(is, os, br);
                    }
                    os.close();
                    br.close();
                    socket.close();
                } catch (Exception e) {
                    System.out.println(e);
                    e.printStackTrace();
                }
               
            }
            private static void process(InputStream is, PrintWriter os, BufferedReader br) throws Exception {
            // Get a reference to the socket's input and output streams.
			/*
			 * InputStream is = socket.getInputStream(); DataOutputStream os = new
			 * DataOutputStream(socket.getOutputStream());
			
            
            // Set up input stream filters.
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
             */
            String line;
            String requestType= br.readLine(); //get request type
            System.out.println("------Process() started------");
            System.out.println("Request type:"+requestType);
            ArrayList<Query> queries= new ArrayList<Query>(); //Array for the query to be stored
            	while(br.ready()) {
            		line = br.readLine().trim();
            		if(!(line.isEmpty())) {// Get message line
	                    Query temp=new Server.Query();//helper Query class with type and data
	                    String second="";//Helper string for data
	                    //StringTokenizer tokens = new StringTokenizer(line);//Split line received using tokens
	                    String[] tokens = line.split("\\s");
	                    temp.setType(tokens[0]); //Extract and set query type
	                    for(int x=1; x<tokens.length; x++) {
	                    	second+=tokens[x]+" ";
	                    }
	                    
	                    //temp.setType(tokens.nextToken()); 
						/*
						 * while(tokens.hasMoreTokens()) { second.concat(tokens.nextToken());// Extract
						 * query data System.out.println("Second concat: "+second); }
						 */
	                    
	                    temp.setData(second.trim());//set query data
	
	                    queries.add(temp);// Add to query array
            		}
                    
                }//Repeat for all lists received
                if(requestType.equalsIgnoreCase("SUBMIT")){// If req type is submit
                	System.out.println("SUBMIT entered");
                    Bibliography bibliography=new Bibliography(); //Bibliography class for storage of bibliography
                    for (Query query : queries){// iterate over every query in the req
                        query.setqueryInt();
                        int typeInt= query.returnqueryInt();//Biblio class methods to id query type
                        if(typeInt==0){
                            bibliography.setISBN(Long.valueOf(query.returnData()));//If query ISBN set bilbiography ISBN
                        }else if(typeInt==1){
                            bibliography.setTitle(query.returnData());//If query Title set bilbiography ISBN
                        }else if(typeInt==2){
                            bibliography.setAuthors(query.returnData());//If query Authors set bilbiography Authors
                        }else if(typeInt==3){
                            bibliography.setPublisher(query.returnData());//If query Publisher set bilbiography Publisher
                        }else if(typeInt==4){
                            bibliography.setYear(Integer.valueOf(query.returnData()));//If query year set bilbiography Year
                        }
                    }
                    if(!(Server.insertDatabase(bibliography))){//insert new bibliography into database
                        //send error message
                    	System.out.println("Submit not accepted");
                    }else{
                        System.out.println("Submit accepted");
                        Server.printDatabase();
                    }
                }
                
                else if(requestType.equalsIgnoreCase("UPDATE")){// If req type is submit
                	System.out.println("UPDATE entered");
                    Bibliography bibliography=new Bibliography();//Bibliography class for storage of bibliography
                    for (Query query : queries){// iterate over every query in the req
                        query.setqueryInt();
                        int typeInt= query.returnqueryInt();//Biblio class methods to id query type
                        if(typeInt==0){
                            bibliography.setISBN(Long.valueOf(query.returnData()));//If query ISBN set bilbiography ISBN
                        }else if(typeInt==1){
                            bibliography.setTitle(query.returnData());//If query Title set bilbiography ISBN
                        }else if(typeInt==2){
                            bibliography.setAuthors(query.returnData());//If query Authors set bilbiography Authors
                        }else if(typeInt==3){
                            bibliography.setPublisher(query.returnData());//If query Publisher set bilbiography Publisher
                        }else if(typeInt==4){
                            bibliography.setYear(Integer.valueOf(query.returnData()));//If query year set bilbiography Year
                        }
                    }
                    if(Server.updateDatabase(bibliography)){ //send bibliography object to update database method
                    	System.out.println("Update accepted");
                    	Server.printDatabase();
                    }else {
                    	System.out.println("UPDATE failed :(");
                    }
                    
                }

                else if(requestType.equalsIgnoreCase("GET")){
                	System.out.println("GET entered");
                    if (!(Server.databaseEmpty())){
                        ArrayList<Bibliography> response = new ArrayList<Bibliography>();//Arraylist of bibliography objects to store the matches of get methods
                        Bibliography bibliography=new Bibliography();//Bibliography class for storage of bibliography
                        for (Query query : queries){
                            query.setqueryInt();
                            int typeInt= query.returnqueryInt();
                            if (typeInt==5){
                                response=Server.getAll();//returns all entries in database
                                break;
                            }else if(typeInt==0){
                                bibliography.setISBN(Long.valueOf(query.returnData()));
                            }else if(typeInt==1){
                                bibliography.setTitle(query.returnData());
                            }else if(typeInt==2){
                                bibliography.setAuthors(query.returnData());
                            }else if(typeInt==3){
                                bibliography.setPublisher(query.returnData());
                            }else if(typeInt==4){
                                bibliography.setYear(Integer.valueOf(query.returnData()));
                            }
                        }
                        response=Server.get(bibliography);//Returns entries that match in the database
                        if(!(response.isEmpty())){
                        	System.out.println("Printing Get");
                        	os.println("WIPE");
                            for(Bibliography biblio : response){//For all the matches in the database
                            ArrayList<String> output=biblio.toarrayString();//returns bibliography as array list of lines to send to client
                                for (String outLine:output){//For array of lines
                                    //os.write(outLine);//Sends lines to client
                                	
                                	os.println(outLine);
                                    System.out.println(outLine);
                                }
                                os.println("SPACE");
                            }
                            
                        }else{
                            //os.write("No matches found");
                            os.println("No matches found");
                            System.out.println("No matches for GET");
                        }
                    }else{
                        //os.write("Database empty, get request invalid");
                        os.println("Database empty, get request invalid");
                        System.out.println("Database empty, get request invalid");
                    }
                }
                else if(requestType.equalsIgnoreCase("REMOVE")){
                	System.out.println("REMOVE entered");
                	if (!(Server.databaseEmpty())){
	                	Bibliography bibliography=new Bibliography();//Bibliography class for storage of bibliography
	                    for (Query query : queries){//Loop through queries
	                        query.setqueryInt();
	                        int typeInt= query.returnqueryInt();
	                        if (typeInt==5){
	                        	Server.removeallDatabase();//Removes all entries in database
	                            break;
	                        }else if(typeInt==0){
	                            bibliography.setISBN(Long.valueOf(query.returnData()));
	                        }else if(typeInt==1){
	                            bibliography.setTitle(query.returnData());
	                        }else if(typeInt==2){
	                            bibliography.setAuthors(query.returnData());
	                        }else if(typeInt==3){
	                            bibliography.setPublisher(query.returnData());
	                        }else if(typeInt==4){
	                            bibliography.setYear(Integer.valueOf(query.returnData()));
	                        }
	                    }
	                    if(Server.removeDatabase(bibliography)){//removes entires that match the query from the database
	                    	System.out.println("Removing from database");
	                    	Server.printDatabase();
	                    }else {
	                    	os.println("No matches found to remove");
                            System.out.println("No matches for REMOVE");
	                    }
                	}else{
                        os.println("Database empty, get request invalid");
                        System.out.println("Database empty, get request invalid");
                    }
                }
                System.out.println("End of SUB, UPDATE, GET or REMOVE options");
                
                if(!(socket.isConnected())) {
                	
                    System.out.println("Streams and sockets closed");
                }
                
                System.out.println("Process done");
            }
        }
        public static class Bibliography{//Helper Bibliography class that helps with storing of all book entries
                
            Long ISBN;//Class has var for each query type
            String title;
            ArrayList<String> authors=new ArrayList<String>();
            String publisher;
            Integer year;
    
            //Override compare
            @Override
            public boolean equals(Object o) {
                
                Boolean equalsRelative=true;
                // If the object is compared with itself then return true   
                if (o == this) { 
                    return true; 
                } 
        
                /* Check if o is an instance of Biblio or not 
                "null instanceof [type]" also returns false */
                if (!(o instanceof Bibliography)) { 
                    return false; 
                } 
                
                // typecast o to Biblio so that we can compare data members  
                Bibliography that = (Bibliography) o;
                
                // Compare the data members and return accordingly  
                if ((that.getTitle()!=null && this.getTitle()!=null ) &&  !((that.getTitle()).equalsIgnoreCase(this.title))){
                    equalsRelative=false;
                }if ((that.getISBN()!=null && this.getISBN()!=null) && (!(that.getISBN().equals(this.ISBN)))){
                    equalsRelative=false;
                }
                if (((that.getAuthors()) != null && !(that.getAuthors().isEmpty())) && (this.getAuthors()) != null && !(this.getAuthors().isEmpty())){ 
                    for (String oAuthors: that.getAuthors()){
                        if(!(this.authors.contains(oAuthors))){
                            equalsRelative=false;
                        }
                    }
                }
                if ((that.getPublisher()!=null && this.getPublisher()!=null) && !((that.getPublisher()).equalsIgnoreCase(this.publisher))){
                    equalsRelative=false;
                }
                if ((that.getYear()!=null && this.getYear()!=null) && (!((that.getYear()).equals(this.year)))){
                    equalsRelative=false;
                }
                return equalsRelative;
            } 
            //Helper methods for bibliography functionality
            public void setISBN(Long iSBN) {
                ISBN = iSBN;
            }
            public void setTitle(String title) {
                this.title = title;
            }
            public void setPublisher(String publisher) {
                this.publisher = publisher;
            }
            public void setAuthors(String authorsString) {//Takes string a splits it into arrayList
                for (String i: authorsString.split(",")){
                    this.authors.add(i);
                }
                //this.authors=Arrays.asList(authorsString.split(","));
            }
            public void setYear(int year) {
                this.year = year;
            }
            public Long getISBN() {
                return ISBN;
            }
            public String getTitle() {
                return title;
            }
            public String getPublisher() {
                return publisher;
            }
            public ArrayList<String> getAuthors() {
                return authors;
            }
            public Integer getYear() {
                return year;
            }
            public ArrayList<String> toarrayString(){//Method to return bibliography as arraylist of strings to facilitate output to client
                ArrayList<String> output= new ArrayList<String>();
                if(this.ISBN!=null){
                    output.add("ISBN ".concat(Long.toString(this.ISBN)));
                }
                if(this.title!=null){
                    output.add("TITLE ".concat(this.title));
                }
                if(this.authors != null && !this.authors.isEmpty()){
                    String authorsString=String.join(",",this.authors);
                    output.add("AUTHOR ".concat(authorsString));
                }
                if(this.publisher!=null){
                    output.add("PUBLISHER ".concat(this.publisher));
                }
                if(this.year != null){
                    output.add("YEAR ".concat(Integer.toString(this.year)));
                }
                return output;
            }
            public void addAuthors(String author){//helper methods for update functionality
                this.authors.add(author);
            }
        }
        public static class Query{//class to help process incoming input from client
            String queryType;//First word of incoming input
            String data;//Data from line
            int querytypeInt;//private var to help for ID of query
            
            public void setqueryInt(){//Helper method for ID of query type: ISBN, Title, Authors, Publisher, Year or ALL
                if(queryType.equalsIgnoreCase("ALL")){//ID's query type with an INT for easy arithmetic comparison of Queries
                    querytypeInt=5;
                }else if(queryType.equalsIgnoreCase("ISBN")){
                    querytypeInt=0;
                }else if(queryType.equalsIgnoreCase("TITLE")){
                    querytypeInt=1;
                }else if(queryType.equalsIgnoreCase("AUTHOR")){
                    querytypeInt=2;
                }else if(queryType.equalsIgnoreCase("PUBLISHER")){
                    querytypeInt=3;
                }else if(queryType.equalsIgnoreCase("YEAR")){
                    querytypeInt=4;
                }
            }
            public int returnqueryInt(){//return query type
                return this.querytypeInt;
            }
            public String returnData(){//return query data
                return this.data;
            }
            public void setType(String type){//set query type
                this.queryType=type;
            }
            public void setData(String queryData){//set query data
                this.data=queryData;
            }
        }
        public static boolean insertDatabase(Bibliography biblio){//Adds bibliography to database
            boolean insert=false;
            if(!(database.contains(biblio))){//Check if database contains already
                database.add(biblio);
                insert=true;
            }
            return insert;//returns success of insertion process
        }
        public static ArrayList<Bibliography> get(Bibliography biblio){
                return searchDatabase(biblio);
        }
        public static ArrayList<Bibliography> searchDatabase(Bibliography biblio){//Method to search database 
            ArrayList<Bibliography> found= new ArrayList<Bibliography>();//returns arraylist of bibliography objects
                for (Bibliography data: database){//Iterate over database to check entries
                    if (data.equals(biblio)){
                        found.add(data);//add to arraylist being returned
                    }
                }
                return found;//return arraylist
            }
        public static boolean removeDatabase(Bibliography biblio){//Method to remove from database
            boolean remove=false;//returns boolean value with success of removal
                for (Bibliography data: database){//iterate over database to check entries
                    if (data.equals(biblio)){
                        database.remove(data);//remove from database
                        remove=true;
                    }
                }
                return remove;//return success of removal
            }
        
            public static boolean removeallDatabase(){//Method to clear database
                ArrayList<Bibliography> collect=new ArrayList<Bibliography>();//with an thread safe array list the fastest way to remove is to copy into another array and them remove all in one go
                boolean remove=false;//var for success of removal
                for (Bibliography data: database){
                   collect.add(data);
                }//copy values to new array
                try{
                    database.removeAll(collect);//removeall
                    remove=true;
                }catch (Exception e){
                    remove=false;
                }
                return remove;//return success of removal
            }
            public static ArrayList<Bibliography> getAll(){//Method to return all entries in database
                ArrayList<Bibliography> all= new ArrayList<Bibliography>();//arraylist to return all entries in database
                for (Bibliography data: database){
                        all.add(data);
                }
                return all;//return all entries in database
            }
            public static boolean updateDatabase(Bibliography biblio){
                boolean updated=false;
                for (Bibliography data: database){
                    if (data.getISBN().equals((biblio.getISBN()))){
                        if (biblio.getTitle() != null && !(biblio.getTitle()).trim().isEmpty()){ data.setTitle(biblio.getTitle());} 
                        if (biblio.getPublisher() != null && !(biblio.getPublisher()).trim().isEmpty()){ data.setPublisher(biblio.getPublisher());} 
                        if (biblio.getYear()!=null){ data.setYear(biblio.getYear());}
                        //data.setAuthors(authorsString);
                        if (biblio.getAuthors() != null && !biblio.getAuthors().isEmpty()){
                        		for (String authors: biblio.getAuthors()){
                            data.addAuthors(authors);//Might want to replace authors, asking proffessor
                        		}
                        }
                        updated=true;
                    }
                }
                return updated;
            }
            public static boolean databaseEmpty(){
                if(Server.database.isEmpty()){
                    return true;
                }else {
                    return false;
                }
            }
            public static void printDatabase(){
                for(Bibliography biblio: Server.getAll()){
                    for (String printing: biblio.toarrayString()){
                        System.out.println(printing);
                    }
                    System.out.println("\n");
                }
            }
    }



