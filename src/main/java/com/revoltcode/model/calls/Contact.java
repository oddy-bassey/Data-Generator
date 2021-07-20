package com.revoltcode.model.calls;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Contact {

    private String imei;
    private String name;
    private String memory;
    private String phoneNumber;
    private String designation;
    private String email;
}
