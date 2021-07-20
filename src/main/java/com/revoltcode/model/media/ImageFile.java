package com.revoltcode.model.media;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageFile {
      private String imei;
      private String name;
      private String storedName;
      private String thumbLocation;
      private String path;
      private String memory;
      private Integer size;
      private String dateTime;
      private String dateTimeModified;
      private String resolution;
      private String pixelResolution;
      private String cameraMake;
      private String cameraModel;
      private String exifDateTime;
}
