package com.ejubc.commons.yilou.utils;


import java.util.Optional;

/**
 * 字符串工具
 *
 * @author xy
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class StringUtil {


    /**
     * 获取字符串子串，从key到结尾
     * 例如：file.jpg，关键词.，将返回jpg
     * @param str 源串，不做参数检查
     * @param key 子串开头，不做参数检查
     * @return 子串，如不存在返回空串
     */
    public static String substringToEnd(String str, String key) {
        int keyIndex = str.indexOf(key);
        if (keyIndex == -1) {
            return "";
        }
        return str.substring(keyIndex + 1);
    }

    /**
     * 获取两个字符串子串，关键文本前，和关键文本后
     * 例如：file.jpg，关键词.，将返回["file", "jpg"]
     * 注意，如果是file.jpg.abc 将返回["file", "jpg.abc"]
     * @param str 源串，不做参数检查
     * @param key 子串开头，不做参数检查
     * @return 子串，如不存在返回空，存在则必然返回有两个元素的数组
     */
    public static Optional<String[]> substringToTopAndEnd(String str, String key) {
        int keyIndex = str.indexOf(key);
        if (keyIndex == -1) {
            return Optional.empty();
        }
        return Optional.of(new String[]{str.substring(0, keyIndex), str.substring(keyIndex + 1)});
    }

    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }
}
