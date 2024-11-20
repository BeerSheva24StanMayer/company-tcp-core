package telran.employees;

import telran.io.Persistable;

public class DataSaver extends Thread {
    private int saveInterval;
    private String sourceFile;
    private Persistable persistable;

    public DataSaver(Persistable persistable, String sourceFile, int saveInterval) {
        this.persistable = persistable;
        this.saveInterval = saveInterval;
        this.sourceFile = sourceFile;
        setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            try {
                persistable.saveToFile(sourceFile);
                sleep(saveInterval);
            } catch (InterruptedException e) {
            }
        }
    }
}
