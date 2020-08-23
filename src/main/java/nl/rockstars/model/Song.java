package nl.rockstars.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@XmlRootElement
@Entity
@Table(name = "songs")
@Builder
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer songId;
    private String name;
    private Integer year;
    private String shortName;
    private Integer bpm;
    private Integer duration;
    private String spotifyId;
    private String album;
    @ManyToOne
    @JoinColumn(name = "artistId")
    private Artist artist;

    /*private Song(SongBuilder builder) {
        this.songId = builder.songId;
        this.name = builder.name;
        this.year = builder.year;
        this.shortName = builder.shortName;
        this.bpm = builder.bpm;
        this.duration = builder.duration;
        this.spotifyId = builder.spotifyId;
        this.album = builder.album;
        this.artist = builder.artist;
    }

    public static SongBuilder builder() {
        return new SongBuilder();
    }

    public static class SongBuilder {
        private Integer songId;
        private String name;
        private Integer year;
        private String shortName;
        private Integer bpm;
        private Integer duration;
        private String spotifyId;
        private String album;
        private Artist artist;

        public SongBuilder withsongId(Integer songId) {
            this.songId = songId;
            return this;
        }

        public SongBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public SongBuilder withYear(Integer year) {
            this.year = year;
            return this;
        }

        public SongBuilder withShortName(String shortName) {
            this.shortName = shortName;
            return this;
        }

        public SongBuilder withBpm(Integer bpm) {
            this.bpm = bpm;
            return this;
        }

        public SongBuilder withDuration(Integer duration) {
            this.duration = duration;
            return this;
        }

        public SongBuilder withSpotifyId(String spotifyId) {
            this.spotifyId = spotifyId;
            return this;
        }

        public SongBuilder withAlbum(String album) {
            this.album = album;
            return this;
        }

        public SongBuilder withArtist(Artist artist) {
            this.artist = artist;
            return this;
        }

        public Song build() {
            return new Song(this);
        }
    }*/
}