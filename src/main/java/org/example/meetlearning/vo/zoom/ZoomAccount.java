package org.example.meetlearning.vo.zoom;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZoomAccount {

    private String zoomUserId;
    private String accountId;
    private Integer zoomType;
    private String clientId;
    private String clientSecret;
    private int apiCallCount;

}
