package com.app.upload;

import com.app.upload.service.DCM4CHEEService;
import com.app.upload.service.UploadService;
import org.zkoss.bind.annotation.*;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class UploadVM {
    @Wire("#dicom")
    Radio dicom;
    @Wire("#media")
    Radio media;
    @Wire("#file")
    Radio file;
    @Wire("#mediaUpload")
    Button mediaUpload;
    @Wire("#folderUpload")
    Button folderUpload;
    @Wire("#filesUpload")
    Button filesUpload;
    @Wire
    Listbox patientData;

    private HashMap<String, ArrayList<String>> groupingDicomPaths;
    private HashMap<String, ArrayList<String>> groupingTagsInfo;

    public HashMap<String, ArrayList<String>> getGroupingDicomPaths() {
        return groupingDicomPaths;
    }

    public void setGroupingDicomPaths(HashMap<String, ArrayList<String>> groupingDicomPaths) {
        this.groupingDicomPaths = groupingDicomPaths;
    }

    public HashMap<String, ArrayList<String>> getGroupingTagsInfo() {
        return groupingTagsInfo;
    }

    public void setGroupingTagsInfo(HashMap<String, ArrayList<String>> groupingTagsInfo) {
        this.groupingTagsInfo = groupingTagsInfo;
    }

    public void printList() {
        System.out.println(this.groupingTagsInfo);
    }

    @Init
    public void init() {
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    @Command
    public void visibility() {
        if (dicom.isSelected()) {
            folderUpload.setVisible(true);
        } else {
            folderUpload.setVisible(false);
        }

        if (file.isSelected()) {
            filesUpload.setVisible(true);
        } else {
            filesUpload.setVisible(false);
        }

        if (media.isSelected()) {
            mediaUpload.setVisible(true);
        } else {
            mediaUpload.setVisible(false);
        }
    }

    @NotifyChange("groupingTagsInfo")
    @Command
    public void uploadDicom(@BindingParam("files") Media[] files) {
        String servicePath = UploadService.saveFolder(1, files);
        ArrayList<String> dicomPaths = UploadService.uploadHandler(servicePath);
        setGroupingTagsInfo(UploadService.groupingTagsInfo(dicomPaths));
        setGroupingDicomPaths(UploadService.groupingDicomPaths(dicomPaths));
    }

    @Command
    public Set<Listitem> getCheckedValues() {
        return patientData.getSelectedItems();
    }

    @Command
    public void uploadFiles(@BindingParam("files") Media[] files) {
        UploadService.filesHandler(1, files);
    }

    @Command
    public void dcm4cheeSender() {
        Set<Listitem> uploadedInformation = getCheckedValues();
        ArrayList<String> chosenDicoms = new ArrayList<String>();
        for (Listitem certainPatient : uploadedInformation) {
            chosenDicoms = groupingDicomPaths.get(certainPatient.getLabel());
            try {
                for (String chosenDicom : chosenDicoms) {
                    DCM4CHEEService.upload(chosenDicom);
                }
                Messagebox.show("Усі файли було успішно завантажено");
            } catch (Exception e) {
                Messagebox.show("Щось пішло не так.Сталася помилка.");
            }
        }
        System.out.println(chosenDicoms);
    }

}
