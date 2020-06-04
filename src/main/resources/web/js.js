zk.afterLoad('zul', function() {
    var xUpload = {};
    zk.override(zul.Upload.prototype, xUpload, {
        initContent : function() {
            xUpload.initContent.apply(this, arguments);
            if(this._wgt.directoryUpload) {
                this._inp.setAttribute('webkitdirectory', 'webkitdirectory');
                this._inp.setAttribute('mozdirectory', 'mozdirectory');
                this._inp.setAttribute('type','file');
            }
        }
    });
});