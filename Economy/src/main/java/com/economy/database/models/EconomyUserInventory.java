package com.economy.database.models;

import java.util.ArrayList;

public class EconomyUserInventory {
    final ArrayList<EconomyUserInventoryEntry> entries = new ArrayList<>();

    public void addEntry(EconomyUserInventoryEntry economyUserInventoryEntry) {
        this.entries.add(economyUserInventoryEntry);
    }

    public ArrayList<EconomyUserInventoryEntry> getEntries() {
        return this.entries;
    }

    public int getSize() {
        return getEntries().size();
    }

}
