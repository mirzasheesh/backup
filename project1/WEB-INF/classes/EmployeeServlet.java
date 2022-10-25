import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

@WebServlet("/employees")
public class EmployeeServlet extends HttpServlet
{
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        PrintWriter pw = resp.getWriter();
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        //resp.setStatus(200);
        pw.write(Employee.employees());
        pw.close();
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        PrintWriter pw = resp.getWriter();
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        
        if(req.getParameter("branchID") != null && req.getParameter("employeeName") != null)
        {
            Integer branchID = Integer.parseInt(req.getParameter("branchID"));

            if(Branch.branchLocation(branchID) == null)
            {
                //resp.setStatus(400);
                pw.write("Invalid branchID");
                pw.close();
                return;
            }
            Integer employeeID = Employee.add(branchID, (String) req.getParameter("employeeName"));
            Branch.addEmployee(branchID, employeeID);
            //resp.setStatus(201);
            pw.write("New employee with employeeID: " + employeeID + " added successfully");
            pw.close();
            return;
        }
        //resp.setStatus(400);
        pw.write("Invalid parameters");
        pw.close();
    }
}

class Employee
{
    private static HashMap<Integer, Employee> employees = new HashMap<Integer, Employee>();
    private static Integer currentID = 0;
    private Integer employeeID;
    private Integer branchID;
    private String employeeName;

    Employee(Integer employeeID, Integer branchID, String employeeName)
    {
        this.employeeID = employeeID;
        this.branchID = branchID;
        this.employeeName = employeeName;
    }

    static Integer add(Integer branchID, String employeeName)
    {
        Employee newEmployee = new Employee(++currentID, branchID, employeeName);
        employees.put(newEmployee.employeeID, newEmployee);
        return newEmployee.employeeID;
    }

    static String Name(Integer employeeID)
    {
        if(employees.containsKey(employeeID))
        {
            return employees.get(employeeID).employeeName;
        }
        return null;
    }

    static Integer BranchID(Integer employeeID)
    {
        if(employees.containsKey(employeeID))
        {
            return employees.get(employeeID).branchID;
        }
        return null;
    }

    static String employees()
    {
        if(employees.isEmpty())
        {
            return "No data exists";
        }

        String result = "";
        Iterator<Integer> iterate = employees.keySet().iterator();
        while(iterate.hasNext())
        {
            Integer employeeID = (Integer) iterate.next();
            result = result + "employeeID: " + employeeID + ", employeeName: " + Employee.Name(employeeID) + ", branchLocation: " + Branch.branchLocation(employees.get(employeeID).branchID) + "\n";
        }
        return result;
    }
}