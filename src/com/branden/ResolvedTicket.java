package com.branden;

import java.util.Date;

/**
 * Created by badams on 3/22/16.
 */
public class ResolvedTicket{
    private Date resolutionDate;
    private String resolution;
    private Ticket ticket;
    
    public void setResolutionDate(Date resolutionDate) {
        this.resolutionDate = resolutionDate;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    ResolvedTicket(String resolution, Date d, Ticket ticket) {
        setResolution(resolution);
        setResolutionDate(d);
        this.ticket = ticket;
    }

    public String toSavedFormat(){
        return "id=" + ticket.getTicketID() + ", issued=" + ticket.getDescription() +", " + "priority=" + ticket.getPriority() +", " + "reportedBy=" + ticket.getReporter() + ", " + "reportDate=" + ticket.getDateReported()+ ", resolution=" + resolution + ", resolutionDate=" + resolutionDate + "\n";
    }
    public String toString(){
        return("ID = " + ticket.getTicketID() + ", Issued = " + ticket.getDescription() + ", Priority = " + ticket.getPriority() + ", Reported by = "
                + ticket.getReporter() + ", Reported on= " + ticket.getReporter() + ", Resolution" + resolution + ", Resolved Date= " + resolutionDate );
    }
}
