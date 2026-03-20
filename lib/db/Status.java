package lib.db;

public enum Status {
    PENDING,
    FETCH,
    FETCH_RETRY,
    FORMAT,
    FORMAT_RETRY,
    CLASSIFY,
    CLASSIFY_RETRY,
    SUCCESS,
    FAILED
}