package leetcode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestSolution010 {

    @Test
    @DisplayName("Two Sum")
    void testTwoNum()
    {
        int[] nums=new int[]{2,7,11, 15};
        int target = 9;

        Solution010 solution = new  Solution010();
        int[] result =  solution.twoSum(nums,target);
        assertEquals(result[0],0);
        assertEquals(result[1],1);
    }
}
