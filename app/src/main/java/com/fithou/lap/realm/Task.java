package com.fithou.lap.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Lap on 27/10/2017.
 */

public class Task extends RealmObject{
    @PrimaryKey
    private int mID;
    private String mName;

    public Task() {
    }

    public Task(int ID, String name) {
        mID = ID;
        mName = name;
    }

    @Override
    public String toString() {
        return this.mName;
    }

    public int getID() {
        return mID;
    }

    public void setID(int ID) {
        mID = ID;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
