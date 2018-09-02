package com.zwonline.top28.utils;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * MD5加密
 */

public class SignUtils {
    public static String getSignature(Map<String, String> params, String key) throws IOException {
        // 先将参数以其参数名的字典序升序进行排序
        Map<String, String> sortedParams = new TreeMap<String, String>(params);
        Set<Map.Entry<String, String>> entrys = sortedParams.entrySet();

        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder basestring = new StringBuilder();
        int indx = 0;
        for (Map.Entry<String, String> param : entrys) {

            if (param.getValue() != null && !"".equals(param.getValue())) {
                basestring.append(param.getKey()).append("=").append(URLEncoder.encode(param.getValue(), "utf-8"));
            }

            if (indx < (params.size() - 1)) {
                basestring.append("&");
            }

            indx++;
        }


        //basestring.append("&key=" + key);
        basestring.append(key);
        String result = basestring.toString();
        if (result.startsWith("&")) {
            result = result.substring(1, result.length());
        }
        System.out.println("MD5加密前=" + basestring.toString());
        String sign = getMD5(result);
        System.out.println("MD5加密后=" + sign.toUpperCase().toString());
        return sign.toUpperCase();
    }

    /**
     * 没有编码过得
     *
     * @param params
     * @param key
     * @return
     * @throws IOException
     */
    public static String getSignatures(Map<String, String> params, String key) throws IOException {
        // 先将参数以其参数名的字典序升序进行排序
        Map<String, String> sortedParams = new TreeMap<String, String>(params);
        Set<Map.Entry<String, String>> entrys = sortedParams.entrySet();

        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder basestring = new StringBuilder();
        int indx = 0;
        for (Map.Entry<String, String> param : entrys) {

            if (param.getValue() != null && !"".equals(param.getValue())) {
                basestring.append(param.getKey()).append("=").append(param.getValue());
            }

            if (indx < (params.size() - 1)) {
                basestring.append("&");
            }
            indx++;
        }

        //basestring.append("&key=" + key);
        basestring.append(key);
        System.out.println("MD5加密前=" + basestring.toString());
        String sign = getMD5(basestring.toString());
        System.out.println("MD5加密后=" + sign.toUpperCase().toString());
        return sign.toUpperCase();
    }

    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F'};

    public static String getMD5(String inStr) {
        byte[] inStrBytes = inStr.getBytes();
        try {
            MessageDigest MD = MessageDigest.getInstance("MD5");
            MD.update(inStrBytes);
            byte[] mdByte = MD.digest();
            char[] str = new char[mdByte.length * 2];
            int k = 0;
            for (int i = 0; i < mdByte.length; i++) {
                byte temp = mdByte[i];
                str[k++] = hexDigits[temp >>> 4 & 0xf];
                str[k++] = hexDigits[temp & 0xf];
            }
            return new String(str).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    /*移除值为空的键值对*/
    public static void removeNullValue(Map map) {
        Set set = map.keySet();
        for (Iterator iterator = set.iterator(); iterator.hasNext(); ) {
            Object obj = (Object) iterator.next();
            Object value = (Object) map.get(obj);
            remove(value, iterator);
        }
    }

    /**
     * 移除map中的空值
     * <p>
     * Iterator 是工作在一个独立的线程中，并且拥有一个 mutex 锁。
     * Iterator 被创建之后会建立一个指向原来对象的单链索引表，当原来的对象数量发生变化时，这个索引表的内容不会同步改变，
     * 所以当索引指针往后移动的时候就找不到要迭代的对象，所以按照 fail-fast 原则 Iterator 会马上抛出 java.util.ConcurrentModificationException 异常。
     * 所以 Iterator 在工作的时候是不允许被迭代的对象被改变的。
     * 但你可以使用 Iterator 本身的方法 remove() 来删除对象， Iterator.remove() 方法会在删除当前迭代对象的同时维护索引的一致性。
     *
     * @param obj
     * @param iterator
     */
    private static void remove(Object obj, Iterator iterator) {
        if (obj instanceof String) {
            String str = (String) obj;
            if (isEmpty(str)) {  //过滤掉为null和""的值 主函数输出结果map：{2=BB, 1=AA, 5=CC, 8=  }
//            if("".equals(str.trim())){  //过滤掉为null、""和" "的值 主函数输出结果map：{2=BB, 1=AA, 5=CC}
                iterator.remove();
            }

        } else if (obj instanceof Collection) {
            Collection col = (Collection) obj;
            if (col == null || col.isEmpty()) {
                iterator.remove();
            }

        } else if (obj instanceof Map) {
            Map temp = (Map) obj;
            if (temp == null || temp.isEmpty()) {
                iterator.remove();
            }

        } else if (obj instanceof Object[]) {
            Object[] array = (Object[]) obj;
            if (array == null || array.length <= 0) {
                iterator.remove();
            }
        } else {
            if (obj == null) {
                iterator.remove();
            }
        }
    }

    public static boolean isEmpty(Object obj) {
        return obj == null || obj.toString().length() == 0;
    }
}
