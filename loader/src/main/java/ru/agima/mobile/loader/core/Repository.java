package ru.agima.mobile.loader.core;

public enum Repository {
    LOCAL_CACHE(""),
    COMMON_CASH(""),
    DIRECTORY_ALARMS(""),
    DIRECTORY_DCIM(""),
    DIRECTORY_DOCUMENTS(""),
    DIRECTORY_DOWNLOADS(""),
    DIRECTORY_MOVIES(""),
    DIRECTORY_MUSIC(""),
    DIRECTORY_NOTIFICATIONS(""),
    DIRECTORY_PICTURES(""),
    DIRECTORY_PODCASTS(""),
    DIRECTORY_RINGTONES(""),
    DEFAULT(LOCAL_CACHE.path);

    private String path;

    public String getPath() {
        return path;
    }

    Repository(String path) {
            this.path = path;
        }
}
