package nl.rockstars.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@XmlRootElement
@Entity
@Table(name = "songs")
@Builder(toBuilder = true)
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    @Getter
    private Integer songId;
    @Getter
    private String name;
    @Getter
    private Integer year;
    @Getter
    private String shortName;
    @Getter
    private Integer bpm;
    @Getter
    private Integer duration;
    @Getter
    private String spotifyId;
    @Getter
    private String album;
    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn(name = "artistId")
    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    @Getter
    private Artist artist;
}