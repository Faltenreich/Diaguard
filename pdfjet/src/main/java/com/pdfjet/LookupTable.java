package com.pdfjet;

import java.util.*;

class Pair {
    List<Byte> key;
    int code;
}

public class LookupTable {

    private final int mask = 0xFFFF;
    private Pair[] pairs = new Pair[mask + 1];

    public LookupTable() {
        for (int i = 0; i < pairs.length; i++) {
            pairs[i] = new Pair();
        }
    }

    public void clear() {
        for (int i = 0; i < pairs.length; i++) {
            pairs[i].key = null;
        }
    }

    public int at(List<Byte> key, int code) {
        if (key.size() == 1) {
            return key.get(0) & 0xFF;
        }
        int index = hash(key);
        while (true) {
            if (pairs[index].key == null) {
                break;
            }
            if (pairs[index].key.equals(key)) {
                return pairs[index].code;
            }
            index += 1;
            if (index > mask) {
                index = 0;
            }
        }
        pairs[index].key = new ArrayList<Byte>(key);
        pairs[index].code = code;
        return -1;
    }

    private int hash(List<Byte> buffer) {
        int hash = 5381;
        for (Byte b1 : buffer) {
            hash = ((hash << 5) + hash) + (int) b1;
        }
        return hash & mask;
    }

}
