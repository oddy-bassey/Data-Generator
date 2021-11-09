package com.revoltcode.model.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.revoltcode.datacategory.PhoneStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrackingReport {

    String trackingId;

    String alias;

    String username;

    String msisdn;

    String imsi;

    String imei;

    String type;

    String queryDate;

    float haLatitude;

    float haLongitude;

    float latitude;

    float longitude;

    String cellReference;

    PhoneStatus phoneStatus;

    String locationQuality;

    String locationAddress;

    Integer haResponseCode;

    String responseCode;
}
