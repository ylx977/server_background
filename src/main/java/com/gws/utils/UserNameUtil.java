package com.gws.utils;

import java.util.Random;

/**
 * 【姓名】
 *
 * @author wangdong 2017/8/4 下午4:22
 */
public class UserNameUtil {


    private static String BOY =
                         "梁、栋、维、启、克、伦、峰、旭、鹏、泽、" +
                         "晨、辰、士、乐、建、杰、致、树、逸、盛、" +
                         "雄、顺、冠、策、腾、茂、榕、风、航、弘、" +
                         "义、兴、良、飞、彬、富、和、鸣、朋、斌、" +
                         "光、时、泰、博、林、民、友、志、清、坚、" +
                         "庆、天、星、思、群、豪、心、伟、浩、勇、" +
                         "强、平、东、文、辉、业、名、永、信、彦、" +
                         "健、世、广、海、山、敬、安、武、生、涛、" +
                         "龙、元、全、善、胜、学、祥、才、毅、俊、" +
                         "仁、义、礼、智、信、新、功、耀、成、康";

    private static String GIRL =
                          "妍、凝、静、如、怡、玉、琴、茹、兰、卉、" +
                          "娜、茹、雪、莉、悦、玥、楚、芬、红、安、" +
                          "玉、艳、婕、颖、霏、仁、琳、瑕、燕、烨、" +
                          "瑶、洁、雅、礼、美、筱、丽、笙、桂、青、" +
                          "秋、素、欣、咏、依、香、思、花、萌、帆、" +
                          "晓、文、念、婷、婉、冉、夏、舒、琳、韵、" +
                          "品、玲、岚、仪、亦、春、冬、妮、莹、忻、" +
                          "飞、雯、筱、纪、钰、伊、梦、颖、虹、彤、" +
                          "怡、枫、星、熙、敏、樱、葵、芯、昕、茹、" +
                          "水、淑、清、鸿、萍、泓、涓、娟、婧、倩";

    /**
     * 获取boy姓名
     * @return
     */
    public static String getBoyName(Integer num){
        if(null == num || num<0 || num>5){
            return  null;
        }

        return encapsName(num, true);
    }

    /**
     * 获取女生名字
     * @param num
     * @return
     */
    public static String getGirlName(Integer num){
        if(null == num || num<0 || num>5){
            return  null;
        }

        return encapsName(num, false);
    }

    /**
     * 封装
     * @param num
     * @param sex
     * @return
     */
    private static String encapsName(Integer num, Boolean sex){
        String[] names = null;
        if(sex){
            names = BOY.split("、");
        }else{
            names = GIRL.split("、");
        }
        int min=1;
        int max = names.length;
        Random random = new Random();
        StringBuffer userName = new StringBuffer("喵星人·");
        for (int i = 0; i < num; i++) {
            int s = random.nextInt(max) % (max - min + 1) + min;
            userName.append(names[s]);
        }
        return userName.toString();
    }

}
