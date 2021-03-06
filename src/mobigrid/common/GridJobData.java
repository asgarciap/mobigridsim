package mobigrid.common;

import BESA.Kernell.Agent.Event.DataBESA;

public class GridJobData extends DataBESA {

    private String DataId;
    private float DataSize;
    private int NodeId;
    private boolean Downloaded;

    public GridJobData(int nodeId, String dataId, float dataSize) {
        DataId = dataId;
        DataSize = dataSize;
        Downloaded = false;
        NodeId = nodeId;
    }

    public GridJobData(GridJobData data) {
        DataId = data.DataId;
        DataSize = data.DataSize;
        Downloaded = data.Downloaded;
        NodeId = data.NodeId;
    }

    public String getDataId() {
        return DataId;
    }

    public float getDataSize() {
        return DataSize;
    }

    public void setDataId(String dataId) {
        DataId = dataId;
    }

    public void setDataSize(float dataSize) {
        DataSize = dataSize;
    }

    public void setNodeId(int nodeId) {
        NodeId = nodeId;
    }

    public int getNodeId() {
        return NodeId;
    }

    public void setDownloaded(boolean downloaded) {
        Downloaded = downloaded;
    }

    public boolean isDownloaded() {
        return Downloaded;
    }
}
