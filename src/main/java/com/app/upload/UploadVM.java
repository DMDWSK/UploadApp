package com.app.upload;
//import com.app.upload.service.DCM4CHEEService;
import com.app.upload.service.DCM4CHEEService;
import com.app.upload.service.UploadService;
import org.zkoss.bind.annotation.*;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import java.io.IOException;

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

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
    }

    @Init
    public void init() { }

    @Command
    public void visibility(){
        if(dicom.isSelected()){
            folderUpload.setVisible(true);
        }
        else{folderUpload.setVisible(false);}

        if(file.isSelected()){
            filesUpload.setVisible(true);
        }
        else{filesUpload.setVisible(false);}

        if(media.isSelected()){
            mediaUpload.setVisible(true);
        }
        else{mediaUpload.setVisible(false);}
    }

    @Command
    public void uploadDicom(@BindingParam("files") Media[] files) throws IOException {
        String path = UploadService.saveFolder(1, files);
        UploadService.uploadHandler(path);
        Messagebox.show("You have successfully uploaded your files");
//        DCM4CHEEService.upload(path);
    }

    @Command
    public void uploadFiles(@BindingParam("files") Media[] files) throws IOException {
        UploadService.filesHandler(1, files);
        Messagebox.show("You have successfully uploaded your files");
    }

}
