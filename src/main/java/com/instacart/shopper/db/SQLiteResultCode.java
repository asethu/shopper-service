package com.instacart.shopper.db;

/**
 * Helper enum to wrap standard SQLite result codes for readability.
 *
 * @author arun
 */
public enum SQLiteResultCode {
    SQLITE_CONSTRAINT (19);

    private int errorCode;

    private SQLiteResultCode(int code) {
        errorCode = code;
    }

    public int errorCode() {
        return errorCode;
    }
}
