package org.example.meetlearning.vo.zoom;


import lombok.Data;

@Data
public class ZoomAccount {

    private String accountId;
    private String accountType;
    private String apiKey;
    private String clientSecret;
    private int apiCallCount;
    private boolean active;
}
