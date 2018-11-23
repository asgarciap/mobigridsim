package mobigrid.simulation.state;

import mobigrid.common.MobileNodeDescription;
import mobigrid.common.NodeStateEnum;

import javax.xml.crypto.Data;
import javax.xml.soap.Node;
import java.util.Collection;
import java.util.HashMap;
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
        DataBuffers = new HashMap<>();
        Programs = new HashMap<>();
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

    public boolean downloadData(String dataId, float dataSize) throws Exception {

        float dataDownloaded = 0f;
        if(NodeFeatures.getState() != NodeStateEnum.DISCONNECTED) {
            NodeFeatures.setState(NodeStateEnum.RECEIVING_DATA);
            NodeFeatures.setRunningProcess(dataId);

            //Check if we have enough space to store the data
            if (NodeFeatures.getAvailableDisk() < dataSize) {
                NodeFeatures.setState(NodeStateEnum.READY);
                NodeFeatures.setRunningProcess("");
                throw new Exception("There is no enough available Disk Space to allocate " + dataSize + " MB.");
            }

            dataDownloaded = DataBuffers.getOrDefault(dataId,0.0f);
            if(dataDownloaded >= dataSize)
                return true;

            dataDownloaded += NodeFeatures.getDiskIORate();
            NodeFeatures.setBatteryLevel(NodeFeatures.getBatteryLevel() - NodeFeatures.getBatteryDischargeRate());

            NodeFeatures.setAvailableDisk(NodeFeatures.getAvailableDisk() - dataDownloaded);
            DataBuffers.put(dataId, dataDownloaded);
            if(dataDownloaded >= dataSize) {
                NodeFeatures.setState(NodeStateEnum.READY);
                NodeFeatures.setRunningProcess("");
            }
        }
        return (dataDownloaded >= dataSize);
    }

    public void eraseDataById(String dataId) {
        if(DataBuffers.containsKey(dataId)) {
            NodeFeatures.setAvailableDisk(NodeFeatures.getAvailableDisk()+DataBuffers.get(dataId));
            DataBuffers.remove(dataId);
        }
    }

    public float eraseNextDataBuffer() {
        float bufferSize;
        Map.Entry<String, Float> buffer  = DataBuffers.entrySet().iterator().next();
        bufferSize = buffer.getValue();
        DataBuffers.remove(buffer.getKey());
        NodeFeatures.setAvailableDisk(NodeFeatures.getAvailableDisk()+bufferSize);
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

    public boolean installProgram(Program program) throws Exception {
        if(!Programs.containsKey(program.getName())) {

            //Check if the file is already downloaded
            if(DataBuffers.containsKey(program.getName())) {
                Programs.put(program.getName(), program);
                return true;
            }

            try {
                if(downloadData(program.getName(), program.getSize())) {
                    Programs.put(program.getName(), program);
                    return true;
                }else {
                    return false;
                }
            } catch (Exception ex) {
                if(program.getSize() > NodeFeatures.getTotalDisk())
                    throw new Exception("The program can't be installed in the phone because there is no enough space to store it");

                eraseNextDataBuffer();
            }
        }
        return true;
    }

    public boolean executeProgram(String programId) {
        if(NodeFeatures.getState() != NodeStateEnum.DISCONNECTED) {
            NodeFeatures.setState(NodeStateEnum.RUNNING_JOB);
            NodeFeatures.setRunningProcess(programId);
            boolean finish = false;
            if (Programs.containsKey(programId)) {
                for (String id : Programs.get(programId).getInputDataIds()) {
                    if (!DataBuffers.containsKey(id)) {
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
                NodeFeatures.setBatteryLevel(NodeFeatures.getBatteryLevel() - (NodeFeatures.getBatteryDischargeRate() * Programs.get(programId).getTotalExecutionTime()));
                finish =  Programs.get(programId).execute();
            }
            if(finish) {
                NodeFeatures.setState(NodeStateEnum.READY);
                NodeFeatures.setRunningProcess("");
            }
            return finish;
        }
        return false;
    }

    public MobileNodeDescription getCurrentStatus() {
        return new MobileNodeDescription(NodeFeatures);
    }
}
