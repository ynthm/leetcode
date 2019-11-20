package leetcode;

import common.beans.ListNode;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestSolution010 {

    private Solution010 solution = new Solution010();

    @Order(1)
    @Test
    @DisplayName("001 Two Sum")
    void testTwoNum() {
        int[] nums = new int[]{2, 7, 11, 15};
        int target = 9;

        int[] result = solution.twoSum(nums, target);
        assertEquals(result[0], 0);
        assertEquals(result[1], 1);
    }

    @Test
    @DisplayName("002 Add Two Numbers")
    void testAddTwoNumbers() {
        ListNode node1 = ListNode.createTestData("2,4,3");
        ListNode node2 = ListNode.createTestData("5,6,4");
        ListNode node3 = ListNode.createTestData("5,6,9");

        ListNode tempNode = solution.addTwoNumbers(node1, node2);

        assertEquals(ListNode.toPrint(tempNode), "[7,0,8]");


        ListNode tempNode1 = solution.addTwoNumbers(node1, node3);

        assertEquals(ListNode.toPrint(tempNode1), "[7,0,3,1]");
    }

    @Test
    @DisplayName("003 Longest Substring Without Repeating Characters")
    void testLengthOfLongestSubstring() {
        String str = "abcabcbb";
        int length = solution.lengthOfLongestSubstring(str);
        assertEquals(length, 3);

        str = "bbbbb";
        length = solution.lengthOfLongestSubstring(str);
        assertEquals(length, 1);

        str = "pwwkew";
        length = solution.lengthOfLongestSubstring(str);
        assertEquals(length, 3);
    }

    @Test
    void testFindMedianSortedArrays() {
        int[] nums1 = new int[]{1, 3};
        int[] nums2 = new int[]{2};

        int[] nums3 = new int[]{1, 2};
        int[] nums4 = new int[]{3, 4};

        assertEquals(solution.findMedianSortedArrays(nums1, nums2), 2.0d);
        assertEquals(solution.findMedianSortedArrays(nums3, nums4), 2.5d);

    }

}
