package com.revoltcode.model.calls;

import com.revoltcode.datacategory.RecordType;
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
public class CallLog {

    private String CallStart;

    private String CallingNumber;

    private String CalledNumber;

    private Integer Duration;

    private RecordType RecordType;

    private String FirstCell;

    private String FirstSite;

    private String LastCell;

    private String LastSite;

    private String MainNumber;

    private String Imsi;

    private String Imei;

    private String Make;

    private String Model;

    private float FirstLongitude;

    private float FirstLatitude;

    private float LastLongitude;

    private float LastLatitude;

    private String CallDate;

    private String CallTime;
}

































