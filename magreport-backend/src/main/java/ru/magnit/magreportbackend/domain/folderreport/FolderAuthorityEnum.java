package ru.magnit.magreportbackend.domain.folderreport;

public enum FolderAuthorityEnum {
    NONE,
    READ,
    WRITE;

    public static FolderAuthorityEnum getById(long id){
        return FolderAuthorityEnum.values()[(int) id];
    }
}

