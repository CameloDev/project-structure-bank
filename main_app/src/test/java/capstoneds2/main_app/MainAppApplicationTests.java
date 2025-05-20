package capstoneds2.main_app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = MainAppApplication.class)
@ActiveProfiles("test")
public class MainAppApplicationTests {
    @Test
    void contextLoads() {
    }
}

