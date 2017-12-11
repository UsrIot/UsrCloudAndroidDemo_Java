package cn.usr.usrcloudmqttsdkdemo.entity;

/**
 * Created by Administrator on 2017/12/1 0001.
 */

public class OptionResponse {

    private String result;

    private String dataId;

    private String option;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    @Override
    public String toString() {
        return "OptionResponse{" +
                "result='" + result + '\'' +
                ", dataId='" + dataId + '\'' +
                ", option='" + option + '\'' +
                '}';
    }
}
