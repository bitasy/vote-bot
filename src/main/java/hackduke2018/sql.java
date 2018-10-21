package hackduke2018;

import com.google.appengine.api.utils.SystemProperty;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SQL Query", value = "/sql")
public class sql extends HttpServlet {

    private String back = "<a href=\"sql\">Run another Query</a>";

    private Connection conn;
    private String databaseName = "vote_assist";
    private String instanceConnectionName = "vote-assist-chat:us-east1:vote-assist-data";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        final String selectStates = request.getParameter("query");

        PrintWriter out = response.getWriter();
        response.setContentType("text/plain");

        try (ResultSet rs = conn.prepareStatement(selectStates).executeQuery()) {
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            for (int i = 1; i <= columnsNumber; i++){
                out.print(rsmd.getColumnName(i) + "\t\t" );
            }
            out.print("\n");
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = rs.getString(i);
                    out.print(columnValue + "\t\t");
                }
                out.println("");
            }
        } catch (SQLException e) {
            throw new ServletException("SQL error", e);
        }

    }

    @Override
    public void init() throws ServletException {

        String jdbcUrl = String.format(
                "jdbc:mysql://google/%s?cloudSqlInstance=%s"
                        + "&socketFactory=com.google.cloud.sql.mysql.SocketFactory&useSSL=false",
                databaseName,
                instanceConnectionName);


        log("connecting to: " + jdbcUrl);
        try {
            conn = DriverManager.getConnection(jdbcUrl, "root", null);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Unable to connect to Cloud SQL", e);
        }
    }

}
