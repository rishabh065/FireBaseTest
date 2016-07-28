//Data Models for favourite contacts list
package org.indianredcross.firstaid.Data;

/**
 * Created by rishabh on 6/18/2016.
 */
public class Contacts {
    private String name;
    private String number;
    public Contacts() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
