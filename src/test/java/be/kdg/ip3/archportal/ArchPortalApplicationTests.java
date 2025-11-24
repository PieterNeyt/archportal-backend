package be.kdg.ip3.archportal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import({TestContainersConfig.class, MockJwtDecoderConfig.class})
class ArchPortalApplicationTests {

    @Test
    void contextLoads() {
    }

}
