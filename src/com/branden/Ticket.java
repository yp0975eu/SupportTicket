package com.branden;
import java.util.*;

/**
 * Created by badams on 3/22/16.
 */
public class Ticket {
    private int priority;
    private String reporter; //Stores person or department who reported issue
    private String description;
    private Date dateReported;


    //STATIC Counter - accessible to all Ticket objects.
    //If any Ticket object modifies this counter, all Ticket objects will have the modified value
    //Make it private - only Ticket objects should have access
    private static int staticTicketIDCounter = 1;
    //The ID for each ticket - instance variable. Each Ticket will have its own ticketID variable
    protected int ticketID;

    public static int getStaticTicketIdCounter(){
        return staticTicketIDCounter;
    }
    public static void setStaticTicketIDCounter( int i ){
        staticTicketIDCounter = i;
    }
    public Ticket(String desc, int p, String rep, Date date) {
        this.description = desc;
        this.priority = p;
        this.reporter = rep;
        this.dateReported = date;
        this.ticketID = staticTicketIDCounter;
        staticTicketIDCounter++;
    }
    // constructor for creating tickets from file with pre-existing ids
    public Ticket(String desc, int p, String rep, Date date, int id) {
        this.description = desc;
        this.priority = p;
        this.reporter = rep;
        this.dateReported = date;
        this.ticketID = id;
    }

    protected String getDescription(){
        return description;
    }
    protected String getReporter(){
        return reporter;
    }
    protected int getTicketID() {
        return ticketID;
    }
    protected int getPriority() {
        return priority;
    }

    public Date getDateReported() {
        return dateReported;
    }

    public String toSavedFormat(){
        return "id=" + this.ticketID + ",issue=" + this.description +"," + "priority=" + this.priority +"," + "reportedBy=" + this.reporter + "," + "reportDate=" + this.dateReported;
    }
    public String toString(){
        return("ID = " + this.ticketID + ", Issue = " + this.description + ", Priority = " + this.priority + " Reported by = " + this.reporter + ", Reported on = " + this.dateReported);
    }
}

