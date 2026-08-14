package com.library.model;

/**
 * Represents the current status of a book copy in inventory.
 */
public enum BookStatus {
    AVAILABLE,
    BORROWED,
    RESERVED,
    IN_TRANSFER,
    LOST
}
