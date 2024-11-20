package telran.employees;


import telran.io.Persistable;
import telran.net.TcpServer;

public class MainServStart {
    private static final int PORT = 4000;
    private static final int SAVE_INTERVAL = 3600;
    private static final String SOURCE_FILE = "employees.data";

    public static void main(String[] args) {
        Company company = new CompanyImpl();
        if (company instanceof Persistable persistable) {
            persistable.restoreFromFile(SOURCE_FILE);
            DataSaver dataSaver = new DataSaver(persistable, SOURCE_FILE, SAVE_INTERVAL);
            dataSaver.start();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> persistable.saveToFile(SOURCE_FILE)));
        }
        TcpServer server = new TcpServer(new CompanyProtocol(company), PORT);
        server.run();
    }
}