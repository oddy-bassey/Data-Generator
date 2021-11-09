package com.revoltcode.model.calls;

import com.revoltcode.datacategory.RecordType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CallLog {

    private String logId;

    private String callStart;

    private String callingNumber;

    private String calledNumber;

    private Integer duration;

    private RecordType recordType;

    private String firstCell;

    private String firstSite;

    private String lastCell;

    private String lastSite;

    private String mainNumber;

    private String imsi;

    private String imei;

    private String make;

    private String model;

    private float firstLongitude;

    private float firstLatitude;

    private float lastLongitude;

    private float lastLatitude;

    private String callDate;

    private String callTime;
}

































