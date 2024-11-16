package telran.employees;

import telran.view.*;

import java.io.*;
import java.util.*;
import telran.net.*;

public class Main {
    private static final String HOST = "localhost";
    private static final int PORT = 4000;

    public static void main(String[] args) {
        InputOutput io = new StandardInputOutput();
        NetworkClient client = new TcpClient(HOST, PORT);
        Company company = new CompanyNetProxy(client);
        Item[] items = CompanyItems.getItems(company);
        items = addExitItem(items, client);
        Menu menu = new Menu("Company Network Application", items);
        menu.perform(io);
        io.writeLine("Application is finished");
    }

    private static Item[] addExitItem(Item[] items, NetworkClient client) {
       Item[] res = Arrays.copyOf(items, items.length + 1);
       res[items.length] = Item.of("Exit", io -> {
        try {
            if(client instanceof Closeable closeable) {
                closeable.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }, true);
    return res;
    }
}