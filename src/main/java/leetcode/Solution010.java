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
}
