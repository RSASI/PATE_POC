package com.example.demo.EntityLayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "TeamsInformation")
public class Entitymodel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String domainUrl;
    private String landingUrl;
    private String personName;

}
