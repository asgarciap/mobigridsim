package mobigrid.common;

import BESA.Kernell.Agent.Event.DataBESA;

public class GridJobData extends DataBESA {

    private String DataId;
    private float DataSize;

    public GridJobData(String dataId, float dataSize) {
        DataId = dataId;
        DataSize = dataSize;
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
}
