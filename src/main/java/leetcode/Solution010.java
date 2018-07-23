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
            sum /= 10;
            if (n1 != null) {
                sum += n1.getVal();
                n1 = n1.getNext();
            }
            if (n2 != null) {
                sum += n2.getVal();
                n2 = n2.getNext();
            }
            t.setNext(new ListNode(sum % 10));
            t = t.getNext();
        }
        if (sum / 10 != 0) {
            t.setNext(new ListNode(1));
        }
        return node.getNext();
    }
}
