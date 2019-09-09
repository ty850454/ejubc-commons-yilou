package com.ejubc.commons.yilou.utils;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author Lincoln
 * @description 提供操作字符串的常用工具方法
 */
public class LocalStringUtils {

    /**
     * 消息格式化
     *
     * @param key
     * @param args
     * @return
     */
    public static String messageFormat(String key, Object[] args){
        if(key == null || args == null || args.length == 0) return key;
        Matcher m = Pattern.compile("\\{(\\d)\\}").matcher(key);
        while(m.find()){
            key = key.replace(m.group(), String.valueOf(args[Integer.parseInt(m.group(1))]));
        }
        return key;
    }

    /**
     * 使用正则表达式验证字符串格式是否合法
     *
     * @param str
     * @return
     */
    public static boolean patternValidate(String pattern, String str) {
        if (pattern == null || str == null) {
            throw new RuntimeException("参数格式不合法[patternValidate(String " + pattern + ", String " + str + ")]");
        }
        return Pattern.matches(pattern, str);
    }

    /**
     * 验证字符串是否为空字符
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().equals("") || str.trim().toLowerCase().equals("null");
    }

    /**
     * 判断字符串不为空
     *
     * @param str
     * @return
     */
    public static boolean notBlank(String str) {
        return !isBlank(str);
    }


    /**
     * 将单个对象转换为可显字符串
     *
     * @param obj
     * @return
     */
    public static String toString(Object obj) {
        if (obj instanceof String) {
            return "\"" + obj + "\"";
        }
        if (obj instanceof Object[]) {
            return toString((Object[]) obj);
        } else {
            return String.valueOf(obj);
        }
    }

    /**
     * 将对象数组转换为可显字符串
     *
     * @param objArr
     * @return
     */
    public static String toString(Object[] objArr) {
        return toString(objArr, true);
    }

    public static String toString(Object[] objArr, boolean hasBracket) {
        return makeString(objArr, ",", hasBracket ? "[" : "", hasBracket ? "]" : "");
    }

    public static String makeString(Object... values) {
        return makeString(",", values);
    }

    public static String makeString(String splitChars, Object... values) {
        return makeString(values, splitChars);
    }

    public static String makeString(Object[] objArr, String splitChars) {
        return makeString(objArr, splitChars, "", "");
    }


    public static String makeString(List list, String splitChar) {
        if (list == null) return null;
        StringBuffer buf = new StringBuffer();
        for (Object o : list) {
            if (o == null) continue;
            if (buf.length() > 0) buf.append(splitChar);
            buf.append(o.toString());
        }
        return buf.toString();
    }

    public static String makeString(Object[] objArr, String splitChars, String beginBracket, String endBracket) {
        if (objArr == null) {
            return null;
        }

        StringBuffer buf = new StringBuffer(beginBracket);
        for (int i = 0; i < objArr.length; i++) {
            if (i > 0) {
                buf.append(splitChars);
            }
            buf.append(objArr[i]);
        }
        buf.append(endBracket);
        return buf.toString();
    }

    public static String toLowerCase(String str) {
        if (str == null) {
            return null;
        }
        return str.toLowerCase();
    }

    /**
     * 精确分隔字符串（分隔符之间视为有空字符串）
     *
     * @param srcStr     被分隔的字符串
     * @param splitChars 多个分隔符
     * @return 分隔结果
     */
    public static List<String> splitStringAccurate(String srcStr, String splitChars) {
        if (isBlank(srcStr)) {
            return null;
        }

        int begin = 0;
        List<String> strList = new ArrayList<String>();
        while (true) {
            int indexOf = srcStr.indexOf(splitChars, begin);
            if (indexOf == -1) {
                strList.add(srcStr.substring(begin));
                break;
            }
            strList.add(srcStr.substring(begin, indexOf));
            begin = indexOf + 1;
        }

        return strList;
    }

    /**
     * 分隔字符串
     *
     * @param srcStr     被分隔的字符串
     * @param splitChars 多个分隔符
     * @return 分隔结果
     */
    public static List<String> splitString(String srcStr, String splitChars) {
        if (isBlank(srcStr)) {
            return null;
        }

        List<String> strList = new ArrayList<String>();
        StringTokenizer tok = new StringTokenizer(srcStr, splitChars);
        while (tok.hasMoreTokens()) {
            strList.add(tok.nextToken());
        }
        return strList;
    }

    public static String convertPlaceholder(String str, Map<String, Object> values) {
        if (isBlank(str) || values == null || values.size() == 0 || !str.startsWith("${") || !str.endsWith("}")) {
            return str;
        }

        return String.valueOf(values.get(str.substring(2, str.length() - 1)));
    }

    public static boolean isMp(String str) {
        return str.matches("^1[0-9]{10}$");
    }

