package leetcode;

import common.beans.ListNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestSolution010 {

    private Solution010 solution = new Solution010();

    @Test
    @DisplayName("001 Two Sum")
    void testTwoNum()
    {
        int[] nums=new int[]{2,7,11, 15};
        int target = 9;

        int[] result =  solution.twoSum(nums,target);
        assertEquals(result[0],0);
        assertEquals(result[1],1);
    }

    @Test
    @DisplayName("002 Add Two Numbers")
    void testAddTwoNumbers()
    {
        ListNode node1 = ListNode.createTestData("2,4,3");
        ListNode node2 = ListNode.createTestData("5,6,4");

        ListNode tempNode = solution.addTwoNumbers(node1,node2);

        assertEquals(ListNode.toPrint(tempNode),"[7,0,8]");
    }

    @Test
    @DisplayName("003 Longest Substring Without Repeating Characters")
    void testLengthOfLongestSubstring()
    {
        String str ="abcabcbb";
        int length = solution.lengthOfLongestSubstring(str);
        assertEquals(length,3);

        str ="bbbbb";
        length = solution.lengthOfLongestSubstring(str);
        assertEquals(length,1);

        str ="pwwkew";
        length = solution.lengthOfLongestSubstring(str);
        assertEquals(length,3);
    }

}
