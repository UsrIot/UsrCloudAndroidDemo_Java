package cn.usr.usrcloudmqttsdkdemo.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/1 0001.
 */

public class ReceiveDataPointsResponse {

    private List<OptionResponse> optionResponse;


    private String devName;

    public List<OptionResponse> getOptionResponse() {
        return optionResponse;
    }

    public void setOptionResponse(List<OptionResponse> optionResponse) {
        this.optionResponse = optionResponse;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }


    @Override
    public String toString() {
        return "ReceiveDataPointsResponse{" +
                "optionResponse=" + optionResponse +
                ", devName='" + devName + '\'' +
                '}';
    }
}
