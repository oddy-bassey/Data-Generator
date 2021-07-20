package com.revoltcode.model.message;

import com.revoltcode.datacategory.MsgStatus;
import com.revoltcode.datacategory.MsgType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sms {

   private String imei;
   private String number;
   private String name;
   private String timestamp;
   private MsgStatus status;
   private String folder;
   private String storage;
   private MsgType type;
   private String text;
   private String smsc;
}
