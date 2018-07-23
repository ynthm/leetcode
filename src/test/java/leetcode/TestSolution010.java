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
        ListNode tempNode = null;
        ListNode node1 = new ListNode(2);
        tempNode =new ListNode(4);
        node1.setNext(tempNode);
        tempNode.setNext(new ListNode(3));

        ListNode node2 = new ListNode(5);
        tempNode =new ListNode(6);
        node2.setNext(tempNode);
        tempNode.setNext(new ListNode(4));

        tempNode = solution.addTwoNumbers(node1,node2);


        assertEquals(tempNode.getVal(),7);
        tempNode =tempNode.getNext();
        assertEquals(tempNode.getVal(),0);
        tempNode =tempNode.getNext();
        assertEquals(tempNode.getVal(),8);
    }

}
