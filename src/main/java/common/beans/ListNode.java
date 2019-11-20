package common.beans;

/**
 * @author : Ynthm
 */
public class ListNode {
    private int val;

    private ListNode next;

    public ListNode(int x) {
        val = x;
    }

    public ListNode getNext() {
        return next;
    }

    public void setNext(ListNode next) {
        this.next = next;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    /**
     * 创建测试数据
     *
     * @param data 2,4,3
     * @return {@link ListNode}
     */
    public static ListNode createTestData(String data) {
        String[] split = data.split(",");
        int len = split.length;
        ListNode[] listNode = new ListNode[len + 1];
        listNode[0] = new ListNode(Integer.valueOf(split[0]));
        for (int i = 1; i < len; i++) {
            listNode[i] = new ListNode(Integer.valueOf(split[i]));
            listNode[i - 1].next = listNode[i];
        }
        return listNode[0];
    }

    public static String toPrint(ListNode listNode) {
        if (listNode == null) {
            return "null";
        }
        StringBuilder str = new StringBuilder("[" + String.valueOf(listNode.val));
        ListNode p = listNode.next;
        while (p != null) {
            str.append(",").append(String.valueOf(p.val));
            p = p.next;
        }

        return str.append("]").toString();
    }
}
