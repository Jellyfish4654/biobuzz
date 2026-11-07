package com.example.jellytest;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TestUITelemetry {
    Telemetry telemetry;
    private String header;
    private String body;
    private String footer;

    public TestUITelemetry(Telemetry telemetry){
        this.telemetry = telemetry;
    }

    public synchronized void updateTelemetry(){
        for(String headerLine : header.split("\n")){
            telemetry.addLine(headerLine);
        }
        telemetry.addLine("------------");
        for(String bodyLine : body.split("\n")){
            telemetry.addLine(bodyLine);
        }
        telemetry.addLine("------------");
        for(String footerLine : footer.split("\n")){
            telemetry.addLine(footerLine);
        }
        telemetry.update();
    }

    public synchronized void setFooterControls(String[] controls, String[] actions){
        if(controls.length==actions.length) {
            StringBuilder footerBuilder = new StringBuilder();
            for (int key = 0;key < controls.length;key++) {
                footerBuilder.append(controls[key]).append("-").append(actions[key]).append("\n");
            }
            footer = footerBuilder.toString();
        }
    }
    public synchronized void setBody(String body){this.body = body;}
    public synchronized void addBodyLine(String line){
        body+=line+"\n";
    }
    public synchronized void addBodyLine(){
        body+="\n";
    }
    public synchronized void setHeader(String header){this.header = header;}
    public synchronized void addHeaderLine(String line){
        header+=line+"\n";
    }
    public synchronized void addHeaderLine(){header+="\n";}
    public synchronized void setFooter(String footer){this.footer = footer;}
    public synchronized void addFooterLine(String line){
        footer+=line+"\n";
    }
    public synchronized void addFooterLine(){
        footer+="\n";
    }

    public synchronized void clearAll(){
        body = "";
        header = "";
        footer = "";
    }



}
