package mobigrid.common;

public enum GridJobStateEnum {
    DISPATCHED, //job dispatched to the mobile node
    QUEUED, //job received and queued in the mobile node
    DOWNLOADING, //job downloading files needed to run
    READY, //job ready to run
    RUNNING, //job running
    DISCARDED, //job discarded
    FINISHED, // job finished
    PAUSED
}
