package com.xuexiang.application.AAChartCoreLib.AAOptionsModel;

import com.xuexiang.application.AAChartCoreLib.AATools.AAJSStringPurer;

public class AASeriesEvents {
    public String legendItemClick;

    public AASeriesEvents legendItemClick(String prop) {
        String pureJSFunctionStr = "(" + prop + ")";
        pureJSFunctionStr = AAJSStringPurer.pureJavaScriptFunctionString(pureJSFunctionStr);
        legendItemClick = pureJSFunctionStr;
        return this;
    }

}
