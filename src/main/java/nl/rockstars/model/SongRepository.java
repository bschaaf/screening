package nl.rockstars.model;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Queries for the songs table
 */
@Repository
public interface SongRepository extends JpaRepository<Song, Integer> {
    @Query("select s from Song s")
    List<Song> findAll();
    @Query("select s from Song s where songId = :id")
    Optional<Song> findById(Integer id);
    @Query("select s from Artist a, Song s where s.artist.artistId = a.artistId and a.genre = :genre")
    List<Song> findAllByGenre(String genre);
    @Query("select s from Song s where s.artist.artistId = :id")
    List<Song> findAllByArtist(Integer id);
}