package nl.rockstars.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "artists")
@Builder
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer artistId;
    private String name;
    private String genre;

    /*private Artist(ArtistBuilder builder){
        this.artistId = builder.artistId;
        this.name = builder.name;
        this.genre = builder.genre;
    }

    public static ArtistBuilder builder() {
        return new ArtistBuilder();
    }

    public static class ArtistBuilder {
        private Integer artistId;
        private String name;
        private String genre;

        public ArtistBuilder withArtistId(Integer artistId) {
            this.artistId = artistId;
            return this;
        }

        public ArtistBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ArtistBuilder withGenre(String genre) {
            this.genre = genre;
            return this;
        }

        public Artist build() {
            return new Artist(this);
        }
    }*/
}
