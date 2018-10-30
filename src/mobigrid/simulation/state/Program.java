package mobigrid.simulation.state;

import mobigrid.common.MobileNodeDescription;
import mobigrid.common.NodeStateEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.lang.Thread.sleep;

/**
 * Defines a program to execute in a mobile node.
 * @author arturogarcia
 */
public class Program {

    private String Name;
    private float Size;
    private int TotalExecutionTime;
    private Map<String, Float> InputData;

    public Program(String name, float size, int totalExecutionTime) {
        Name = name;
        Size = size;
        TotalExecutionTime = totalExecutionTime;
        InputData = new HashMap<>();
    }

    public void addInputData(String dataId, float dataSize) {
        InputData.put(dataId, dataSize);
    }

    public String getName() {
        return Name;
    }

    public float getSize() {
        return Size;
    }

    public float getTotalExecutionTime() {
        return TotalExecutionTime;
    }

    public Set<String> getInputDataIds() {
        return InputData.keySet();
    }

    public float getInputDataSize(String dataId) {
        if(InputData.containsKey(dataId))
            return InputData.get(dataId);
        return 0;
    }

    public void execute() {
        try {
            sleep(TotalExecutionTime * 1000);
        }
        catch(InterruptedException ex)
        {
            String m = ex.getMessage();
        }
    }
}