    public static boolean isEmail(String str) {
        return str.matches("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
    }

    public static String findString(String str, String regex) {
        Matcher matcher = Pattern.compile(regex).matcher(str);
        return matcher.find() ? matcher.group() : null;
    }

    /**
     * 模糊手机号
     *
     * @return
     */
    public static String maskMobilePhone(String str) {
        if (LocalStringUtils.isBlank(str) || str.length() != 11) {
            return null;
        }
        return str.substring(0, 3) + "******" + str.substring(9, 11);
    }

    public static byte[] getBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("获取字符串[" + content + "]字符集为[" + charset + "]的字节码时发生异常:" + e.getMessage(), e);
        }
    }

    /**
     * 模糊姓名
     *
     * @return
     */
    public static String maskName(String str) {
        if (LocalStringUtils.isBlank(str)) {
            return null;
        }
        String maskName = str.substring(0, 1);
        for (int i = 1; i < str.length(); i++) {
            maskName += "*";
        }
        return maskName;
    }

    /**
     * 模糊身份证
     *
     * @return
     */
    public static String maskIDCard(String str) {
        if (LocalStringUtils.isBlank(str) || str.length() != 18) {
            return null;
        }
        return str.substring(0, 3) + "*************" + str.substring(16, 18);
    }

    /**
     * 模糊身份证
     *
     * @return
     */
    public static String maskBankCardNO(String str) {
        if (isBlank(str)) return "";
        if (str.length() < 16) throw new RuntimeException("银行卡号[" + str + "]长度小于16位!");
        StringBuffer buf = new StringBuffer();
        buf.append(str.substring(0, 6));
        for (int i = 0; i < str.length() - 10; i++) {
            buf.append("*");
        }
        buf.append(str.substring(str.length() - 4, str.length()));
        StringBuffer result = new StringBuffer();
        String cardNumMask = buf.toString();
        for (int i = 0; i < cardNumMask.length(); i++) {
            if (i > 0 && i % 4 == 0) result.append(" ");
            result.append(cardNumMask.charAt(i));
        }
        return result.toString();
    }

    public static String maskString(String str, int headCount, int endCount) {
        if (isBlank(str)) return str;

        if (str.length() <= headCount + endCount) {
            throw new RuntimeException("字符串[" + str + "]的长度小于等于预留明文长度和[" + headCount + "][" + endCount + "]");
        }

        StringBuffer buf = new StringBuffer();
        buf.append(str.substring(0, headCount));
        for (int i = headCount; i < str.length() - endCount; i++) {
            buf.append("*");
        }
        buf.append(str.substring(str.length() - endCount));
        return buf.toString();
    }


    public static String substring(String str, String begin, String end) {
        return str.substring(str.indexOf(begin) + begin.length(), str.indexOf(end));
    }

    public static String substring(String str, int maxLength) {
        if (str != null && str.length() > maxLength)
            return str.substring(0, maxLength - 3) + "...";
        else
            return str;
    }

    public static Map<String, String> string2map(String string) {
        if (string == null || string.trim().equals("")) return new HashMap<String, String>();

        Map<String, String> map = new LinkedHashMap<String, String>();
        String[] lines = string.trim().split("\n");
        for (String line : lines) {
            if (line.startsWith("#")) continue;
            int indexOf = line.indexOf("=");
            if (indexOf < 0) continue;
            String key = line.substring(0, indexOf);
            String value = line.substring(indexOf + 1);
            if (map.get(key) != null) throw new RuntimeException("[" + string + "]中有重复项[" + key + "]");
            map.put(key.trim(), value.trim());
        }

        return map;
    }

    public static String map2string(Map<?, ?> map) {
        if (map == null || map.isEmpty()) return "";

        StringBuffer buf = new StringBuffer();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();

            if (buf.length() > 0) buf.append("\n");
            buf.append(key != null ? key.toString() : "");
            buf.append("=");
            buf.append(value != null ? value.toString() : "");
        }

        return buf.toString();
    }

    public static boolean isUrl(String str) {
        str = str.toLowerCase();
        String regex = "^((https|http|ftp|rtsp|mms)?://)"
                + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?"
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}"
                + "|"
                + "([0-9a-z_!~*'()-]+\\.)*"
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\."
                + "[a-z]{2,6})"
                + "(:[0-9]{1,4})?"
                + "((/?)|"
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        return Pattern.matches(regex, str);
    }

    public static String substringByMaxByte(String str, int maxByte) {
        try {
            int len = str.getBytes("GBK").length;
            if (len <= maxByte) return str;

            for (int i = 0; i < maxByte; i++) {
                char c = str.charAt(i);
                if (String.valueOf(c).getBytes("GBK").length > 1) {
                    maxByte--;
                }
            }
            return str.substring(0, maxByte);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static List<String> string2list(String str) {
        if (isBlank(str)) return new ArrayList<String>();

        StringTokenizer st = new StringTokenizer(str.trim(), ",\n");
        List<String> list = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            list.add(st.nextToken().trim());
        }
        return list;
    }

    public static String randomString(int length) {
        final String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(str.length());
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static String map2LinkString(Map<String, ?> params) {
        String prestr = "";
        if (params == null || params.isEmpty()) return prestr;
        ArrayList<String> keys = new ArrayList(params.keySet());
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object value = params.get(key);
            if (i == keys.size() - 1) {
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

    /**
     * 生成指定位数验证码
     */
    public static String getRandNum(int charCount) {
        String charValue = "";
        for (int i = 0; i < charCount; i++) {
            char c = (char) (randomInt(0, 10) + '0');
            charValue += String.valueOf(c);
        }
        return charValue;
    }

    private static int randomInt(int from, int to) {
        Random r = new Random();
        return from + r.nextInt(to - from);
    }

}