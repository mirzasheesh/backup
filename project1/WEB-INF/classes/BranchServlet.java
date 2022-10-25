import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

@WebServlet("/branches")
public class BranchServlet extends HttpServlet
{
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        PrintWriter pw = resp.getWriter();   
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");

        try
        {
            String[] path = ((String) req.getPathInfo()).split("/");

            if(path.length == 3 && path[1] != null && path[2].equals("employees"))
            {
                pw.write(Branch.employees(Integer.parseInt(path[1])));
                pw.close();
                return;
            }

            if(path.length == 2 && path[1] != null)
            {
                Integer branchID = Integer.parseInt(path[1]);
                if(Branch.branchName(branchID) == null)
                {
                    //resp.setStatus(404);
                    pw.write("Invalid branchID, branch doesn't exists");
                    pw.close();
                    return;
                }
                //resp.setStatus(200);
                pw.write("branchID: " + path[1] + ", branchName: " + Branch.branchName(branchID) + ", branchLocation: " + Branch.branchLocation(branchID) + ", employeesCount: " + Branch.employeesCounts(branchID));
                pw.close();
                return;
            }
        }
        catch(NumberFormatException err)
        {
            //resp.setStatus(400);
            pw.write("Invalid branchID, it must be a number");
            pw.close();
            return;
        }
        catch(Exception e)
        {
            //System.out.println(e);
        }
        //resp.setStatus(200);
        pw.write(Branch.branches());
        pw.close();
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        PrintWriter pw = resp.getWriter();
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        
        if(req.getParameter("branchName") != null && req.getParameter("branchLocation") != null)
        {
            Integer branchID = Branch.add((String) req.getParameter("branchName"), (String) req.getParameter("branchLocation"));
            //resp.setStatus(201);
            pw.write("New branch with branchID: " + branchID + " added successfully");
            pw.close();
            return;
        }
        //resp.setStatus(400);
        pw.write("Invalid parameters");
        pw.close();
    }
}

class Branch
{
    private static HashMap<Integer, Branch> branches = new HashMap<Integer, Branch>();
    private static Integer currentID = 0;
    private HashSet<Integer> employees = new HashSet<Integer>();
    private Integer branchID;
    private String branchName;
    private String branchLocation;

    Branch(Integer branchID, String branchName, String branchLocation)
    {
        this.branchID = branchID;
        this.branchName = branchName;
        this.branchLocation = branchLocation;
    }

    static Integer add(String branchName, String branchLocation)
    {
        Branch newBranch = new Branch(++currentID, branchName, branchLocation);
        branches.put(newBranch.branchID, newBranch);
        return newBranch.branchID;
    }

    static String branchName(Integer branchID)
    {
        if(branches.containsKey(branchID))
        {
            return branches.get(branchID).branchName;
        }
        return null;
    }

    static String branchLocation(Integer branchID)
    {
        if(branches.containsKey(branchID))
        {
            return branches.get(branchID).branchLocation;
        }
        return null;
    }

    static void addEmployee(Integer branchID, Integer employeeID)
    {
        if(branches.containsKey(branchID))
        {
            branches.get(branchID).employees.add(employeeID);
        }
    }

    static Integer employeesCounts(Integer branchID)
    {
        if(branches.containsKey(branchID))
        {
            return branches.get(branchID).employees.size();
        }
        return null;
    }

    static String branches()
    {
        if(branches.isEmpty())
        {
            return "No data exists";
        }

        String result = "";

        Iterator<Integer> iterate = branches.keySet().iterator();
        
        while(iterate.hasNext())
        {
            Integer branchID = (Integer) iterate.next();
            result = result + "branchID: " + branchID + ", branchName: " + Branch.branchName(branchID) + ", branchLocation: " + Branch.branchLocation(branchID) + ", employeeCounts: " + Branch.employeesCounts(branchID) + "\n";
        }
        return result;
    }

    static String employees(Integer branchID)
    {
        if(branches.containsKey(branchID))
        {
            if(branches.get(branchID).employees.isEmpty())
            {
                return "No employees exists at this branch";
            }

            Iterator<Integer> iterate = branches.get(branchID).employees.iterator();
            String result = "";
            while(iterate.hasNext())
            {
                Integer employeeID = (Integer) iterate.next();
                result = result + "employeeID: " + employeeID + ", employeeName: " + Employee.Name(employeeID) + "\n";
            }
            return result;
        }
        return "No data exists";
    }
}