package com.revoltcode.model.calls;

import com.revoltcode.datacategory.CallType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Call {

    private String callId;
    private CallType type;
    private String number;
    private String name;
    private String timestamp;
    private String duration;
}
