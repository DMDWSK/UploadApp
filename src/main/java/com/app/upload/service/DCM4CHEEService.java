package com.app.upload.service;
import org.dcm4che3.tool.storescu.StoreSCU;

public class DCM4CHEEService {

    public static void upload(String path) {
        String [] args = {"-c", "DCM4CHEE2@195.211.240.48:4949", path};
        StoreSCU.main(args);
    }
}

