package org.example.meetlearning.vo.file;


import lombok.Data;

import java.util.Date;

@Data
public class FilePageRespVo {

    private String lastModified;

    private String name;

    private String path;

    private Long size;

    private String type;

    private String url;

}
