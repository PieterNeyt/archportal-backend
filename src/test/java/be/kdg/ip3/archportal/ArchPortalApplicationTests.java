package be.kdg.ip3.archportal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestContainersConfig.class)
class ArchPortalApplicationTests {

    @Test
    void contextLoads() {
    }

}
