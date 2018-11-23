package mobigrid.common;

import BESA.Kernell.Agent.Event.DataBESA;
import com.sun.corba.se.spi.orbutil.threadpool.Work;

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
    private float TotalComputationTime;
    private float WorkComplete;

    public JobDescription(String name, float programFileSize, float requiredRam, float computationalTime) {
        Name = name;
        ProgramFileSize = programFileSize;
        RequiredRam = requiredRam;
        ComputationalTime = computationalTime;
        InputFilesSize = new HashMap<>();
        addInputFile(Name, ProgramFileSize);
        WorkComplete = 0;
        TotalComputationTime = 0;
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

    public void setWorkComplete(float workComplete) {
        WorkComplete = workComplete;
    }

    public float getWorkComplete() {
        return WorkComplete;
    }

    public void updateComputationTime() {
        TotalComputationTime++;
        WorkComplete = (TotalComputationTime/ComputationalTime)*100;
        if(WorkComplete > 100)
            WorkComplete = 100f;
    }
}
