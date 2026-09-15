package com.smartparking.display;

/**
 * Observer interface for real-time parking spot updates.
 */
public interface DisplayBoardObserver {
    void onSpotStatusChanged();
}
