package com.panburikat.busscanner;

public class TicketList {
    private String ticketID;
    private String stop;
    private String quantity;
    private String dateOfPurchase;
    private String status;

    public TicketList(String ticketID, String stop, String quantity, String dateOfPurchase, String status) {
        this.ticketID = ticketID;
        this.stop = stop;
        this.quantity = quantity;
        this.dateOfPurchase = dateOfPurchase;
        this.status = status;
    }

    public String getTicketID() {
        return ticketID;
    }

    public String getStop() {
        return stop;
    }

    public String getQuantity(){ return  quantity; }

    public String getDateOfPurchase() {
        return dateOfPurchase;
    }

    public String getStatus() {
        return status;
    }

}

