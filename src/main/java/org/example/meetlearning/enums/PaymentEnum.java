package org.example.meetlearning.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentEnum {

    B001("支付宝"),
    B002("微信"),
    B003("银行"),
    B004("现金"),
    B005("淘宝"),
    B006("拼多多"),
    B007("其他"),
    ;

    private final String content;

    /**
     * 根据code获取枚举
     */
    public static PaymentEnum getByCode(String code) {
        for (PaymentEnum paymentEnum : PaymentEnum.values()) {
            if (paymentEnum.getContent().equals(code)) {
                return paymentEnum;
            }
        }
        return null;
    }
}
