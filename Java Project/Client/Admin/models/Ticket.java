package com.dor.cmovies.models;

public class Ticket {

    private int seat;
    private int rowSeat;
    private String purchasDate;
    private Show show = new Show();

    public Ticket() {}

    public Ticket(int seat, int rowSeat, Show show, String purchaseDate) {

        this.show = show;
        this.seat = seat;
        this.rowSeat = rowSeat;
        this.purchasDate = purchaseDate;

    }


    public Ticket(Show show, String purchasDate) {

        this.show = show;
        this.purchasDate = purchasDate;
        this.seat = 0;
        this.rowSeat = 0;
    }

    public String getPurchasDate() {
        return purchasDate;
    }

    public void setPurchasDate(String purchasDate) {
        this.purchasDate = purchasDate;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }


    public int getSeat() {
        return seat;
    }


    public void setSeat(int seat) {
        this.seat = seat;
    }


    public int getRowSeat() {
        return rowSeat;
    }


    public void setRowSeat(int rowSeat) {
        this.rowSeat = rowSeat;
    }

    public String toString() {

        return "Ticket: Seat: " + seat + "\tRow: " + rowSeat + "\tDate: " + purchasDate + "\nShow: " + show.toString();
    }


}

