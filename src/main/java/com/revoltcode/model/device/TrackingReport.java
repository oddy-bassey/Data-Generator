package com.revoltcode.model.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsefa.csv.annotation.CsvDataType;
import org.jsefa.csv.annotation.CsvField;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrackingReport {

    String Alias;

    String Username;

    String MSISDN;

    String IMSI;

    String IMEI;

    String Type;

    String QueryDate;

    float HALatitude;

    float HALongitude;

    float Latitude;

    float Longitude;

    String CellReference;

    com.revoltcode.datacategory.PhoneStatus PhoneStatus;

    String LocationQuality;

    String LocationAddress;

    Integer HAResponseCode;

    String ResponseCode;
}
