import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import Server.Request.Bibliography;
import jdk.jfr.DataAmount;

public final class Server {
    public static void main(String argv[]) throws IOException {
        // Get the port number from the command line.

        CopyOnWriteArrayList<Bibliography> database = new CopyOnWriteArrayList<Bibliography>();// Multithread safe
                                                                                               // arraylist of type
                                                                                               // bibliogrpahy

        int port = new Integer(argv[0]).intValue();// port nuactmber extr

        // Establish the listen socket.
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);// socket creation
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444."); // error message
            System.exit(1);
        }

        // Process requests in an inf loop

        while (true) {
            // Listen for a TCP connection request.

            Socket connect = null;
            try {
                connect = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");// Error message
                System.exit(1);
            }

            // Construct an object to process the request message.
            Request req;
            try {
                req = new Request(connect);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
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
                while ((line=(br.readLine())) != null) {// Get message line
                    
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
                            bibliography.setYear(Integer.parseInt(query.returnData()));//If query year set bilbiography Year
                        }
                    }
                    if(!insertDatabase(bibliography)){//insert new bibliography into database
                        //send error message
                    }
                }
                
                else if(requestType.equalsIgnoreCase("UPDATE")){// If req type is submit
                    Bibliography bibliography=new Bibliography();//Bibliography class for storage of bibliography
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
                            bibliography.setYear(Integer.parseInt(query.returnData()));//If query year set bilbiography Year
                        }
                    }
                    updateDatabase(biblio);//send bibliography object to update database method
                }

                else if(requestType.equalsIgnoreCase("GET")){
                    if (!(databaseEmpty())){
                        ArrayList<Bibliography> response = new ArrayList<Bibliography>();//Arraylist of bibliography objects to store the matches of get methods
                        Bibliography bibliography=new Bibliography();//Bibliography class for storage of bibliography
                        for (Query query : queries){
                            query.setqueryInt();
                            int typeInt= query.returnqueryInt();
                            if (typeInt==5;){
                                response=getAll();//returns all entries in database
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
                                bibliography.setYear(Integer.parseInt(query.returnData()));
                            }
                        }
                        response=get(bibliography);//Returns entries that match in the database
                        if(!(response.isEmpty())){
                            for(Bibliography biblio : response){//For all the matches in the database
                            ArrayList<String> output=biblio.toarrayString();//returns bibliography as array list of lines to send to client
                                for (String outLine:output){//For array of lines
                                    os.writeBytes(outLine);//Sends lines to client
                                }
                            }
                        }else{
                            os.writeBytes("No matches found");
                        }
                    }else{
                        os.writeBytes("Database empty, get request invalid");
                    }
                }
                else if(requestType.equalsIgnoreCase("REMOVE")){
                    Bibliography bibliography=new Bibliography();//Bibliography class for storage of bibliography
                    for (Query query : queries){//Loop through queries
                        query.setqueryInt();
                        int typeInt= query.returnqueryInt();
                        if (typeInt==5;){
                            removeallDatabase();//Removes all entries in database
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
                            bibliography.setYear(Integer.parseInt(query.returnData()));
                        }
                    }
                    removeDatabase(bibliography);//removes entires that match the query from the database
                }
            
                os.close();
                br.close();
                socket.close();
            }
        }
        public class Bibliography{//Helper Bibliography class that helps with storing of all book entries
                
            Integer ISBN;//Class has var for each query type
            String title;
            ArrayList<String> authors=new ArrayList<String>();
            String publisher;
            Integer year;
    
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
                if (o.getTitle()!=null && !((o.getTitle()).equalsIgnoreCase(this.title))){
                    equalsRelative=false;
                }if (o.getISBN()!=null && (o.getISBN()!=this.ISBN)){
                    equalsRelative=false;
                }
                if (o.getAuthors()!=null){ 
                    for (String oAuthors: o.getAuthors()){
                        if(!(this.authors.contains(oAuthors))){
                            equalsRelative=false;
                        }
                    }
                }
                if (o.getPublisher()!=null && !((o.getPublisher()).equalsIgnoreCase(this.publisher))){
                    equalsRelative=false;
                }
                if (o.getYear()!=null && !((o.getYear())==this.year)){
                    equalsRelative=false;
                }
                return equalsRelative;
            } 
            //Helper methods for bibliography functionality
            public void setISBN(int iSBN) {
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
            public Integer getISBN() {
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
                    output.add("ISBN ".concat(Integer.toString(this.ISBN)));
                }
                if(!(this.title.isBlank())){
                    output.add("TITLE ".concat(this.title));
                }
                if(!(this.authors.isEmpty())){
                    String authorsString=String.join(",",this.authors);
                    output.add("AUTHORS ".concat(authorsString));
                }
                if(!(this.publisher.isBlank())){
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
        public class Query{//class to help process incoming input from client
            String queryType;//First word of incoming input
            String data;//Data from line
            int querytypeInt;//private var to help for ID of query
            
            void setqueryInt(){//Helper method for ID of query type: ISBN, Title, Authors, Publisher, Year or ALL
                if(queryType.equalsIgnoreCase("ALL")){//ID's query type with an INT for easy arithmetic comparison of Queries
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
            int returnqueryInt(){//return query type
                return querytypeInt;
            }
            String returnData(){//return query data
                return data;
            }
            void setType(String type){//set query type
                queryType=type;
            }
            void setData(String queryData){//set query data
                data=queryData;
            }
        }
        public boolean insertDatabase(Bibliography biblio){//Adds bibliography to database
            boolean insert=false;
            if(!(database.contains(biblio))){//Check if database contains already
                database.add(biblio);
                insert=true;
            }
            return insert;//returns success of insertion process
        }
        public ArrayList<Bibliography> get(Bibliography biblio){
                return searchDatabase(biblio);
        }
        public ArrayList<Bibliography> searchDatabase(Bibliography biblio){//Method to search database 
            ArrayList<Bibliography> found= new ArrayList<Bibliography>();//returns arraylist of bibliography objects
                for (Bibliography data: database){//Iterate over database to check entries
                    if (data.equals(biblio)){
                        found.add(data);//add to arraylist being returned
                    }
                }
                return found;//return arraylist
            }
        public boolean removeDatabase(Bibliography biblio){//Method to remove from database
            boolean remove=false;//returns boolean value with success of removal
                for (Bibligraphy data: database){//iterate over database to check entries
                    if (data.equals(biblio)){
                        database.remove(data);//remove from database
                        remove=true;
                    }
                }
                return remove;//return success of removal
            }
        
            public boolean removeallDatabase(){//Method to clear database
                ArrayList<Bibliogaphy> collect;//with an thread safe array list the fastest way to remove is to copy into another array and them remove all in one go
                boolean remove=false;//var for success of removal
                for (Bibligraphy data: database){
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
            public ArrayList<Bibliography> getAll(){//Method to return all entries in database
                ArrayList<Bibliography> all= new ArrayList<Bibliography>();//arraylist to return all entries in database
                for (Bibligraphy data: database){
                        all.add(data);
                }
                return all;//return all entries in database
            }
            public boolean updateDatabase(Bibliography biblio){
                boolean updated=false;
                for (Bibliography data: database){
                    if (data.getISBN()==(biblio.getISBN())){
                        data.setTitle(biblio.getTitle());
                        data.setPublisher(biblio.getPublisher());
                        data.setYear(biblio.getYear());
                        //data.setAuthors(authorsString);
                        for (String authors: biblio.getAuthors()){
                            data.addAuthors(authors);//Might want to replace authors, asking proffessor
                        }
                        update=true;
                    }
                }
                return updated;
            }
            public boolean databaseEmpty(){
                if(database.isEmpty()){
                    return true;
                }else {
                    return false;
                }
            }
    }


