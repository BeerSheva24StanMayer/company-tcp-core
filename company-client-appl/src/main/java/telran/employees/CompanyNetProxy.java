package telran.employees;

import java.util.*;
import org.json.JSONArray;
import telran.net.*;

public class CompanyNetProxy implements Company {
    NetworkClient client;

    public CompanyNetProxy(NetworkClient client) {
        this.client = client;
    }

    @Override
    public Iterator<Employee> iterator() {
        return null;
    }

    @Override
    public void addEmployee(Employee employee) {
        client.sendAndReceive("addEmployee", employee.toString());
    }

    @Override
    public int getDepartmentBudget(String department) {
        return Integer.valueOf(client.sendAndReceive("getDepartmentBudget", department));
    }

    @Override
    public String[] getDepartments() {
        String departments = client.sendAndReceive("getDepartments", "");
        JSONArray jsonArray = new JSONArray(departments);
        return jsonArray.toList().toArray(String[]::new);
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
        return Employee.getEmployeeFromJSON(remove);
    }

}
