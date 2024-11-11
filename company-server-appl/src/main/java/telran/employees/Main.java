package telran.employees;
import telran.io.Persistable;
import telran.net.Protocol;
import telran.net.TcpServer;

public class Main {
    private static final int PORT = 4000;
    private static final String FILE_NAME = "employees.data";

    public static void main(String[] args) {
        Company company = new CompanyImpl();
        Protocol protocol = new CompanyProtocol(company);
        TcpServer server = new TcpServer(protocol, PORT);
        if (company instanceof Persistable persistable) {
            try {
                persistable.restoreFromFile(FILE_NAME);
            } catch (Exception e) {
                persistable.saveToFile(FILE_NAME);
            }
        }
        server.run();
    }
}