package nl.rockstars.service;

import static nl.rockstars.config.GlobalStringResources.CONSTRAINT_VIOLATION_DELETE;
import static nl.rockstars.config.GlobalStringResources.DELETE_SUCCESFUL;
import static nl.rockstars.config.GlobalStringResources.DELETE_UNSUCCESFUL;
import static nl.rockstars.config.GlobalStringResources.ENTITY_NOT_FOUND;
import static nl.rockstars.config.GlobalStringResources.LOGIN_STATUS_SUCCESFUL;
import static nl.rockstars.config.GlobalStringResources.USER_NOT_FOUND;
import static nl.rockstars.config.GlobalStringResources.USER_NOT_VALIDATED;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import nl.rockstars.controller.exceptions.EntityNotDeletedException;
import nl.rockstars.controller.exceptions.EntityNotFoundException;
import nl.rockstars.controller.exceptions.UserNotFoundException;
import nl.rockstars.controller.exceptions.UserNotValidatedException;
import nl.rockstars.controller.mapping.LoginRequest;
import nl.rockstars.controller.mapping.MessageResponse;
import nl.rockstars.model.Artist;
import nl.rockstars.model.ArtistRepository;
import nl.rockstars.model.Song;
import nl.rockstars.model.Song.SongBuilder;
import nl.rockstars.model.SongRepository;
import nl.rockstars.model.UserRepository;
import nl.rockstars.model.dto.ArtistDTO;
import nl.rockstars.model.dto.SongDTO;

/**
 * The Music Service contains the business logic of the application and is
 * mostly called bij the REST api. Exceptions thrown are handled by
 * MusicControllerExceptionHandler for methods called from the REST api.
 * 
 * Spring Cache provides caching using the fallback cache provider
 * (ConcurrentHashMap).
 */
@Service
@RequiredArgsConstructor
public class MusicService {

  private final UserRepository userRepository;
  private final SongRepository songRepository;
  private final ArtistRepository artistRepository;
  private final PasswordEncoder passwordEncoder;

  public MessageResponse login(LoginRequest request) {
    userRepository.findOptionalByEmail(request.getEmail()).ifPresentOrElse(user -> {
      if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
        throw new UserNotValidatedException(USER_NOT_VALIDATED + request.getEmail());
    }, () -> {
      throw new UserNotFoundException(USER_NOT_FOUND + request.getEmail());
    });
    return new MessageResponse(LOGIN_STATUS_SUCCESFUL);
  }

  @Cacheable("song")
  public Song getSong(Integer id) {
    return songRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND + id));
  }

  @Cacheable("artist")
  public Artist getArtist(Integer id) {
    return artistRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND + id));
  }

  @Cacheable("songs")
  public List<Song> getSongs() {
    return songRepository.findAll();
  }

  @Cacheable("artists")
  public List<Artist> getArtists() {
    return artistRepository.findAll();
  }

  @Cacheable("songs")
  public List<Song> getSongsByGenre(String genre) {
    return songRepository.findAllByGenre(genre);
  }

  @Cacheable("songs")
  public List<Song> getSongsByArtist1(Integer id) {
    return songRepository.findAllByArtist(id);
  }

  @Cacheable("songs")
  public List<Song> getSongsByArtist(Integer id) {
    return new ArrayList<>(artistRepository.findById(id).get().getSongs());
  }

  @Cacheable("artists")
  public List<Artist> getArtistsByName(String name) {
    return artistRepository.findAllByName(name);
  }

  @CachePut("song")
  public Song updateSong(Integer id, SongDTO songDTO) {
    var song = songRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND + id));
    return songRepository.save(transferSong(songDTO, song));
  }

  // Updates song fields except artist
  protected Song transferSong(SongDTO songDTO, Song song) {
    var builder = song.toBuilder();
    if (StringUtils.hasLength(songDTO.getArtist()) && !song.getArtist().getName().equals(songDTO.getArtist()))
      builder.artist(insertArtist(songDTO.getArtist()));
    if (StringUtils.hasLength(songDTO.getName()))
      builder.name(songDTO.getName());
    if (StringUtils.hasLength(songDTO.getShortName()))
      builder.shortName(songDTO.getShortName());
    if (StringUtils.hasLength(songDTO.getSpotifyId()))
      builder.spotifyId(songDTO.getSpotifyId());
    if (StringUtils.hasLength(songDTO.getAlbum()))
      builder.album(songDTO.getAlbum());
    if (songDTO.getYear() != null)
      builder.year(songDTO.getYear());
    if (songDTO.getBpm() != null)
      builder.bpm(songDTO.getBpm());
    if (songDTO.getDuration() != null)
      builder.duration(songDTO.getDuration());
    return builder.build();
  }

  protected Artist transferArtist(ArtistDTO artistDTO, Artist artist) {
    var builder = artist.toBuilder();
    if (StringUtils.hasLength(artistDTO.getName()))
      builder.name(artistDTO.getName());
    if (StringUtils.hasLength(artistDTO.getGenre()))
      builder.genre(artistDTO.getGenre());
    return builder.build();
  }


  @CachePut("artist")
  public Artist updateArtist(Integer id, ArtistDTO artistDTO) {
    var artist = artistRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND + id));
    return artistRepository.save(transferArtist(artistDTO, artist));
  }

  public Artist insertArtist(String artistName, String genre) {
    return artistRepository.findOptionalByName(artistName)
        .orElseGet(() -> artistRepository.save(Artist.builder().name(artistName).genre(genre).build()));
  }

  public Artist insertArtist(String artistName) {
    return artistRepository.findOptionalByName(artistName)
        .orElseGet(() -> artistRepository.save(Artist.builder().name(artistName).build()));
  }

  @CacheEvict(cacheNames = "songs", allEntries = true)
  public Song createSong(SongDTO song) {
    var artist = insertArtist(song.getArtist(), song.getGenre());
    return songRepository
        .save(Song.builder().name(song.getName()).year(song.getYear()).shortName(song.getShortName()).bpm(song.getBpm())
            .duration(song.getDuration()).spotifyId(song.getSpotifyId()).album(song.getAlbum()).artist(artist).build());
  }

  @CacheEvict(cacheNames = "artists", allEntries = true)
  public Artist createArtist(ArtistDTO artist) {
    return artistRepository.save(Artist.builder().name(artist.getName()).genre(artist.getGenre()).build());
  }

  @CacheEvict(cacheNames = "songs", allEntries = true)
  public MessageResponse deleteSong(Integer id) {
    return deleteIdFromRepository(id, songRepository);
  }

  @CacheEvict(cacheNames = "artists", allEntries = true)
  public MessageResponse deleteArtist(Integer id) {
    return deleteIdFromRepository(id, artistRepository);
  }

  protected MessageResponse deleteIdFromRepository(Integer id, JpaRepository<?, Integer> repository) {
    repository.findById(id).ifPresentOrElse(o -> {
      try {
        repository.deleteById(id);
      } catch (DataIntegrityViolationException e) {
        throw new EntityNotDeletedException(CONSTRAINT_VIOLATION_DELETE);
      }
    }, () -> {
      throw new EntityNotDeletedException(ENTITY_NOT_FOUND + id);
    });

    repository.findById(id).ifPresent(o -> {
      throw new EntityNotDeletedException(DELETE_UNSUCCESFUL + id);
    });

    return new MessageResponse(DELETE_SUCCESFUL + id);
  }

}