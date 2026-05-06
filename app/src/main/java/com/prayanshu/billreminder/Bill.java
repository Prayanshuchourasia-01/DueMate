package com.prayanshu.billreminder;

public class Bill {

    int id;
    String name;
    double amount;
    String dueDate;
    int isPaid;

    public Bill(int id, String name, double amount, String dueDate, int isPaid) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.dueDate = dueDate;
        this.isPaid = isPaid;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public String getDueDate() {
        return dueDate;
    }

    public int getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(int isPaid) {
        this.isPaid = isPaid;
    }
}