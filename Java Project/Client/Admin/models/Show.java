package com.dor.cmovies.models;

public class Show {

    private long showID;
    private String showName;
    private String artistName;
    private String showDescription;
    private String showVenue;
    private String showDate;
    private String showTimeStart;
    private double ticketPrice;
    private  int numberOfTickets;
    private  int ticketsInvited;
    private  int ticketsAvailable;
    private String imageURL;


    public Show() {}

    public Show(String e)
    {
        showID = -1;
        showName = e;
    }

    public Show(Show show) {

        this.showID = show.getShowID();
        this.showName = show.getShowName();
        this.artistName = show.getArtistName();
        this.showDescription = show.getShowDescription();
        this.showVenue = show.getShowVenue();
        this.showDate = show.getShowDate();
        this.showTimeStart = show.getShowTimeStart();
        this.ticketPrice = show.getTicketPrice();
        this.numberOfTickets = show.getNumberOfTickets();
        this.ticketsInvited = show.getTicketsInvited();
        this.ticketsAvailable = show.getTicketsAvailable();
        this.imageURL = show.getImageURL();

    }

    public Show(long id, String name, String artist, String description, String venue, String date, String time,
                double price, int numOfTickets, int invited, int available, String imageUrl )
    {
        this.showID = id;
        this.showName = name;
        this.artistName = artist;
        this.showDescription = description;
        this.showVenue = venue;
        this.showDate = date;
        this.showTimeStart = time;
        this.ticketPrice = price;
        this.numberOfTickets = numOfTickets;
        this.ticketsInvited = invited;
        this.ticketsAvailable = available;
        this.imageURL = imageUrl;

    }

    public Show(long id, String name, String artist, String description, String venue, String date, String time,
                double price, int numOfTickets, int invited, int available)
    {
        this.showID = id;
        this.showName = name;
        this.artistName = artist;
        this.showDescription = description;
        this.showVenue = venue;
        this.showDate = date;
        this.showTimeStart = time;
        this.ticketPrice = price;
        this.numberOfTickets = numOfTickets;
        this.ticketsInvited = invited;
        this.ticketsAvailable = available;

    }

    public Show(String showName, String showDate){

        this.showName = showName;
        this.showDate = showDate;
        this.showTimeStart = showTimeStart;
        this.imageURL = imageURL;

    }

    public long getShowID() {
        return showID;
    }

    public void setShowID(long showID) {
        this.showID = showID;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getShowDescription() {
        return showDescription;
    }

    public void setShowDescription(String showDescription) {this.showDescription = showDescription; }

    public String getShowVenue() {
        return showVenue;
    }

    public void setShowVenue(String showVenue) {
        this.showVenue = showVenue;
    }

    public String getShowDate() {
        return showDate;
    }

    public void setShowDate(String showDate) {
        this.showDate = showDate;
    }

    public String getShowTimeStart() {
        return showTimeStart;
    }

    public void setShowTimeStart(String showTimeStart) {
        this.showTimeStart = showTimeStart;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public int getTicketsInvited() {
        return ticketsInvited;
    }

    public void setTicketsInvited(int ticketsInvited) {
        this.ticketsInvited = ticketsInvited;
    }

    public int getTicketsAvailable() {
        return ticketsAvailable;
    }

    public void setTicketsAvailable(int ticketsAvailable) { this.ticketsAvailable = ticketsAvailable; }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


    public String toString() {

        String s1 = "showID: " + showID + " showName: " + showName + " artistName: " + artistName + " showDescription: " + showDescription;
        String s2 = "showVenue: " + showVenue + " showDate: " + showDate + " showTimeStart: " + showTimeStart + " ticketPrice: " + ticketPrice;
        String s3 = "numberOfTickets: " + numberOfTickets + " ticketsInvited: " + ticketsInvited + " ticketsAvailable: " + ticketsAvailable;
        String s4 = "\n \t ImageUrl: \t" + imageURL;
        return s1 + " " + s2 + " " + s3 + " " + s4;
    }
}
