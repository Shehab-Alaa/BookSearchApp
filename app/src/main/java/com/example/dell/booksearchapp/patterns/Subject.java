package com.example.dell.booksearchapp.patterns;

/**
 * Created by dell on 2/19/2019.
 */

public interface Subject {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyAllObservers();
}
