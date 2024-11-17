package telran.employees;
import java.io.ObjectInputFilter.Config;

import telran.io.Persistable;
import telran.net.Protocol;
import telran.net.TcpServer;

public class MainServStart {
    private static final int PORT = 4000;
    private static final String FILE_NAME = "employees.data";

    public static void main(String[] args) {
        Company company = new CompanyImpl();
        TcpServer server = new TcpServer(new CompanyProtocol(company), PORT);
        if (company instanceof Persistable persistable) {
            persistable.restoreFromFile(FILE_NAME);
            DataSaver dataSaver = new DataSaver(persistable);
            dataSaver.run();;
        }
        server.run();
    }
}