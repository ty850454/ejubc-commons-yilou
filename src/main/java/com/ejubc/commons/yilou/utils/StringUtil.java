package com.ejubc.commons.yilou.utils;


import java.util.Optional;

/**
 * 字符串工具
 *
 * @author xy
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class StringUtil {
    public static final String EMPTY = "";

    /**
     * 获取字符串子串，从key到结尾
     * 例如：file.jpg，关键词.，将返回jpg
     * @param str 源串，不做参数检查
     * @param key 子串开头，不做参数检查
     * @return 子串，如不存在返回空串
     * @see StringUtil#sub(java.lang.CharSequence, java.lang.String, java.lang.String)
     */
    @Deprecated
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

    /**
     * 改进JDK subString<br>
     * index从0开始计算，最后一个字符为-1<br>
     * 如果from和to位置一样，返回 "" <br>
     * 如果from或to为负数，则按照length从后向前数位置，如果绝对值大于字符串长度，则from归到0，to归到length<br>
     * 如果经过修正的index中from大于to，则互换from和to example: <br>
     * abcdefgh 2 3 =》 c <br>
     * abcdefgh 2 -3 =》 cde <br>
     *
     * @param str String
     * @param fromIndex 开始的index（包括）
     * @param toIndex 结束的index（不包括）
     * @return 字串
     */
    public static String sub(CharSequence str, int fromIndex, int toIndex) {
        if (isEmpty(str)) {
            return null;
        }
        int len = str.length();

        if (fromIndex < 0) {
            fromIndex = len + fromIndex;
            if (fromIndex < 0) {
                fromIndex = 0;
            }
        } else if (fromIndex > len) {
            fromIndex = len;
        }

        if (toIndex < 0) {
            toIndex = len + toIndex;
            if (toIndex < 0) {
                toIndex = len;
            }
        } else if (toIndex > len) {
            toIndex = len;
        }

        if (toIndex < fromIndex) {
            int tmp = fromIndex;
            fromIndex = toIndex;
            toIndex = tmp;
        }

        if (fromIndex == toIndex) {
            return EMPTY;
        }

        return str.toString().substring(fromIndex, toIndex);
    }

    /**
     * 获取fromStr与toStr之间的字符串，
     * @param cs null安全
     * @param fromStr 可以是null
     * @param toStr 可以是null
     * @return
     */
    public static String sub(CharSequence cs, String fromStr, String toStr) {
        if (isEmpty(cs)) {
            return null;
        }
        String str = cs.toString();
        int fromIndex, toIndex;

        if (fromStr == null || fromStr.length() == 0) {
            fromIndex = 0;
        } else {
            fromIndex = str.indexOf(fromStr);
            if (fromIndex == -1) {
                fromIndex = 0;
            } else {
                fromIndex += 1;
            }
        }
        if (toStr == null) {
            toIndex = str.length();
        } else {
            if (fromIndex != 0) {
                toIndex = str.indexOf(toStr, fromIndex);
            } else {
                toIndex = str.indexOf(toStr);
            }
            if (toIndex == -1) {
                toIndex = str.length();
            }
        }

        if (fromIndex == toIndex) {
            return EMPTY;
        }

        return str.substring(fromIndex, toIndex);
    }

    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }
}
