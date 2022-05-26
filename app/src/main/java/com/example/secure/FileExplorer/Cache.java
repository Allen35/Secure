package com.example.secure.FileExplorer;

import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public enum Cache {

    Instance;
    private TreeMap<String, Long> tMap = new TreeMap<>();
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public TreeMap<String, Long> gettMap() {
        return tMap;
    }

    public ReentrantReadWriteLock getLock() {
        return lock;
    }
}