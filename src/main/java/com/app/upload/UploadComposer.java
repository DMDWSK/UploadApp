//package com.app.upload;
//
//import org.zkoss.bind.annotation.AfterCompose;
//import org.zkoss.zk.ui.*;
//import org.zkoss.zk.ui.event.*;
//import org.zkoss.zk.ui.select.SelectorComposer;
//import org.zkoss.zk.ui.select.annotation.Listen;
//import org.zkoss.zul.*;
//import org.zkoss.util.media.*;
//
//public class UploadComposer extends SelectorComposer {
//
//    @AfterCompose
//    public void doAfterCompose(Component comp) throws Exception {
//        super.doAfterCompose(comp);
//
//    }
//
//    @Listen("onUpload = #folderUpload; onUpload = #fileUpload ")
//    public void handleUpload(UploadEvent e)
//    {
//        if (e.getMedias() != null) {
//            StringBuilder sb = new StringBuilder("You uploaded: \n");
//
//
//            for (Media media : e.getMedias()) {
//                sb.append(media.getName());
//                sb.append(" (");
//                sb.append(media.getContentType());
//                sb.append(")\n");
//            }
//
//            Messagebox.show(sb.toString());
//        } else {
//            Messagebox.show("You uploaded no files!");
//        }
//    }
//}
