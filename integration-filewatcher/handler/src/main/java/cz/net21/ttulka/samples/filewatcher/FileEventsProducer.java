package cz.net21.ttulka.samples.filewatcher;

import java.io.IOException;
import java.nio.file.Path;

import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.file.FileHeaders;
import org.springframework.messaging.Message;

public class FileEventsProducer extends MessageProducerSupport implements FileWatcherHandler {

    private final TaskExecutor taskExecutor;
    private final FileWatcher fileWatcher;

    public FileEventsProducer(Path root) throws IOException {
        taskExecutor = new SimpleAsyncTaskExecutor();
        fileWatcher = new FileWatcher(root, this);
    }

    @Override
    protected void doStart() {
        super.doStart();
        taskExecutor.execute(fileWatcher);
    }

    @Override
    protected void doStop() {
        super.doStop();
        //fileWatcher.stop();
    }

    @Override
    public void onFileCreated(Path file) {
        sendEvent(file, "FILE_NEW");
    }

    @Override
    public void onFileModified(Path file) {
        sendEvent(file, "FILE_MODIFIED");
    }

    @Override
    public void onFileDeleted(Path file) {
        sendEvent(file, "FILE_DELETED");
    }

    private void sendEvent(Path file, String mode) {
        if (isRunning()) {
            try {
                super.sendMessage(prepareMessage(file, mode));

            } catch (Exception e) {
                handleMessageProcessingError(file, e);
            }
        }
    }

    private void handleMessageProcessingError(Path file, Exception e) {
        try {
            sendErrorMessage("Error by sending a message for the file " + file, e);
        } catch (Exception ignore) {
        }
    }

    private void sendErrorMessage(String message, Exception exception) {
        Message<?> errorMessage = this.getMessageBuilderFactory()
                .withPayload(message)
                .build();
        super.sendErrorMessageIfNecessary(errorMessage, (exception instanceof RuntimeException ? (RuntimeException) exception : new RuntimeException(exception)));
    }

    private Message<Path> prepareMessage(Path file, String mode) {
        return this.getMessageBuilderFactory().withPayload(file)
                .setHeader(FileHeaders.RELATIVE_PATH, file)
                .setHeader(FileHeaders.FILENAME, file.toFile().getName())
                .setHeader(FileHeaders.ORIGINAL_FILE, file)
                .setHeader("FILE_MODE", mode)
                .build();
    }
}
