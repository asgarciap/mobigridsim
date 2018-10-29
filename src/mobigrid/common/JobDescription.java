package mobigrid.common;

import BESA.Kernell.Agent.Event.DataBESA;

import java.util.HashMap;
import java.util.Map;

/**
 * Describe a grid job.
 * @author arturogarcia
 */
public class JobDescription extends DataBESA {

    private String Name;
    private GridJobStateEnum State;
    private Map<String,Float> InputFilesSize;
    private float ProgramFileSize;
    private float RequiredRam;
    private float ComputationalTime;

    public JobDescription(String name, float programFileSize, float requiredRam, float computationalTime) {
        Name = name;
        ProgramFileSize = programFileSize;
        RequiredRam = requiredRam;
        ComputationalTime = computationalTime;
        InputFilesSize = new HashMap<>();
    }

    public void addInputFile(String name, float size) {
        InputFilesSize.put(name,size);
    }

    public void removeInputFile(String name) {
        InputFilesSize.remove(name);
    }

    public float getComputationalTime() {
        return ComputationalTime;
    }

    public float getInputFilesTotalSize() {
        float total = 0;
        for(Map.Entry<String, Float> item: InputFilesSize.entrySet()) {
            total += item.getValue();
        }
        return total;
    }

    public float getProgramFileSize() {
        return ProgramFileSize;
    }

    public float getRequiredRam() {
        return RequiredRam;
    }

    public String getName() {
        return Name;
    }

    public GridJobStateEnum getState() {
        return State;
    }

    public void setState(GridJobStateEnum state) {
        State = state;
    }

    public Map<String, Float> getInputFiles() {
        return InputFilesSize;
    }
}
