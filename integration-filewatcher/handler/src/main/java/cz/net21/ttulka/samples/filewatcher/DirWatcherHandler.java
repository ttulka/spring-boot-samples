package cz.net21.ttulka.samples.filewatcher;

import java.nio.file.Path;

public interface DirWatcherHandler {
    void onDirCreated(Path dir);
    void onDirDeleted(Path dir);
    void onDirModified(Path dir);
}
