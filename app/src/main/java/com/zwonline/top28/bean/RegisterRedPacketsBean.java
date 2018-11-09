package com.zwonline.top28.bean;

import java.io.Serializable;

/**
 * 首页注册红包领取
 */
public class RegisterRedPacketsBean implements Serializable {


    /**
     * status : 1
     * msg : success
     * data : {"dialog_item":{"register_red_packet":{"status":"1","content1":"薄礼相送，赠送给您一个","content2":"20-100商机币\n随机红包","content3":"价值2-10元人民币","btn1":{"name":"点击领取","action":"/App/BusinessOpportunityCoin/getRegisterRedPacket.html"}}}}
     * dialog : 693o2cktcrrlf2b2eni620ghe1
     */

    public int status;
    public String msg;
    public DataBean data;
    public String dialog;

    public static class DataBean {
        /**
         * dialog_item : {"register_red_packet":{"status":"1","content1":"薄礼相送，赠送给您一个","content2":"20-100商机币\n随机红包","content3":"价值2-10元人民币","btn1":{"name":"点击领取","action":"/App/BusinessOpportunityCoin/getRegisterRedPacket.html"}}}
         */

        public DialogItemBean dialog_item;

        public static class DialogItemBean {
            /**
             * register_red_packet : {"status":"1","content1":"薄礼相送，赠送给您一个","content2":"20-100商机币\n随机红包","content3":"价值2-10元人民币","btn1":{"name":"点击领取","action":"/App/BusinessOpportunityCoin/getRegisterRedPacket.html"}}
             */

            public RegisterRedPacketBean register_red_packet;
            public ShowRegisterRedPacketBean show_register_red_packet;

            public static class RegisterRedPacketBean {
                /**
                 * status : 1
                 * content1 : 薄礼相送，赠送给您一个
                 * content2 : 20-100商机币
                 * 随机红包
                 * content3 : 价值2-10元人民币
                 * btn1 : {"name":"点击领取","action":"/App/BusinessOpportunityCoin/getRegisterRedPacket.html"}
                 */

                public String status;
                public String content1;
                public String content2;
                public String content3;
                public String content4;
                //                public String content5;
//                public String content6;
//                public String content7;
//                public String content8;
//                public String content9;
//                public String content10;
                public Btn1Bean btn1;
                public Btn2Bean btn2;

                public static class Btn1Bean {
                    /**
                     * name : 点击领取
                     * action : /App/BusinessOpportunityCoin/getRegisterRedPacket.html
                     */

                    public String name;
                    public String action;
                }

                public static class Btn2Bean {
                    /**
                     * name : 点击领取
                     * action : /App/BusinessOpportunityCoin/getRegisterRedPacket.html
                     */

                    public String name;
                    public String action;
                }
            }

            public static class ShowRegisterRedPacketBean {
                /**
                 * status : 1
                 * content1 : 薄礼相送，赠送给您一个
                 * content2 : 20-100商机币
                 * 随机红包
                 * content3 : 价值2-10元人民币
                 * btn1 : {"name":"点击领取","action":"/App/BusinessOpportunityCoin/getRegisterRedPacket.html"}
                 */

                public String status;
                public String content1;
                public String content2;
                public String content3;
                public String content4;
                //                public String content5;
//                public String content6;
//                public String content7;
//                public String content8;
//                public String content9;
//                public String content10;
                public Btn1Bean btn1;
                public Btn2Bean btn2;

                public static class Btn1Bean {
                    /**
                     * name : 点击领取
                     * action : /App/BusinessOpportunityCoin/getRegisterRedPacket.html
                     */

                    public String name;
                    public String action;
                }

                public static class Btn2Bean {
                    /**
                     * name : 点击领取
                     * action : /App/BusinessOpportunityCoin/getRegisterRedPacket.html
                     */

                    public String name;
                    public String action;
                }
            }
        }
    }
}
