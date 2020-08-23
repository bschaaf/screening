package nl.rockstars.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongDTO {
    @JsonProperty("Id")
    private Integer id;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Year")
    private Integer year;
    @JsonProperty("Artist")
    private String artist;
    @JsonProperty("Shortname")
    private String shortName;
    @JsonProperty("Bpm")
    private Integer bpm;
    @JsonProperty("Duration")
    private Integer duration;
    @JsonProperty("Genre")
    private String genre;
    @JsonProperty("SpotifyId")
    private String spotifyId;
    @JsonProperty("Album")
    private String album;
}