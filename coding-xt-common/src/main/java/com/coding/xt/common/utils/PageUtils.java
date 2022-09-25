package com.coding.xt.common.utils;

import java.util.Arrays;
import java.util.List;

public class PageUtils {
        /**
         * 计算总页数
         *
         * @param sumCount
         * @param pageSize
         * @return
         */
        public static int pageCount(int sumCount, int pageSize) {
            int i = sumCount / pageSize;
            if (sumCount % pageSize > 0) {
                i++;
            }
            return i;
        }

        /**
         * 获取相应页数的数据
         *
         * @param data
         * @param pageSize
         * @return
         */
        public static <T> List<T> getOnePage(List<T> data, int page, int pageSize) {
            if (data.size() <= pageSize) {
                return data;
            }
            if (data.size() > ((page - 1) * pageSize) && data.size() <= (page * pageSize)) {
                return data.subList((page - 1) * pageSize, data.size());
            }
            if (data.size() >= page * pageSize) {
                return data.subList((page - 1) * pageSize, page * pageSize);
            }
            return data;
        }

        public static void main(String[] args) {
            System.out.println(getOnePage(Arrays.asList(1, 2, 3, 4, 5), 3, 2));
            System.out.println(getOnePage(Arrays.asList(1, 2, 3, 4, 5), 2, 2));
            System.out.println(getOnePage(Arrays.asList(1, 2, 3, 4, 5), 1, 2));
            System.out.println(getOnePage(Arrays.asList(1, 2, 3, 4, 5), 1, 3));
            System.out.println(getOnePage(Arrays.asList(1, 2, 3, 4, 5), 2, 3));
            System.out.println(getOnePage(Arrays.asList(1, 2, 3, 4, 5), 3, 3));
        }

}
