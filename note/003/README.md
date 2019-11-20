# [Longest Substring Without Repeating Characters][title]

## Description

Given a string, find the length of the **longest substring** without repeating characters.

**Examples:**

Given `"abcabcbb"`, the answer is `"abc"`, which the length is 3.

Given `"bbbbb"`, the answer is `"b"`, with the length of 1.

Given `"pwwkew"`, the answer is `"wke"`, with the length of 3. Note that the answer must be a **substring**, `"pwke"` is a *subsequence* and not a substring.

**Tags:** Hash Table, Two Pointers, String

```java
class Solution {
    public int lengthOfLongestSubstring(String s) {   
    }
}
```



给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。

示例 1:

```
输入: "abcabcbb"
输出: 3 
解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
```


示例 2:

```
输入: "bbbbb"
输出: 1
解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
```


示例 3:

```
输入: "pwwkew"
输出: 3
解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
     请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
```



## 思路

题意是计算不带重复字符的最长子字符串的长度，，遍历该字符串，获取到上次出现的最大索引（只能向前，不能退后），与当前的索引做差获取的就是本次所需长度，从中迭代出最大值就是最终答案。

- 顺序遍历字符串，记录每个不同字符最近一次索引，如果遇到相同字符就计算长度，找出最大的长度。
- 开辟一个 hash 数组来存储该字符上次出现的位置，比如 `hash[a] = 3` 就是代表 `a` 字符前一次出现的索引在 3
- 临时变量 max 记录最大值，遇到相同zifu计算当前



[title]: https://leetcode.com/problems/longest-substring-without-repeating-characters