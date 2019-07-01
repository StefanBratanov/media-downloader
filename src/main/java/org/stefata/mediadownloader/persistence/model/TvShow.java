package org.stefata.mediadownloader.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TV_SHOWS")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TvShow implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "TITLE", unique = true)
    private String title;

}
