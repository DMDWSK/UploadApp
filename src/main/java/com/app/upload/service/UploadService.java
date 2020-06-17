package com.app.upload.service;
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

    public static ArrayList<String> uploadHandler(String dirPath) {
        File rootDir = new File(dirPath);
        File[] list = rootDir.listFiles();
        IOD iod = new IOD();
        ArrayList<String> dicomPaths = new ArrayList<String>();

        for (File file : list != null ? list : new File[0]) {
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
        return dicomPaths;

    }
    
    public static HashMap<String,ArrayList<String>> groupingTagsInfo(ArrayList<String> dicomPaths){
        HashMap<String,ArrayList<String>>studies= new HashMap<String,ArrayList<String>>();

        ArrayList<String> tagsData = new ArrayList<String>();

        for ( String dicomPath : dicomPaths) {
            if (!studies.containsKey(getMetaData(dicomPath).getString(Tag.StudyID))) {
                tagsData = new ArrayList<String>();
                tagsData.add(getMetaData(dicomPath).getString(Tag.PatientID));
                tagsData.add(getMetaData(dicomPath).getString(Tag.PatientName));
                tagsData.add(getMetaData(dicomPath).getString(Tag.PatientBirthDate));
                studies.put(getMetaData(dicomPath).getString(Tag.StudyID),tagsData);
            }
        }
        return studies;
    }

    public static HashMap<String,ArrayList<String>>groupingDicomPaths(ArrayList<String> dicomPaths){
        HashMap<String,ArrayList<String>>studyPaths= new HashMap<String,ArrayList<String>>();
        ArrayList<String> groupedPaths = new ArrayList<String>();
        for ( String dicomPath : dicomPaths) {
            if (studyPaths.containsKey(getMetaData(dicomPath).getString(Tag.StudyID))) {
                studyPaths.get(getMetaData(dicomPath).getString(Tag.StudyID)).add(dicomPath);
            }
            else {
                groupedPaths = new ArrayList<String>();
                groupedPaths.add(dicomPath);
                studyPaths.put(getMetaData(dicomPath).getString(Tag.StudyID),groupedPaths);
            }
        }
        return studyPaths;
    }
}


