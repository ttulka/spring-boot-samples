package cz.net21.ttulka.samples.filewatcher;

import java.nio.file.Path;

public interface FileWatcherHandler {
    void onFileCreated(Path file);
    void onFileDeleted(Path file);
    void onFileModified(Path file);
}
