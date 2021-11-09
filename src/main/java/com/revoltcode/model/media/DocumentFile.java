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
public class DocumentFile {

    private String docId;
    private String name;
    private String storedName;
    private String path;
    private String memory;
    private Integer size;
    private String dateTime;
    private String dateTimeModified;

}
