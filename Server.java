import java.io.* ;
import java.net.* ;
import java.util.* ;

public final class Server {
    public static void main(String argv[]) throws IOException {
	// Get the port number from the command line.

    CopyOnWriteArrayList<Bibliography> database=new CopyOnWriteArrayList<Bibliography>();//Multithread safe arraylist of type bibliogrpahy


    int port = new Integer(argv[0]).intValue();//port nuactmber extr

	// Establish the listen socket.
	ServerSocket serverSocket = null;
        try {
            socket = new ServerSocket(port);//socket creation
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444."); //error message
            System.exit(1);
        }
	
	// Process  requests in an inf loop
    
	while (true) {
	    // Listen for a TCP connection request.
	    
        Socket connect = null;
        try {
            connect = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");//Error message
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
            String requestType= br.readLine(); //get request type
            
            ArrayList<Query> queries= new Arraylist<Query>(); //Array for the query to be stored
                while (line=(br.readLine()) != null) {// Get message line
                    
                    Query temp=new Query();//helper Query class with type and data
                    String second="";//Helper string for data
                    StringTokenizer tokens = new StringTokenizer(line);//Split line received using tokens
                    
                    temp.setType(tokens.nextToken()); //Extract and set query type
                    while(tokens.hasMoreTokens()) {
                        second.concat(tokens.nextToken());// Extract query data
                     }
                    temp.setData(second);//set query data
                    queries.add(temp);// Add to query array
                }//Repeat for all lists received

                if(requestType.equalsIgnoreCase("SUBMIT")){// If req type is submit
                    Bibliography bibliography=new Bibliography(); //Bibliography class for storage of bibliography
                    for (Query query : queries){// iterate over every query in the req
                        query.setqueryInt();
                        int typeInt= query.returnqueryInt();//Biblio class methods to id query type
                        if(typeInt==0){
                            bibliography.setISBN(Integer.parseInt(query.returnData()));//If query ISBN set bilbiography ISBN
                        }else if(typeInt==1){
                            bibliography.setTitle(query.returnData());//If query Title set bilbiography ISBN
                        }else if(typeInt==2){
                            bibliography.setAuthors(query.returnData());//If query Authors set bilbiography Authors
                        }else if(typeInt==3){
                            bibliography.setPublisher(query.returnData());//If query Publisher set bilbiography Publisher
                        }else if(typeInt==4){
                            bibliography.setYear(query.returnData());//If query year set bilbiography Year
                        }
                    }
                    insertDatabase(bibliography);//insert new bibliography into database
        
                }else if(requestType.equalsIgnoreCase("UPDATE")){// If req type is submit
                    Bibliography bibliography=new Bibliography();//NOT DONE
                    for (Query query : queries){
                        query.setqueryInt();
                        int typeInt= query.returnqueryInt();
                        if(typeInt==0){
                            bibliography.setISBN(Integer.parseInt(query.returnData()));
                        }else if(typeInt==1){
                            bibliography.setTitle(query.returnData());
                        }else if(typeInt==2){
                            bibliography.setAuthors(query.returnData());
                        }else if(typeInt==3){
                            bibliography.setPublisher(query.returnData());
                        }else if(typeInt==4){
                            bibliography.setYear(query.returnData());
                        }
                    }
                }
                else if(requestType.equalsIgnoreCase("GET")){
                    ArrayList<Bibliography> response = new ArrayList<Bibliography>();
                    Bibliography bibliography=new Bibliography();
                    for (Query query : queries){
                        int typeInt= query.returnqueryInt();
                        if (typeInt==5;){
                            response=getAll(biblio);
                            break;
                        }else if(typeInt==0){
                            bibliography.setISBN(Integer.parseInt(query.returnData()));
                        }else if(typeInt==1){
                            bibliography.setTitle(query.returnData());
                        }else if(typeInt==2){
                            bibliography.setAuthors(query.returnData());
                        }else if(typeInt==3){
                            bibliography.setPublisher(query.returnData());
                        }else if(typeInt==4){
                            bibliography.setYear(query.returnData());
                        }
                    }
                    response=get(bibliography);
                    for(Bibliography biblio : response){
                        ArrayList<String> output=biblio.toarrayString();
                        for (String line:output){
                            os.writeBytes(line);
                        }

                    }

                }
                else if(requestType.equalsIgnoreCase("REMOVE")){
                    Bibliography bibliography=new Bibliography();
                    for (Query query : queries){
                        int typeInt= query.returnqueryInt();
                        if (typeInt==5;){
                            removeallDatabase();
                        }else if(typeInt==0){
                            bibliography.setISBN(Integer.parseInt(query.returnData()));
                        }else if(typeInt==1){
                            bibliography.setTitle(query.returnData());
                        }else if(typeInt==2){
                            bibliography.setAuthors(query.returnData());
                        }else if(typeInt==3){
                            bibliography.setPublisher(query.returnData());
                        }else if(typeInt==4){
                            bibliography.setYear(query.returnData());
                        }
                    }
                    removeDatabase(bibliography);
                }
                
                
            
        
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
                int querytypeInt;
                
                void setqueryInt(){
                    if(queryType.equalsIgnoreCase("ALL")){
                        queryType=5;
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
                int returnqueryInt(){
                    return querytypeInt;
                }
                String returnData(){
                    return data;
                }
                void setType(String type){
                    queryType=type;
                }
                void setData(String queryData){
                    data=queryData;
                }
            }
            private class Bibliography{
                
                int ISBN;
                String title;
                ArrayList<String> authors=new ArrayList<String>();
                
                String publisher;
                int year;
        
                //Override compare
                @Override
                public boolean equals(Bibliography o) { 
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
                    Bibliography c = (Bibliography) o; 
                    
                    // Compare the data members and return accordingly  
                    if (o.getTitle!=null && !((o.getTitle()).equalsIgnoreCase(this.title))){
                        equalsRelative=false;
                    }if (o.getISBN!=null && (o.getISBN()!=this.ISBN)){
                        equalsRelative=false;
                    }
                    if (o.getAuthors()!=null){ 
                        for (String oAuthors: o.getAuthors()){
                            if(!(this.authors.contains(oAuthors))){
                                equalsRelative=false;
                            }
                        }
                    }
                    if (o.getPublisher!=null && !((o.getPublisher()).equalsIgnoreCase(this.publisher))){
                        equalsRelative=false;
                    }
                    if (o.getYear!=null && !((o.getYear()).equalsIgnoreCase(this.year))){
                        equalsRelative=false;
                    }
                    return equalsRelative;
                } 
        
                public void setISBN(int iSBN) {
                    ISBN = iSBN;
                }
                public void setTitle(String title) {
                    this.title = title;
                }
                public void setPublisher(String publisher) {
                    this.publisher = publisher;
                }
                public void setAuthors(String authorsString) {
                    this.authors =Arrays.asList(authorsString.split(","));
                }
                public void setYear(int year) {
                    this.year = year;
                }
                public int getISBN() {
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
                public int getYear() {
                    return year;
                }
                public ArrayList<String> toarrayString(){
                    ArrayList<String> output= new ArrayList<String>();
                    if(this.ISBN){
                        output.add("ISBN".concat(Integer.toString(this.ISBN)));
                    }
                    if(this.title){
                        output.add(this.title);
                    }
                    if(this.authors){
                        String authorsString=String.join(",",this.authors);
                        output.add(authorsString);
                    }
                    if(this.publisher){
                        output.add(this.publisher);
                    }
                    if(this.year){
                        output.add(Integer.toString(this.year));
                    }
                    return output;
                }
            }
        }
        public void insertDatabase(Bibliography biblio){
            database.add(biblio);
        }
        public ArrayList<Bibliography> get(Bibliography biblio){
    
                return searchDatabase(biblio);
        }
        public ArrayList<Bibliography> searchDatabase(Bibliography biblio){
            ArrayList<Bibliography> found= new ArrayList<Bibliography>();
                for (Bibligraphy data: database){
                    if (data.equals(biblio)){
                        found.add(data);
                    }
                }
                return found;
            }
        public boolean removeDatabase(Bibliography biblio){
            boolean remove=false;
                for (Bibligraphy data: database){
                    if (data.equals(biblio)){
                        database.remove(data);
                        remove=true;
                    }
                }
                return remove;
            }
        
            public boolean removeallDatabase(Bibliography biblio){
                ArrayList<Bibliogaphy> collect;
                boolean remove=false;
                for (Bibligraphy data: database){
                   collect.add(data);
                }
                try{
                    database.removeAll(collect);
                    remove=true;
                }catch (Exception e){
                    remove=false;
                }
                return remove;
            }
            public ArrayList<Bibliography> getAll(Bibliography biblio){
                ArrayList<Bibliography> all= new ArrayList<Bibliography>();
                for (Bibligraphy data: database){
                        all.add(data);
                }
                return all;
            }
    }


