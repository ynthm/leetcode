package leetcode;

import common.beans.ListNode;

import java.util.HashMap;
import java.util.Map;

public class Solution010 {

    public int[] twoSum(int[] nums, int target) {
        int len = nums.length;
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < len; ++i) {
            if (map.containsKey(nums[i])) {
                return new int[]{map.get(nums[i]), i};
            }
            map.put(target - nums[i], i);
        }
        return null;
    }

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode node = new ListNode(0);
        ListNode n1 = l1, n2 = l2, t = node;
        int sum = 0;
        while (n1 != null || n2 != null) {
            // 上次求和是否进位
            sum /= 10;
            if (n1 != null) {
                sum += n1.getVal();
                n1 = n1.getNext();
            }
            if (n2 != null) {
                sum += n2.getVal();
                n2 = n2.getNext();
            }
            // 包含进位求和取余即为下一个节点
            t.setNext(new ListNode(sum % 10));
            t = t.getNext();
        }
        // 最后一次遍历是否需要进位
        if (sum / 10 != 0) {
            t.setNext(new ListNode(1));
        }
        return node.getNext();
    }

    public int lengthOfLongestSubstring(String s) {
        int len;
        if (s == null || (len = s.length()) == 0) {
            return 0;
        }

        int preP = 0, max = 0;
        int[] hash = new int[128];
        for (int i = 0; i < len; ++i) {
            char c = s.charAt(i);
            //
            if (hash[c] > preP) {
                preP = hash[c];
            }

            // 计算最长子串的长度
            int l = i - preP;
            // 记录上一次索引
            hash[c] = i;
            // 取最大值
            if (l > max) {
                max = l;
            }
        }

        return max;
    }

    public int lengthOfLongestSubstring1(String s) {
        int n = s.length(), ans = 0;
        int[] index = new int[128]; // current index of character
        // try to extend the range [i, j]
        for (int j = 0, i = 0; j < n; j++) {
            i = Math.max(index[s.charAt(j)], i);
            ans = Math.max(ans, j - i + 1);
            index[s.charAt(j)] = j + 1;
        }
        return ans;
    }

    public double findMedianSortedArrays(int[] nums1, int[] nums2) {


        return 1d;
    }

    /**
     * 很自然地想到归并排序，再取中间数，但是是nlogn的复杂度，题目要求logn
     * 所以要用二分法来巧妙地进一步降低时间复杂度
     * 思想就是利用总体中位数的性质和左右中位数之间的关系来把所有的数先分成两堆，然后再在两堆的边界返回答案
     *
     * @param A
     * @param B
     * @return
     */
    public double findMedianSortedArrays1(int[] A, int[] B) {
        int m = A.length;
        int n = B.length;
        // 让B成为长的数组
        if (m > n) { // to ensure m<=n
            int[] temp = A;
            A = B;
            B = temp;
            int tmp = m;
            m = n;
            n = tmp;
        }

        if (n == 0) {
            System.out.println("two arrays are empty");
        }

        int iMin = 0, iMax = m, halfLen = (m + n + 1) / 2;
        while (iMin <= iMax) {
            int i = (iMin + iMax) / 2;
            int j = halfLen - i;

            if (i < iMax && B[j - 1] > A[i]) {
                iMin = i + 1; // i is too small
            } else if (i > iMin && A[i - 1] > B[j]) {
                iMax = i - 1; // i is too big
            } else { // i is perfect
                int maxLeft = 0;
                if (i == 0) {
                    maxLeft = B[j - 1];
                } else if (j == 0) {
                    maxLeft = A[i - 1];
                } else {
                    maxLeft = Math.max(A[i - 1], B[j - 1]);
                }

                // 前面也提过，因为取中间的时候用的是向下取整，所以如果总数是奇数的话，
                // 应该是右边个数多一些，边界的minright就是中位数
                if ((m + n) % 2 == 1) {
                    return maxLeft;
                }

                int minRight = 0;
                if (i == m) {
                    minRight = B[j];
                } else if (j == n) {
                    minRight = A[i];
                } else {
                    minRight = Math.min(B[j], A[i]);
                }

                return (maxLeft + minRight) / 2.0;
            }
        }
        return 0.0;
    }
}
