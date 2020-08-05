package com.app.upload;

import com.app.upload.service.DCM4CHEEService;
import com.app.upload.service.UploadService;
import org.zkoss.bind.annotation.*;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class UploadVM {
    private LongOperation longOperation;
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
    @Wire
    Progressmeter progressmeter;
    @Wire
    Label percentLabel;

    private HashMap<String, ArrayList<String>> groupingDicomPaths;
    private HashMap<String, ArrayList<String>> groupingTagsInfo;

    public Label getPercentLabel() {
        return percentLabel;
    }

    public void setPercentLabel(Label percentLabel) {
        this.percentLabel = percentLabel;
    }

    public Progressmeter getProgressmeter() {
        return progressmeter;
    }

    public void setProgressmeter(Progressmeter progressmeter) {
        this.progressmeter = progressmeter;
    }

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


    @Init
    public void init() {
//        longOperation = new LongOperation() {
//            @Override
//            protected void execute() throws InterruptedException {
//                Set<Listitem> uploadedInformation = getCheckedValues();
//                ArrayList<String> chosenDicoms = new ArrayList<String>();
//                int iterationDcmNum = 0;
//                int progressValue = 0;
//
//                for (Listitem certainPatient : uploadedInformation) {
//                    chosenDicoms.addAll(groupingDicomPaths.get(certainPatient.getLabel()));
//                    iterationDcmNum = 100/chosenDicoms.size();
//                }
//
//                for (String chosenDicom : chosenDicoms) {
//
//                    checkCancelled();
//
//                    progressValue = progressValue+iterationDcmNum;
//                    activate();
//                    DCM4CHEEService.upload(chosenDicom);
//                    percentLabel.setValue(chosenDicom + String.valueOf(progressValue)+"%");
//                    progressmeter.setValue(progressValue);
//                    deactivate();
//                }
//            }
//            @Override
//            protected void onCancel()  {
//                Clients.showNotification("operation aborted...");
//            }
//        };
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

    @NotifyChange({"progressmeter","percentLabel"})
    @Command
    public void dcm4cheeSender()  {
            longOperation = new LongOperation() {
            @Override
            protected void execute() throws InterruptedException {
                Set<Listitem> uploadedInformation = getCheckedValues();
                ArrayList<String> chosenDicoms = new ArrayList<String>();
                int iterationDcmNum = 0;
                int progressValue = 0;

                for (Listitem certainPatient : uploadedInformation) {
                    chosenDicoms.addAll(groupingDicomPaths.get(certainPatient.getLabel()));
                    iterationDcmNum = 100/chosenDicoms.size();
                }

                for (String chosenDicom : chosenDicoms) {

                    checkCancelled();

                    progressValue = progressValue+iterationDcmNum;
                    activate();
                    DCM4CHEEService.upload(chosenDicom);
                    percentLabel.setValue(chosenDicom + String.valueOf(progressValue)+"%");
                    progressmeter.setValue(progressValue);
                    deactivate();
                }
            }
            @Override
            protected void onCancel()  {
                Clients.showNotification("operation aborted...");
            }
        };longOperation.start();
    }
    @Command
    public void cancelOperation(){
        try {
            longOperation.cancel();
        }
        catch (Exception e){
            System.out.println("CANCELED");
        }

    }
}
