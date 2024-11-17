package telran.employees;

import telran.io.Persistable;

public class DataSaver implements Runnable {
    private final int SAVE_INTERVAL = 3600;
    private final String SOURCE_FILE = "employees.data";

    private int saveInterval;
    private String sourceFile;
    private Persistable persistable;

    public DataSaver(Persistable persistable) {
        this.persistable = persistable;
        this.saveInterval = SAVE_INTERVAL;
        this.sourceFile = SOURCE_FILE;
    }

    @Override
    public void run() {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(saveInterval);
                    persistable.saveToFile(sourceFile);
                } catch (InterruptedException e) {
                    
                }

            }
        });
        thread.start();
    }
}
