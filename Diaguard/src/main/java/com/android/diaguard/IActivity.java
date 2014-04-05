package com.android.diaguard;

/**
 * Created by Filip on 12.12.13.
 */
public interface IActivity {

    /**
     * Initializes Helper objects
     * Handling intent information
     */
    void initialize();

    /**
     * Initializes Views
     */
    void getComponents();

    /**
     * Implement View functionality
     */
    void initializeGUI();
}
