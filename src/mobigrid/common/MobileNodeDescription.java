package mobigrid.common;

import BESA.Kernell.Agent.Event.DataBESA;

import static java.lang.Thread.sleep;

/**
 * Describe a mobile node in the grid simulator.
 * @author arturogarcia
 */
public class MobileNodeDescription extends DataBESA {

    private int Id;
    private NodeStateEnum State;
    private float BatteryLevel;
    private float TotalRam;
    private float AvailableRam;
    private float TotalDisk;
    private float AvailableDisk;
    private String RunningProcess;

    // Node discharge rates (percentage/second)
    private float BatteryDischargeRate;

    //Time to read/write data (bytes/second)
    private float DiskIORate;

    public MobileNodeDescription(int id, float initialBattery, float initialRam, float initialDisk) {
        Id = id;
        BatteryLevel = initialBattery;
        TotalRam = initialRam;
        AvailableRam = initialRam;
        TotalDisk = initialDisk;
        AvailableDisk = initialDisk;
        State = NodeStateEnum.READY;
        RunningProcess = "";

        //default discharge rates
        BatteryDischargeRate = 0.3f; //percentaje/sec
        DiskIORate = 50f; //bytes/sec
    }

    public MobileNodeDescription(MobileNodeDescription copy) {
        this.Id = copy.Id;
        this.State = copy.State;
        this.BatteryLevel = copy.BatteryLevel;
        this.TotalRam = copy.TotalRam;
        this.AvailableRam = copy.AvailableRam;
        this.TotalDisk = copy.TotalDisk;
        this.AvailableDisk = copy.AvailableDisk;
        this.BatteryDischargeRate = copy.BatteryDischargeRate;
        this.DiskIORate = copy.DiskIORate;
    }

    public void setDischargeRates(float batteryDischargeRate, float diskIORate) {
        BatteryDischargeRate = batteryDischargeRate;
        DiskIORate = diskIORate;
    }

    public float getBatteryLevel() {
        return BatteryLevel;
    }

    public void setBatteryLevel(float batteryLevel) {
        BatteryLevel = batteryLevel;
    }

    public float getTotalRam() {
        return TotalRam;
    }

    public float getAvailableRam() {
        return AvailableRam;
    }

    public void setAvailableRam(float availableRam) {
        AvailableRam = availableRam;
    }

    public float getTotalDisk() {
        return TotalDisk;
    }

    public float getAvailableDisk() {
        return AvailableDisk;
    }

    public void setAvailableDisk(float availableDisk) {
        AvailableDisk = availableDisk;
    }

    public int getId() {
        return Id;
    }

    public NodeStateEnum getState() {
        return State;
    }

    public void setState(NodeStateEnum state) {
        State = state;
    }

    public float getDiskIORate() {
        return DiskIORate;
    }

    public float getBatteryDischargeRate() {
        return BatteryDischargeRate;
    }

    public String getRunningProcess() {
        return RunningProcess;
    }

    public void setRunningProcess(String runningProcess) {
        RunningProcess = runningProcess;
    }
}
