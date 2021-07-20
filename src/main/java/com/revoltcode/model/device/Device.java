package com.revoltcode.model.device;

import com.revoltcode.model.calls.Call;
import com.revoltcode.model.calls.Contact;
import com.revoltcode.model.media.DocumentFile;
import com.revoltcode.model.media.ImageFile;
import com.revoltcode.model.message.Sms;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Device {

        private String reportType;
        private String selectedManufacture;
        private String selectedModel;
        private String detectedManufacture;
        private String detectedModel;
        private String revision;
        private String imei;
        private String iccid;
        private String imsi;
        private String advertisingId;
        private String startDateTime;
        private String endDateTime;
        private String phoneDateTime;
        private String connectionType;
        private String xmlFormat;
        private String software;
        private String serial;
        private Integer usingclient;
        private List<Contact> contacts;
        private List<Sms> sms;
        private List<Call> calls;
        private List<ImageFile> imageFiles;
        private List<DocumentFile> documentFiles;
        private TrackingReport trackingReports;
}
