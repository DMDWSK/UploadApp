package com.app.upload;

import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.util.Clients;

public class ThreadVM {
    LongOperation longOperation;

    @Command
    public void startLongOperation() {
        longOperation = new LongOperation() {

            @Override
            protected void execute() throws InterruptedException {
                //performing a long loop
                for (long i = 0; i < 100000000L; i++) {
                    checkCancelled();
                    if(i == 100000){
                        System.out.println("I am sleep");
                        //Thread.sleep(20000);
                        System.out.println("I am awake");
                    }
                    if(i == 10){
                        System.out.println("10");
                    }
                    if(i == 11){
                        System.out.println("11");
                    }

                    System.out.println(i);
                     //will throw an InteruptedException and exit when cancelled from outside

                }
            }

            @Override
            protected void onFinish() {
                //give the user some feedback the task is done
                Clients.showNotification("done sleeping for 5 seconds");
            }

            @Override
            protected void onCancel()  {

                Clients.showNotification("operation aborted...");

            }

            @Override
            protected void onCleanup() {
                Clients.showNotification("ON CleanUP");
            }
        };
        longOperation.start();

        Clients.showNotification("starting, you'll be notified when done.");
    }

    @Command
    public void cancelOperation() {

        longOperation.cancel();

    }
}
