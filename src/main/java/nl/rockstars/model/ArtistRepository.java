package nl.rockstars.model;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Queries for the artists table
 */
@Repository
public interface ArtistRepository extends JpaRepository<Artist, Integer> {
    @Query("select a from Artist a")
    List<Artist> findAll();
    @Query("select a from Artist a where a.artistId = :id")
    Optional<Artist> findById(Integer id);
    @Query("select a from Artist a where a.name = :name")
    Optional<Artist> findOptionalByName(String name);
    @Query("select a from Artist a where a.name like %:name%")
    List<Artist> findAllByName(String name);
}




