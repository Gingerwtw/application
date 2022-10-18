

package com.xuexiang.application;

public class UrlUtils {

    public String getFace_calibrate() {
        return "http://10.249.45.227:8000/calibrate_face_1/";}
    public String getFace_analyze() {
        return "http://10.249.45.227:8001/analyze_face_1/";}
    public String getTongue_calibrate() {
        return "http://10.249.45.227:8000/calibrate_tongue_1/";}
    public String getTongue_analyze() {
        return "http://10.249.45.227:8001/analyze_tongue_1/";}
    public String getPulse_analyze() {
        return "http://10.249.45.227:8001/analyze_pulse_1/";}
    public String getLogin() {
        return "http://10.249.45.227:8001/login/";}
    public String getRegister() {
        return "http://10.249.45.227:8001/register/";}
    public String getAddRecord() {
        return "http://10.249.45.227:8001/add_diagnostic_record/";}
    public String getRecord() {
        return "http://10.249.45.227:8001/get_diagnostic_record/";}
}
