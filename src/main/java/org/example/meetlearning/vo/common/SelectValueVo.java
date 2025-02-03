package org.example.meetlearning.vo.common;


import lombok.Data;

@Data
public class SelectValueVo {

    private String value;

    private String label;

    public SelectValueVo(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
