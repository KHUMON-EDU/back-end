package com.teamh.khumon.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
@Getter
public enum MediaFileType {

    PDF("pdf"),
    TEXT("txt"),
    VIDEO("mp4");

    private final String fileType;

}