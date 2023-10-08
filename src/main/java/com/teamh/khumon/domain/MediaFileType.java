package com.teamh.khumon.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
@Getter
public enum MediaFileType {

    PDF("PDF"),
    TEXT("TEXT"),
    VIDEO("VIDEO"),
    ETC("ETC");

    private final String fileType;

}