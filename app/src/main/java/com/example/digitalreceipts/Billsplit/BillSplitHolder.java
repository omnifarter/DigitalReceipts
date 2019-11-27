package com.example.digitalreceipts.Billsplit;

import java.util.HashMap;

public class BillSplitHolder {
    String personname;
    HashMap<String,Integer> person_item_amount;
    BillSplitHolder(){

    }

    public void setPerson_item_amount(HashMap<String, Integer> person_item_amount) {
        this.person_item_amount = person_item_amount;
    }

    public void setPersonname(String personname) {
        this.personname = personname;
    }

    public HashMap<String, Integer> getPerson_item_amount() {
        return person_item_amount;
    }

    public String getPersonname() {
        return personname;
    }
}
