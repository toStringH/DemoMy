package com.kunhe.mybasedemo.common;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hekun on 2017/3/31.
 * Desc: 对含有数字的字符串排序的比较器
 */
public class NumberComparator implements Comparator<String> {

    private static final int DEFAULT_TRANSLATE_CODE = 1;

    private boolean ignoreCase = true;
    /**
     * 中文数字一到十对应的ASCII码
     */
    private int[] mAsciiCode = new int[]{19968, 20108, 19977, 22235, 20116, 20845, 19971, 20843, 20061, 21313};
    private Map<Integer, Integer> mMap = new HashMap<>();

    public NumberComparator() {
        this(true);
    }

    public NumberComparator(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
        for (int i = 0; i < mAsciiCode.length; i++) {
            mMap.put(mAsciiCode[i], i + DEFAULT_TRANSLATE_CODE);
        }
    }

    @Override
    public int compare(String o1, String o2) {
        if (ignoreCase) {
            o1 = o1.toLowerCase();
            o2 = o2.toLowerCase();
        }

        for (int i = 0; i < o1.length(); i++) {
            if (i == o1.length() && i < o2.length()) {
                return -1;
            } else if (i == o2.length() && i < o1.length()) {
                return 1;
            }

            char ch1 = o1.charAt(i);
            char ch2 = o2.charAt(i);

            if (ch1 >= '0' && ch2 <= '9') {
                int i1 = getNumber(o1.substring(i));
                int i2 = getNumber(o2.substring(i));
                if (i1 == i2) {
                    continue;
                } else {
                    return i1 - i2;
                }
            } else if (ch1 != ch2) {
                Integer i1 = mMap.get((int) ch1);
                Integer i2 = mMap.get((int) ch2);
                if (i1 != null && i2 != null) {
                    int cn1 = getCnNumber(o1.substring(i));
                    int cn2 = getCnNumber(o2.substring(i));
                    return cn1 - cn2;
                }
                return ch1 - ch2;
            }
        }
        return 0;
    }

    /**
     * 对含有数字的字符串排序
     *
     * @param str
     * @return
     */
    private int getNumber(String str) {
        int num = Integer.MAX_VALUE;
        int bits = 0;

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) >= '0' && str.charAt(i) <= '9') {
                bits++;
            } else {
                break;
            }
        }

        if (bits > 0) {
            num = Integer.parseInt(str.substring(0, bits));
        }
        return num;
    }

    /**
     * 对含有中文数字的字符串排序
     * 只支持一百以内的数字
     *
     * @param str 含有中文数字的格式 （一，二，...，十，一十一，一十二，...，二十，二十一，...）
     * @return 中文数字对应的 1，2 ，...，10，11，12，...，20，21，...
     */
    private int getCnNumber(String str) {
        int num = Integer.MAX_VALUE;
        int bits = 0;

        for (int i = 0; i < str.length(); i++) {
            Integer code = mMap.get((int) str.charAt(i));
            if (code != null) {
                bits++;
            } else {
                break;
            }
        }

        if (bits == 1) {
            num = mMap.get((int) str.charAt(0));
        } else if (bits == 2) {
            num = mMap.get((int) str.charAt(0)) * 10;
        } else if (bits == 3) {
            String cnNumber = str.substring(0, bits);
            num = mMap.get((int) str.charAt(0)) * 10;
            num += mMap.get((int) cnNumber.charAt(cnNumber.length() - 1));
        }

        return num;
    }
}
