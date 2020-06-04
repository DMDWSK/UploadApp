package com.app.upload.service;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.IOD;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import java.io.*;
import java.util.*;

import org.dcm4che3.io.DicomInputStream;

public class UploadService {

    private static final Logger LOG = LoggerFactory.getLogger(UploadService.class);

    public static void videoToDicom(Media media) throws Exception { }

    public static Attributes getMetaData(String path){
        Attributes attributes = new Attributes();
        DicomInputStream din = null;
        try {
            din = new DicomInputStream(new File(path));
            attributes = din.readDataset(-1, -1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return attributes;
    }

    public static String saveFolder(int userId, Media[] files) {
        File dir = new File(Integer.toString(userId));
        for (Media file : files) {
            try {
                Files.copy(new File("D:\\" + dir + "\\"
                        + file.getName()), file.getStreamData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "D:\\" + dir;
    }

    public static void filesHandler(int userId, Media[] files) {
        File dir = new File("_" + Integer.toString(userId) + "_FilesStorage");
        for (Media file : files) {
            System.out.println(file.getName());
            try {
                Files.copy(new File("D:\\" + dir + "\\"
                        + file.getName()), file.getStreamData());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void uploadHandler(String dirPath) {
        File rootDir = new File(dirPath);
        File[] list = rootDir.listFiles();
        IOD iod = new IOD();
        ArrayList<String> dicomPaths = new ArrayList<String>();

        if (list == null) return;
        for (File file : list) {
            if (file.isDirectory()) {
                uploadHandler(file.getAbsolutePath());
            } else {
                try {
                    if (iod == null) {
                        throw new IllegalStateException("IOD net initialized");
                    }
                    DicomInputStream dis = new DicomInputStream(file);
                    Attributes attrs = dis.readDataset(-1, -1);
                    ValidationResult result = attrs.validate(iod);
                    if (result.isValid()) {
                        dicomPaths.add(file.getAbsolutePath());
                    } else {
                        System.out.println("NOTVALID");
                    }
                } catch (IOException e) {   }
            }
        }
        dicomCompare(dicomPaths);

    }
    public static void dicomCompare(ArrayList<String> dicomPaths){
        ListMultimap<String, String> multimap = ArrayListMultimap.create();
        ArrayList<String> keyValue = new ArrayList<String>();
        for ( String dicomPath : dicomPaths) {
            multimap.put(getMetaData(dicomPath).getString(Tag.StudyID)+
                    getMetaData(dicomPath).getString(Tag.PatientID),dicomPath );
        }
        for (String dicomIdentification : multimap.keySet()) {
            List<String> sortedPaths = multimap.get(dicomIdentification);
        }
        System.out.println(multimap);
    }
}

