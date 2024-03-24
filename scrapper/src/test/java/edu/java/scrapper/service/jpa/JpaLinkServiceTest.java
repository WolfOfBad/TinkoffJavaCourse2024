package edu.java.scrapper.service.jpa;

import edu.java.scrapper.integration.IntegrationTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {"app.database-access-type=jpa"})
@ExtendWith(MockitoExtension.class)
public class JpaLinkServiceTest extends IntegrationTest {

}

