package nl.rockstars.model;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
public class ArtistRepositoryIntegrationTest {
    @Autowired
    private ArtistRepository artistRepository;

    @Test
    void givenArtistWhenSavedThenFoundByName(){
        // Given artist
        var artist = Artist.builder().name("artist").build();
        //When an artist is saved
        artistRepository.save(artist);
        // Then can be found by name
        assertTrue(artistRepository.findOptionalByName("artist").isPresent());
    }

}