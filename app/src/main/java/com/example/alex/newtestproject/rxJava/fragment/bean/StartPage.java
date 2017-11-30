package com.example.alex.newtestproject.rxJava.fragment.bean;

public class StartPage extends BaseBean {
    public ImageVo obj;

    public class ImageVo {

        public String imageAlt;// 图片标题

        public String subTitle;// 副标题

        public String imageSrc;// 图片链接地址

        public int target;// 目标链接打开方式(0:不新开页面,1:新开页面)

        public String targetHref;// 目标链接

        public int activeType;// 活动类型(0:最新活动; 1:邀请活动; 2:已过期;3,临时活动)

    }
}
