package com.netease.nim.uikit.business.session;

public class MyQRCodeBean {
    String qr_Type;
    String qr_Code;

    public MyQRCodeBean(String qr_Type, String qr_Code) {
        this.qr_Type = qr_Type;
        this.qr_Code = qr_Code;
    }

    public MyQRCodeBean() {
        super();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public String getQr_Type() {
        return qr_Type;
    }

    public void setQr_Type(String qr_Type) {
        this.qr_Type = qr_Type;
    }

    public String getQr_Code() {
        return qr_Code;
    }

    public void setQr_Code(String qr_Code) {
        this.qr_Code = qr_Code;
    }
}
