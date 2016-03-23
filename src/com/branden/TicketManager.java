package com.branden;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class TicketManager {

    public static void main(String[] args) {

        LinkedList<Ticket> ticketQueue = readTicketsFromFile();
        LinkedList<ResolvedTicket> resolvedTickets = new LinkedList<ResolvedTicket>();
        Scanner scan = new Scanner(System.in);

        while(true){

            System.out.println("1. Enter Ticket\n2. Delete by ID\n3. Delete by Issue\n4. Search by Name\n5. Display All Tickets\n6. Quit");
            int task = Integer.parseInt(scan.nextLine());

            if (task == 1) {
                //Call addTickets, which will let us enter any number of new tickets
                addTickets(ticketQueue);

            } else if (task == 2) {
                //delete a ticket
                deleteTicket(ticketQueue, resolvedTickets);

            }
            else if (task == 3) {
                //delete by issue
                searchTickets(ticketQueue, "issue");
                deleteTicket(ticketQueue, resolvedTickets);

            }
            else if (task == 4) {
                // search by name
                searchTickets(ticketQueue, "name");

            }
            else if ( task ==  6) {
                //Quit. Future prototype may want to save all tickets to a file
                System.out.println("Quitting program");
                saveData(ticketQueue, resolvedTickets);
                break;
            }
            else {
                //this will happen for 5 or any other selection that is a valid int
                //TODO Program crashes if you enter anything else - please fix
                //Default will be print all tickets
                printAllTickets(ticketQueue);
            }
        }

        scan.close();

    }

    protected static void deleteTicket(LinkedList<Ticket> ticketQueue,LinkedList<ResolvedTicket> resolvedTickets) {
        boolean found = false;
        printAllTickets(ticketQueue);   //display list for user

        if (ticketQueue.size() == 0) {    //no tickets!
            System.out.println("No tickets to delete!\n");
            return;
        }
        while( !found ) {
            Scanner deleteScanner = new Scanner(System.in);
            System.out.println("Enter ID of ticket to delete");

            // Check for a valid integer
            while( !deleteScanner.hasNextInt()) {
                deleteScanner.next();
                System.out.println("Please enter a valid integer");
            }
            int deleteID = deleteScanner.nextInt();

            //Loop over all tickets. Delete the one with this ticket ID
            for (Ticket ticket : ticketQueue) {
                if (ticket.getTicketID() == deleteID) {
                    found = true;
                    ticketQueue.remove( ticket );
                    System.out.println("Enter resolution");

                    String resolution = deleteScanner.next();
                    resolvedTickets.add( new ResolvedTicket(resolution, new Date(), ticket ) );

                    System.out.println(String.format("Ticket %d deleted", deleteID));
                    // exit function
                    return;

                }
            }
            // if we get this far there is no ticket found.
            System.out.println("Not found. Please try again.");
        }

        printAllTickets(ticketQueue);  //print updated list

    }


    protected static void addTickets(LinkedList<Ticket> ticketQueue) {
        Scanner sc = new Scanner(System.in);
        boolean moreProblems = true;
        String description, reporter;
        Date dateReported = new Date(); //Default constructor creates date with current date/time
        int priority;

        while (moreProblems){
            System.out.println("Enter problem");
            description = sc.next();
            System.out.println("Who reported this issue?");
            reporter = sc.next();

            System.out.println("Enter priority of " + description);

            while ( !sc.hasNextInt() ) {
                System.out.println("Priority must be integer");
                sc.next();
            }
            priority = sc.nextInt();

            Ticket t = new Ticket(description, priority, reporter, dateReported);
            //ticketQueue.add(t);
            addTicketInPriorityOrder(ticketQueue, t);

            printAllTickets(ticketQueue);

            System.out.println("More tickets to add?");
            String more = sc.next();
            if (more.equalsIgnoreCase("N")) {
                moreProblems = false;
            }
        }
    }

    protected static void addTicketInPriorityOrder(LinkedList<Ticket> tickets, Ticket newTicket){

        //Logic: assume the list is either empty or sorted

        if (tickets.size() == 0 ) {//Special case - if list is empty, add ticket and return
            tickets.add(newTicket);
            return;
        }

        //Tickets with the HIGHEST priority number go at the front of the list. (e.g. 5=server on fire)
        //Tickets with the LOWEST value of their priority number (so the lowest priority) go at the end

        int newTicketPriority = newTicket.getPriority();

        for (int x = 0; x < tickets.size() ; x++) {    //use a regular for loop so we know which element we are looking at

            //if newTicket is higher or equal priority than the this element, add it in front of this one, and return
            if (newTicketPriority >= tickets.get(x).getPriority()) {
                tickets.add(x, newTicket);
                return;
            }
        }

        //Will only get here if the ticket is not added in the loop
        //If that happens, it must be lower priority than all other tickets. So, add to the end.
        tickets.addLast(newTicket);
    }
    protected static void printAllTickets(LinkedList<Ticket> tickets) {
        System.out.println(" ------- All open tickets ----------");

        for (Ticket t : tickets ) {
            System.out.println(t); //Write a toString method in Ticket class
            //println will try to call toString on its argument
        }
        System.out.println(" ------- End of ticket list ----------");

    }
    protected static LinkedList<Ticket> searchTickets(LinkedList<Ticket> tickets, String searchField){
        Scanner searchScanner = new Scanner(System.in);

        String needle;
        String haystack;
        LinkedList<Ticket> matches = new LinkedList<>();

            // get string for searching
        System.out.printf("Enter %s to search for\n", searchField);
        needle = searchScanner.nextLine();
        // loop through tickets and their descriptions and search for string matches
            for (Ticket t : tickets) {

                if ( searchField.equalsIgnoreCase("name")) {
                    haystack = t.getReporter();
                } else if ( searchField.equalsIgnoreCase("issue")){
                    haystack = t.getDescription();
                } else{
                    System.out.println("Parameter 2 must be either 'name', or 'issue'");
                    break;
                }

                if ( haystack.toLowerCase().contains( needle.toLowerCase() )) {
                    matches.add(t);
                    System.out.println(t);

                }

                //System.out.println(t); //Write a toString method in Ticket class
                //println will try to call toString on its argument
            }
            if ( matches.size() == 0){
                System.out.println("No matches found");
            }
            // return list of tickets
            return matches;

    }
    public static void saveData(LinkedList<Ticket> openTickets,LinkedList<ResolvedTicket> resolvedTickets ){
        SimpleDateFormat format = new SimpleDateFormat("dd_MMM_yyyy");
        String openTicketsFile = "open_tickets.txt";
        String ResolvedTicketsFile = "Resolved_tickets_as_of_"+format.format( new Date() )+".txt";

        // FileWriter throws exception
        try {

            FileWriter of = new FileWriter(openTicketsFile, false);
            FileWriter rf = new FileWriter(ResolvedTicketsFile, true);

            BufferedWriter bfr_open = new BufferedWriter(of);
            BufferedWriter bfr_closed = new BufferedWriter(rf);

            Ticket.getStaticTicketIdCounter();
            bfr_open.write("ticketCount="+ Ticket.getStaticTicketIdCounter()+"\n");
            for ( int i = 0; i < openTickets.size(); i++){
                bfr_open.write(openTickets.get(i).toSavedFormat());
               if ( i != openTickets.size() -1 ) bfr_open.newLine();
            }



            for ( int i = 0; i < resolvedTickets.size(); i++){
                bfr_closed.write(resolvedTickets.get(i).toSavedFormat());
                if ( i != resolvedTickets.size() -1 ) bfr_closed.newLine();
            }

            bfr_closed.close();
            bfr_open.close();

        } catch (Exception er){
            System.out.println(er);
        }


    }
    public static LinkedList readTicketsFromFile() {
        int priority, id, ticketCount = 0;
        LinkedList<Ticket> ticketQueue = new LinkedList<Ticket>();
        String description, reporter, line;
        String[] tempData, tempData2;
        HashMap dataHashmap = new HashMap();
        Date dateReported;
        File openTicketsFile = new File("open_tickets.txt");
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM DD HH:mm:ss z yyyy");

        try {
            // check if the file exists. If not it will be created when the program extits.
            if ( openTicketsFile.exists() ){

                FileReader reader = new FileReader( openTicketsFile );
                BufferedReader bfr_open = new BufferedReader(reader);
                line = bfr_open.readLine();
                while(line != null ){
                    if ( !line.contains("ticketCount")) {
                        tempData = line.split(",");
                        for (int i = 0; i < tempData.length ; i++) {
                            // splits tempData into key: value
                            tempData2 = tempData[i].split("=");
                            dataHashmap.put(tempData2[0], tempData2[1] );
                        }
                    } else {
                        // if the first line is ticket count then add it to the static ticket counter,
                        // read the next line and continue to loop through the file.
                        tempData2 = line.split("=");
                        dataHashmap.put(tempData2[0], tempData2[1]);
                        ticketCount    = Integer.parseInt(  (String) dataHashmap.get("ticketCount") );
                        Ticket.setStaticTicketIDCounter(ticketCount);
                        line = bfr_open.readLine();
                        continue;
                    }


                    description = (String) dataHashmap.get("issue");
                    id          = Integer.parseInt( (String) dataHashmap.get("id") );
                    priority    = Integer.parseInt(  (String) dataHashmap.get("priority") );
                    reporter    = (String) dataHashmap.get("reportedBy");
                    dateReported = formatter.parse( (String) dataHashmap.get("reportDate") );

                    Ticket t = new Ticket(description, priority, reporter, dateReported, id);

                    addTicketInPriorityOrder(ticketQueue, t);

                    line = bfr_open.readLine();
                }

            }
        }
        catch (Exception ex){
            System.out.println("File Reader Error");
            System.out.println(ex);
        }
        return ticketQueue;
    }

}
