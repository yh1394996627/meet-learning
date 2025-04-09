package org.example.meetlearning.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileTypeEnum {

    CERTIFICATE("证书", 1),
    WX("微信", 2),
    ZFB("支付宝", 3),
    ;

    private final String remark;
    private final Integer fileType;

}
