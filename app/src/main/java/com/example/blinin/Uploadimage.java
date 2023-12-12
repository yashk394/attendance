package com.example.blinin;

public class Uploadimage {
    public String imgurl;

    public void setMkey(String mkey) {
        this.mkey = mkey;
    }

    public String mkey;

    public String getImgurl() {
        return imgurl;
    }

    public String getMkey() {
        return mkey;
    }

    public Uploadimage(String imgurl) {
        this.imgurl = imgurl;
    }


}
