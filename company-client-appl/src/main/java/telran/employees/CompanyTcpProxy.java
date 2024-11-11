package telran.employees;

import java.util.Arrays;
import java.util.Iterator;

import org.json.JSONArray;

import telran.net.*;

public class CompanyTcpProxy implements Company{
    TcpClient client;

    public CompanyTcpProxy(TcpClient client) {
        this.client = client;
    }

    @Override
    public Iterator<Employee> iterator() {
        return null;
    }

    @Override
    public void addEmployee(Employee employee) {
        client.sendAndReceive("addEmployee", employee.toString());
        client.sendAndReceive("save", "");
    }

    @Override
    public int getDepartmentBudget(String department) {
        return Integer.valueOf(client.sendAndReceive("getDepartmentBudget", department));
    }

    @Override
    public String[] getDepartments() {
    
        return client.sendAndReceive("getDepartments", "").split(",");
    }

    @Override
    public Employee getEmployee(long id) {
        String employee = client.sendAndReceive("getEmployee", id + "");
        return Employee.getEmployeeFromJSON(employee);
    }

    @Override
    public Manager[] getManagersWithMostFactor() {
        String manString = client.sendAndReceive("getManagersWithMostFactor", "");
        JSONArray manJSON = new JSONArray(manString);
        String[] manArray = manJSON.toList().toArray(String[]::new);
        return Arrays.stream(manArray).map(Manager::getEmployeeFromJSON).toArray(Manager[]::new);
    }

    @Override
    public Employee removeEmployee(long id) {
        String remove = client.sendAndReceive("removeEmployee", id + "");
        client.sendAndReceive("save", "");
        return Employee.getEmployeeFromJSON(remove);
    }

}
