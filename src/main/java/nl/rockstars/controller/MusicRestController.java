package nl.rockstars.controller;

import static nl.rockstars.utils.TokenGenerator.generateNewToken;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import nl.rockstars.controller.exceptions.KeyNotAuthenticatedException;
import nl.rockstars.controller.mapping.LoginRequest;
import nl.rockstars.controller.mapping.MessageResponse;
import nl.rockstars.model.Artist;
import nl.rockstars.model.Song;
import nl.rockstars.model.dto.ArtistDTO;
import nl.rockstars.model.dto.ArtistsDTO;
import nl.rockstars.model.dto.SongDTO;
import nl.rockstars.model.dto.SongsDTO;
import nl.rockstars.service.MusicService;

/**
 * Defines the REST API interface. API versioning is done with global uri
 * versioning. An alternative versioning strategy is shown on method getSongV2
 * which uses mime-based resource versioning, using
 * application/nl.rockstars.music.v2+json.
 * 
 * Authentication is done using a token which is authenticated on each method.
 * Probably better to handle by Spring Security as a cross cutting concern. (todo)
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("api/music/v1")
public class MusicRestController {
    private final MusicService musicService;
    protected static Set<String> tokens = Collections.synchronizedSet(new HashSet<>());

    protected void authenticate(String key) {
        if (!tokens.contains(key))
            throw new KeyNotAuthenticatedException();
    }

    @Operation(summary = "Login with email and password")
    @PostMapping("/login")
    ResponseEntity<MessageResponse> login(@RequestBody LoginRequest loginRequest) {
        var response = musicService.login(loginRequest);
        var authentication = generateNewToken();
        tokens.add(authentication);
        return ResponseEntity.ok().header("authentication_key", authentication).body(response);
    }

    @Operation(summary = "Get a song by its id")
    @RequestMapping(value = "/song/{id}", produces = { "application/json",
            "application/xml" }, method = RequestMethod.GET)
    Song getSong(@PathVariable Integer id, @RequestHeader("authentication_key") String key) {
        authenticate(key);
        return musicService.getSong(id);
    }

    // For demo purposes. Mime-based resource versioning. Returns song for id + 1.
    @Operation(summary = "Get a song by its id")
    @RequestMapping(value = "/song/{id}", produces = {
            "application/nl.rockstars.music.v2+json" }, method = RequestMethod.GET)
    Song getSongV2(@PathVariable Integer id, @RequestHeader("authentication_key") String key) {
        authenticate(key);
        return musicService.getSong(id + 1);
    }

    @Operation(summary = "Get an artist by its id")
    @RequestMapping(value = "/artist/{id}", produces = { "application/json",
            "application/xml" }, method = RequestMethod.GET)
    Artist getArtist(@PathVariable Integer id, @RequestHeader("authentication_key") String key) {
        authenticate(key);
        return musicService.getArtist(id);
    }

    @Operation(summary = "Get a list of songs")
    @RequestMapping(value = "/songs", produces = { "application/json", "application/xml" }, method = RequestMethod.GET)
    SongsDTO getSongs(@RequestHeader("authentication_key") String key) {
        authenticate(key);
        return new SongsDTO(musicService.getSongs());
    }

    @Operation(summary = "Get a list of artists")
    @RequestMapping(value = "/artists", produces = { "application/json",
            "application/xml" }, method = RequestMethod.GET)
    ArtistsDTO getArtists(@RequestHeader("authentication_key") String key) {
        authenticate(key);
        return new ArtistsDTO(musicService.getArtists());
    }

    @Operation(summary = "Get a list of songs by genre")
    @RequestMapping(value = "/songsByGenre/{genre}", produces = { "application/json",
            "application/xml" }, method = RequestMethod.GET)
    SongsDTO getSongsByGenre(@PathVariable String genre,
            @RequestHeader("authentication_key") String key) {
        authenticate(key);
        return new SongsDTO(musicService.getSongsByGenre(genre));
    }

    @Operation(summary = "Get a list of songs by artist id")
    @RequestMapping(value = "/songsByArtist/{id}", produces = { "application/json",
            "application/xml" }, method = RequestMethod.GET)
    SongsDTO getSongsByArtist(@PathVariable Integer id, @RequestHeader("authentication_key") String key) {
        authenticate(key);
        return new SongsDTO(musicService.getSongsByArtist(id));
    }

    @Operation(summary = "Get a list of artists which match name")
    @RequestMapping(value = "/artistsByName/{name}", produces = { "application/json",
            "application/xml" }, method = RequestMethod.GET)
    ArtistsDTO getArtistsByName(@PathVariable String name, @RequestHeader("authentication_key") String key) {
        authenticate(key);
        return new ArtistsDTO(musicService.getArtistsByName(name));
    }

    @Operation(summary = "Update song by id")
    @RequestMapping(value = "/song/{id}", produces = { "application/json",
            "application/xml" }, method = RequestMethod.PUT)
    Song updateSong(@RequestBody SongDTO songRequest, @PathVariable Integer id,
            @RequestHeader("authentication_key") String key) {
        authenticate(key);
        return musicService.updateSong(id, songRequest);
    }

    @Operation(summary = "Update artist by id")
    @RequestMapping(value = "/artist/{id}", produces = { "application/json",
            "application/xml" }, method = RequestMethod.PUT)
    Artist updateArtist(@RequestBody ArtistDTO artistRequest, @PathVariable Integer id,
            @RequestHeader("authentication_key") String key) {
        authenticate(key);
        return musicService.updateArtist(id, artistRequest);
    }

    @Operation(summary = "Create a song")
    @RequestMapping(value = "/song", produces = { "application/json", "application/xml" }, method = RequestMethod.POST)
    Song updateSong(@RequestBody SongDTO songRequest, @RequestHeader("authentication_key") String key) {
        authenticate(key);
        return musicService.createSong(songRequest);
    }

    @Operation(summary = "Create an artist")
    @RequestMapping(value = "/artist", produces = { "application/json",
            "application/xml" }, method = RequestMethod.POST)
    Artist updateArtist(@RequestBody ArtistDTO artistRequest, @RequestHeader("authentication_key") String key) {
        authenticate(key);
        return musicService.createArtist(artistRequest);
    }

    @Operation(summary = "Delete a song by id")
    @RequestMapping(value = "/song/{id}", produces = { "application/json",
            "application/xml" }, method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    MessageResponse deleteSong(@PathVariable Integer id, @RequestHeader("authentication_key") String key) {
        authenticate(key);
        return musicService.deleteSong(id);
    }

    @Operation(summary = "Delete an artist by id")
    @RequestMapping(value = "/artist/{id}", produces = { "application/json",
            "application/xml" }, method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    MessageResponse deleteArtist(@PathVariable Integer id, @RequestHeader("authentication_key") String key) {
        authenticate(key);
        return musicService.deleteArtist(id);
    }

}