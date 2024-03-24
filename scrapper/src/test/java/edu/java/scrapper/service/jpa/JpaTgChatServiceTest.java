package edu.java.scrapper.service.jpa;

import edu.java.scrapper.integration.IntegrationTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {"app.database-access-type=jpa", "app.scheduler.enable=false"})
public class JpaTgChatServiceTest extends IntegrationTest {

}
