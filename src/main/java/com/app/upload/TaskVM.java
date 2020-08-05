package com.app.upload;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Progressmeter;
import org.zkoss.zul.Textbox;


public class TaskVM {

    @Wire
    public Textbox txtProgress;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    public Progressmeter getProgressmeter() {
        return progressmeter;
    }

    public void setProgressmeter(Progressmeter progressmeter) {
        this.progressmeter = progressmeter;
    }

    @Wire
    Progressmeter progressmeter;

    @NotifyChange("progressmeter")
    @Command
    public void startLongOperation() {

        new LongOperation() {
            @Override
            protected void execute() throws InterruptedException {

                Thread.sleep(2000);

//                activate();
//                progressmeter.setValue(2);
//                deactivate();
              try{
                activate();
                progressmeter.setValue(20);
                deactivate();}catch(Exception e){
                  System.out.println("ZAEBALO");
              }

//                activate();
//                txtProgress.setValue("2");
//                deactivate();
//
//                Thread.sleep(2000);
//
//                activate();
//                txtProgress.setValue("4");
//                deactivate();
//
//                Thread.sleep(2000);
//                progress = "6";
//                activate();
//                txtProgress.setValue(progress);
//                deactivate();
//
//                Thread.sleep(2000);
//                progress = "8";
//                activate();
//                txtProgress.setValue(progress);
//                deactivate();
//
//                Thread.sleep(2000);
//                progress = "10";
//                activate();
//                txtProgress.setValue(progress);
//                deactivate();



            }
        }.start();
        //Clients.showNotification("background task started");
    }

}
