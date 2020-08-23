package nl.rockstars.model;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
public class SongRepositoryIntegrationTest {
    @Autowired
    private SongRepository songRepository;

    @Test
    void givenSongWhenSavedThenFoundById(){
        // Given song
        var songName = "songName";
        var song = Song.builder().name(songName).build();
        //When an artist is saved
        var savedSong = songRepository.save(song);
        // Then can be found by name
        assertTrue(songRepository.findById(savedSong.getSongId()).isPresent());
    }

}