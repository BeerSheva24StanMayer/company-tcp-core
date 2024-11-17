package telran.employees;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.NoSuchElementException;

import org.json.JSONArray;

import telran.io.Persistable;
import telran.net.Protocol;
import telran.net.Request;
import telran.net.Response;
import telran.net.ResponseCode;

public class CompanyProtocol implements Protocol{
    Company company;
    private static final String FILE_NAME = "employees.data";

    public CompanyProtocol(Company company) {
        this.company = company;
    }

    @Override
    public Response getResponse(Request request) {
        String requestType = request.requestType();
        String requestData = request.requestData();
        Response response = null;
        try {
            Method method = CompanyProtocol.class.getDeclaredMethod(requestType, String.class);
            method.setAccessible(true);
            response = (Response) method.invoke(this, requestData);
        } catch (NoSuchMethodException e) {
            response = new Response(ResponseCode.WRONG_TYPE, requestType + " Wrong type");
           
        } catch (InvocationTargetException e) {
            Throwable causeExc = e.getCause();
            String message = causeExc == null ? e.getMessage() : causeExc.getMessage();
            response = new Response(ResponseCode.WRONG_DATA, message);
        } catch (Exception e){
            //only for finishing Server and printing out Exception full stack
            throw new RuntimeException(e);
        }
        return response;
    }

    private Response addEmployee(String data) {
        Response res = null;
        Employee employee = Employee.getEmployeeFromJSON(data);
        try {
            company.addEmployee(employee);
            res = getOKresponse("");
        } catch (IllegalStateException e) {
            res = getWRONGresponse("This ID is already exists");
        }
        // save();
        return res;
    }

    private Response removeEmployee(String data) {
        Response res = null;
        long id = Integer.valueOf(data);
        try {
            Employee employee = company.removeEmployee(id);
            res = getOKresponse(employee.toString());
        } catch (NoSuchElementException e) {
            res = getWRONGresponse("This ID has not been found");
        }
        // save();
        return res;
    }

    private Response getEmployee(String data) {
        Response res = null;
        long id = Long.valueOf(data);
        Employee employee = company.getEmployee(id);
        if (employee != null) {
            res = getOKresponse(employee.toString());
        } else {
            res = getWRONGresponse("This ID has not been found");
        }
        return res;
    }

    private Response getManagersWithMostFactor() {
        Manager[] managers = company.getManagersWithMostFactor();
        String[] jsonStrings = Arrays.stream(managers).map(Manager::toString).toArray(String[]::new);
        JSONArray jsonArray = new JSONArray(jsonStrings);
        return getOKresponse(jsonArray.toString());
    }

    private Response getDepartments() {
        String[] departments = company.getDepartments();
        JSONArray jsonArray = new JSONArray(departments);
        return getOKresponse(jsonArray.toString());
    }

    private Response getDepartmentBudget(String data) {
        int buget = company.getDepartmentBudget(data);
        return getOKresponse(buget + "");
    }

    // private Response save() {
    //     Response res = null;
    //     if (company instanceof Persistable persistable) {
    //         try {
    //             persistable.saveToFile(FILE_NAME);
    //             res = getOKresponse("");
    //         } catch (Exception e) {
    //             res = getWRONGresponse(e.getMessage());
    //         }
    //     }
    //     return res;
    // }

    private Response getOKresponse(String string) {
        return new Response(ResponseCode.OK, string);
    }

    private Response getWRONGresponse(String string) {
        return new Response(ResponseCode.WRONG_DATA, string);
    }

}
