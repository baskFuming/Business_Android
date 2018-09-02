package com.zwonline.top28.bean;

/**
 * @author YSG
 * @desc
 * @date ${Date}
 */
public class QrCodeBean {

    /**
     * status : 1
     * msg : make qrcode successfully
     * data : {"qrcode":"iVBORw0KGgoAAAANSUhEUgAAAMwAAADMAQMAAAAF7N6xAAAABlBMVEX///8AAABVwtN+AAACTElEQVRYhb2YUW4kMQhEuQH3v2XdgPAKZ3ak/YWMkonbr6XGbgrKifjDT1ZVRoZUPYz+USSTFygYKRUVqR6muDfyAvVF/2GyY+ihIorJI9Rf0P6jEou+RFx0EB0GQSiuEFTiZRYXBBJfO7+KePh/n38ZtYkm74nFedO/rPZLEpuItOGxckSSyFFx9zp6e8uc36Jv8gbvo/D2CvHlBJKMVBdIxNAVhcW2+vqLEQpcR/n0MAKkoMz3BQpUTcKM5JCDQ4gDxEOd+7PO9PYyOEDkCgPkkMy6SD/l7SNPoguSx+OK0d8y8vqcpGTrvEnlbPEBoiJb1jxezhfUd4BQAKJwo3Hb0ZPeOnJbsx5IH6RBDK+ybSPnJaOeZV/nWnGC6gUQTsyuXzVWaB+9CknZZ3cp0ogjTpCrlusKA3qA0hrcR9QvuytnzVToSZ8DJOzOvMcHq5472EZYRtWXl5ueGhfoUyBZqfuac6ZOkCsVnW1cvptBTPZuI8wA71EOxWbVN+oAhT2I66WdiKVOHl0g91GqY41XsCkJ24N9VPY7ivdK023nucplpPFz09Achi3yCXK9N6jxIe7ZrwIsI9x2WYG/JxmbyLxA41C9UJZuKVoSBwgnMGKr8SFu12O/t1H9enAfpu1/eu0nyO3znTLd3qYunyCnB9XYh6RK6y6ePVhGLiMk6BhIcVFurAconJZzrM053L5T4D7KyZXP8TZqDoNniAimx9F78EBxh6y757Xcd/ICeXspXf53jsOxL7lAtjhOG1lzsp37ZNQm+sPPD46M2mn6xNDbAAAAAElFTkSuQmCC"}
     * dialog :
     */

    public int status;
    public String msg;
    public DataBean data;
    public String dialog;

    public static class DataBean {
        /**
         * qrcode : iVBORw0KGgoAAAANSUhEUgAAAMwAAADMAQMAAAAF7N6xAAAABlBMVEX///8AAABVwtN+AAACTElEQVRYhb2YUW4kMQhEuQH3v2XdgPAKZ3ak/YWMkonbr6XGbgrKifjDT1ZVRoZUPYz+USSTFygYKRUVqR6muDfyAvVF/2GyY+ihIorJI9Rf0P6jEou+RFx0EB0GQSiuEFTiZRYXBBJfO7+KePh/n38ZtYkm74nFedO/rPZLEpuItOGxckSSyFFx9zp6e8uc36Jv8gbvo/D2CvHlBJKMVBdIxNAVhcW2+vqLEQpcR/n0MAKkoMz3BQpUTcKM5JCDQ4gDxEOd+7PO9PYyOEDkCgPkkMy6SD/l7SNPoguSx+OK0d8y8vqcpGTrvEnlbPEBoiJb1jxezhfUd4BQAKJwo3Hb0ZPeOnJbsx5IH6RBDK+ybSPnJaOeZV/nWnGC6gUQTsyuXzVWaB+9CknZZ3cp0ogjTpCrlusKA3qA0hrcR9QvuytnzVToSZ8DJOzOvMcHq5472EZYRtWXl5ueGhfoUyBZqfuac6ZOkCsVnW1cvptBTPZuI8wA71EOxWbVN+oAhT2I66WdiKVOHl0g91GqY41XsCkJ24N9VPY7ivdK023nucplpPFz09Achi3yCXK9N6jxIe7ZrwIsI9x2WYG/JxmbyLxA41C9UJZuKVoSBwgnMGKr8SFu12O/t1H9enAfpu1/eu0nyO3znTLd3qYunyCnB9XYh6RK6y6ePVhGLiMk6BhIcVFurAconJZzrM053L5T4D7KyZXP8TZqDoNniAimx9F78EBxh6y757Xcd/ICeXspXf53jsOxL7lAtjhOG1lzsp37ZNQm+sPPD46M2mn6xNDbAAAAAElFTkSuQmCC
         */

        public String url;
    }
}
