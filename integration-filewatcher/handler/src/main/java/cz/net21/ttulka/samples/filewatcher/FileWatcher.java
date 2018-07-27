package cz.net21.ttulka.samples.filewatcher;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

@Slf4j
public class FileWatcher implements Runnable {
    private final AtomicBoolean isRunning;
    private final WatchService watcher;
    private final Map<WatchKey, Path> keys;
    private final FileWatcherHandler fileWatcherHandler;
    private final DirWatcherHandler dirWatcherHandler;


    public FileWatcher(Path rootPath, FileWatcherHandler fileWatcherHandler) throws IOException {
        this.isRunning = new AtomicBoolean(false);
        this.watcher = FileSystems.getDefault().newWatchService();
        this.fileWatcherHandler = fileWatcherHandler;
        this.dirWatcherHandler = new DefaultDirWatcherHandler();
        this.keys = new HashMap<>();
        if (rootPath.toFile().isDirectory()) {
            register(rootPath);
        } else {
            log.warn("Given path '%s' is either does not exist or is not a directory!", rootPath);
        }
    }

    @Override
    public void run() {
        try {
            startWatching();
        } catch (InterruptedException e) {
            log.debug(String.format("'%s' was interrupted!", getClass().getSimpleName()), e);
        }
    }

    private void startWatching() throws InterruptedException {
        WatchKey key;
        for (;;) {
            // wait for key to be signalled
            key = watcher.take();
            Path dir = keys.get(key);
            if (dir != null) {
                key.pollEvents().stream()
                        .filter(event -> event.kind() != OVERFLOW)
                        .map(event -> (WatchEvent<Path>) event)
                        .forEach(event -> dispatchEvent(dir, event));

                // reset key and remove from set if directory no longer accessible
                if (allKeysProcessed(key)) {
                    break;
                }
            } else {
                log.debug("WatchKey not recognized!!");
            }

        }
    }

    /**
     * Register the given directory, and all its sub-directories, with the WatchService.
     */
    private void register(final Path rootPath) {
        // register directory and sub-directories
        try {
            Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                        throws IOException {
                    WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
                    Path registeredDir = keys.get(key);
                    if (registeredDir == null) {
                        log.debug(String.format("registered directory: %s", dir));
                    } else {
                        if (!dir.equals(registeredDir)) {
                            log.debug(String.format("updated directory: %s -> %s%n", registeredDir, dir));
                        }
                    }
                    keys.put(key, dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            log.warn(String.format("Could not register directory '%s' or one of its sub directories for file Watcher!", rootPath), e);
        }
    }

    private boolean dirIsWatched(Path dir) {
        return keys.keySet().stream()
                .anyMatch(key -> keys.get(key).startsWith(dir));
    }

    private void deregister(Path dir){
        final List<WatchKey> pathsToRemove = keys.keySet().stream()
                .filter(key -> keys.get(key).startsWith(dir))
                .collect(Collectors.toList());
        pathsToRemove.forEach(key -> {
            log.debug(String.format("Removing registered directory '%s'!", keys.get(key).toString()));
            key.cancel();
        });
    }

    private void dispatchEvent(Path dir, WatchEvent<Path> event){
        log.info(String.format("dispatchEvent(dir=%s, event=%s)", dir, event));
        log.info(String.format("event.context='%s'", event.context()));
        Path path = Paths.get(dir.resolve(event.context()).toString());
//        log.info(String.format("path='%s'", path));

        if(path.toFile().isDirectory() || dirIsWatched(path)) {
//            log.info("isDirectory==true");
            dispatchDirEvent(path, event.kind());
        } else {
//            log.info("isDirectory==false");
            dispatchFileEvent(path, event.kind());
        }
    }

    private boolean allKeysProcessed(WatchKey key){
        boolean valid = key.reset();
        if (!valid) {
            keys.remove(key);
            return keys.isEmpty();
        } else {
            return false;
        }
    }

    private void dispatchDirEvent(Path path, WatchEvent.Kind kind){
        if (kind == ENTRY_CREATE ){
            dirWatcherHandler.onDirCreated(path);
        } else if( kind == ENTRY_DELETE ){
            dirWatcherHandler.onDirDeleted(path);
        } else if( kind == ENTRY_MODIFY ){
            dirWatcherHandler.onDirModified(path);
        } else {
            log.warn(String.format("Received unknown file watcher event of kind '%s' of for dir path '%s'. Ignoring event", kind, path));
        }
    }

    private void dispatchFileEvent(Path path, WatchEvent.Kind kind){
        if (kind == ENTRY_CREATE ){
            fileWatcherHandler.onFileCreated(path);
        } else if( kind == ENTRY_DELETE ){
            fileWatcherHandler.onFileDeleted(path);
        } else if( kind == ENTRY_MODIFY ){
            fileWatcherHandler.onFileModified(path);
        } else {
            log.warn(String.format("Received unknown file watcher event of kind '%s' of for file path '%s'. Ignoring event", kind, path));
        }
    }

    private static void usage() {
        log.error("Usage: java FileWatcher dir");
        System.exit(-1);
    }

    public static void main(String[] args) throws IOException {
        // parse arguments
        if (args.length == 0 || args.length > 1)
            usage();

        // register directory and process its events
        Path dir = Paths.get(args[0]);
        FileWatcher fileWatcher = new FileWatcher(dir, new DefaultFileWatcherHandler());
        fileWatcher.run();
    }

    private static class DefaultFileWatcherHandler implements FileWatcherHandler {
        @Override
        public void onFileCreated(Path file) {
            log.debug(String.format("'%s.onFileCreated(%s)' called", FileWatcherHandler.class.getSimpleName(), file.toString()));
            log.info(String.format("---> created file '%s'", file.toString()));
        }

        @Override
        public void onFileDeleted(Path file) {
            log.debug(String.format("'%s.onFileDeleted(%s)' called", FileWatcherHandler.class.getSimpleName(), file.toString()));
            log.info(String.format("---> deleted file '%s'", file.toString()));
        }

        @Override
        public void onFileModified(Path file) {
            log.debug(String.format("'%s.onFileModified(%s)' called", FileWatcherHandler.class.getSimpleName(), file.toString()));
            log.info(String.format("---> modified file '%s'", file.toString()));
        }
    }

    private class DefaultDirWatcherHandler implements  DirWatcherHandler {
        @Override
        public void onDirCreated(Path dir) {
            log.debug(String.format("'%s.onDirCreated(%s)' called", getClass().getSimpleName(), dir.toString()));
            register(dir);
        }

        @Override
        public void onDirDeleted(Path dir) {
            log.debug(String.format("'%s.onDirDeleted(%s)' called", getClass().getSimpleName(), dir.toString()));
            deregister(dir);
        }

        @Override
        public void onDirModified(Path dir) {
            log.debug(String.format("'%s.onDirModified(%s)' called", getClass().getSimpleName(), dir.toString()));
        }
    };
}
