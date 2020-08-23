package nl.rockstars.config;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles("test")
@SpringBootTest
@TestPropertySource("classpath:application.properties")
public class RockstarPropertiesIntegrationTest {
 
    @Autowired
    private RockstarProperties rockstarProperties;
 
    @Test
    public void whenSimplePropertyQueriedThenReturnsPropertyValue() 
      throws Exception {
        assertTrue(StringUtils.isNotBlank(rockstarProperties.getBaseUrl()));
        assertTrue(StringUtils.isNotBlank(rockstarProperties.getSongFile()));
    }  
}