package mobigrid.simulation.state;

import mobigrid.common.MobileNodeDescription;
import mobigrid.common.NodeStateEnum;

import javax.xml.crypto.Data;
import java.util.Map;
import java.util.Set;

import static java.lang.Thread.sleep;

/**
 * Defines a mobile execution node.
 * @author arturogarcia
 */
public class MobilePhone {

    // Node State Variables
    private MobileNodeDescription NodeFeatures;
    private Map<String, Float> DataBuffers;
    private Map<String, Program> Programs;

    public MobilePhone(MobileNodeDescription nodeFeatures) {
        NodeFeatures = nodeFeatures;
    }

    public void setDischargeRates(float batteryDischargeRate, float diskIORate) {
        NodeFeatures.setDischargeRates(batteryDischargeRate,diskIORate);
    }

    public int getPhoneId() {
        return NodeFeatures.getId();
    }

    public NodeStateEnum getState() {
        return NodeFeatures.getState();
    }

    public float getBatteryLevel() {
        return NodeFeatures.getBatteryLevel();
    }

    public float getTotalRam() {
        return NodeFeatures.getTotalRam();
    }

    public float getAvailableRam() {
        return NodeFeatures.getAvailableRam();
    }

    public float getTotalDisk() {
        return NodeFeatures.getTotalDisk();
    }

    public float getAvailableDisk() {
        return NodeFeatures.getAvailableDisk();
    }

    public void downloadData(String dataId, float dataSize) throws Exception {
        //Check if we have enough space to store the data
        if(NodeFeatures.getAvailableDisk() < dataSize)
            throw new Exception("There is no enough available Disk Space to allocate "+dataSize+" MB.");

        float dataDownloaded = 0.0f;
        while(dataDownloaded < dataSize) {
            dataDownloaded += NodeFeatures.getDiskIORate();
            sleep(1000);
        }
        NodeFeatures.setAvailableDisk(NodeFeatures.getAvailableDisk()+dataDownloaded);
        DataBuffers.put(dataId, dataSize);
    }

    public void eraseDataById(String dataId) {
        if(DataBuffers.containsKey(dataId)) {
            NodeFeatures.setAvailableDisk(NodeFeatures.getAvailableDisk()-DataBuffers.get(dataId));
            DataBuffers.remove(dataId);
        }
    }

    public float eraseNextDataBuffer() {
        float bufferSize = 0;
        for(Map.Entry<String, Float> buffer : DataBuffers.entrySet()) {
            bufferSize = buffer.getValue();
            DataBuffers.remove(buffer.getKey());
            NodeFeatures.setAvailableDisk(NodeFeatures.getAvailableDisk()-bufferSize);
        }
        return bufferSize;
    }

    public Set<String> getBuffersIds() {
        return DataBuffers.keySet();
    }

    public boolean isDataDownloaded(String dataId) {
        for(Map.Entry<String, Float> buffer : DataBuffers.entrySet()) {
            if (buffer.getKey().equals(dataId))
                return true;
        }
        return false;
    }

    public void installProgram(Program program) throws Exception {
        if(!Programs.containsKey(program.getName())) {
            boolean installed = false;
            while(!installed) {
                try {
                    downloadData(program.getName(), program.getSize());
                    Programs.put(program.getName(), program);
                    installed = true;
                } catch (Exception ex) {
                    if(program.getSize() > NodeFeatures.getTotalDisk())
                        throw new Exception("The program can't be installed in the phone because there is no enough space to store it");

                    eraseNextDataBuffer();
                }
            }
        }
    }

    public void executeProgram(String programId) {
        if(Programs.containsKey(programId)) {
            for (String id : Programs.get(programId).getInputDataIds()) {
                if(!DataBuffers.containsKey(id)) {
                    boolean downloaded = false;
                    while (!downloaded) {
                        try {
                            downloadData(id, Programs.get(programId).getInputDataSize(id));
                            downloaded = true;
                        } catch (Exception ex) {
                            eraseNextDataBuffer();
                        }
                    }
                }
            }
            Programs.get(programId).execute();
        }
    }

    public MobileNodeDescription getCurrentStatus() {
        return new MobileNodeDescription(NodeFeatures);
    }
}
