package com.ilegra.listener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

import org.apache.log4j.Logger;

import com.ilegra.Main;

public class Listener {

	final static Logger logger = Logger.getLogger(Listener.class);
	
    public static void listenForChanges(File file) throws IOException {

        Path path = file.toPath();
        if (file.isDirectory()) {
            WatchService ws = path.getFileSystem().newWatchService();
            path.register(ws, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
            WatchKey watch = null;
            while (true) {
            	logger.info("Watching directory: " + file.getPath());
                try {
                    watch = ws.take();
                } catch (InterruptedException ex) {
                	logger.error("Interrupted " + ex.getMessage());
                }
                List<WatchEvent<?>> events = watch.pollEvents();
                watch.reset();
                for (WatchEvent<?> event : events) {
                    Kind<Path> kind = (Kind<Path>) event.kind();

                    if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE) || (kind.equals(StandardWatchEventKinds.ENTRY_DELETE))
                            || kind.equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
                        Main main = new Main();
                        main.processFiles();
                    } else {
                    	logger.error("Not a directory. Will exit.");
                    }
                }
            }
        }
    }
}
