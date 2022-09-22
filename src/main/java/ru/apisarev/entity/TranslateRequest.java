package ru.apisarev.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TranslateRequest {

    @Id
    @GeneratedValue
    private Long id;

    private String request;

    private String result;

    private LocalDateTime time;

    private String remoteAddress;

    private String targetLangCode;

}