import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class tests {

    @BeforeEach
    void init() {
        // 每条用例开始前执行
        System.out.println("用例开始");
    }

    @AfterEach
    void tearDown(){
        // 每条用例结束时执行
        System.out.println("用例结束");
    }

    @BeforeAll
    static void initAll() {
        // 所有用例开始前执行
        System.out.println("开始");
    }

    @AfterAll
    static void tearDownAll() {
        // 所有用例结束时执行
        System.out.println("结束");
    }

    @Test
    @DisplayName("用例显示名字")
    void myFirstTest() {
        assertEquals(2, 1 + 1);
    }

    @Test
    @Disabled("这条用例暂时跑不过，忽略!")
    void myFailTest(){
        assertEquals(1,2);
    }

    @Test
    @DisplayName("运行一组断言")
    public void assertAllCase() {
        assertAll("groupAssert",
                () -> assertEquals(2, 1 + 1),
                () -> assertTrue(1 > 0)
        );
    }

    @Test
    @DisplayName("依赖注入1")
    public void testInfo(final TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());
    }

    @Test
    @DisplayName("依赖注入2")
    public void testReporter(final TestReporter testReporter) {
        testReporter.publishEntry("name", "Alex");
    }
}
