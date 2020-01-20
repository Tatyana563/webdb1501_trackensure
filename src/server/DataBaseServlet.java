package server;

import beans.CountryBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CountryDAO;
import tables.Country;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
//http://localhost:9999/
@WebServlet(name = "DataBaseServlet", urlPatterns = {"/countries"})
public class DataBaseServlet extends HttpServlet {
    public static final String URL = "jdbc:postgresql://localhost:5432/example?" +
            "user=postgres&password=apple25";

 /*   @Resource(name = "jdbc/example")
    DataSource ds;*/

    private CountryDAO countryDAO;
    private Connection connection;

    private Connection getConnection() {
        if (Objects.isNull(connection)) {
            try {
                connection = DriverManager.getConnection(URL);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("org.postgresql.Driver");
            countryDAO = new CountryDAO(getConnection());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*@Override
    public void init() throws ServletException {
        super.init();
        try {
            Connection connection = getConnection();
            try (Statement statement = connection.createStatement()) {
//                statement.execute("set search_path to demo");
            }
            countryDAO = new CountryDAO(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/
    //http://localhost:9999/countries?country_name=Italy
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String countryName = request.getParameter("country_name");
        countryDAO.add(countryName);
        response.sendRedirect("countries.jsp");
    }
//http://localhost:9999/countries
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CountryBean countryBean = new CountryBean();
        countryBean.setCountries(countryDAO.findAll());
        request.setAttribute("countryBean", countryBean);
        request.getRequestDispatcher("/countries.jsp").forward(request, response);
    }

    //http://localhost:9999/countries?country=Italy&id=1
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Country country = null;

        ObjectMapper mapper = new ObjectMapper();
        try {
            BufferedReader reader = req.getReader();
            country = mapper.readValue(reader, Country.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
        String countryName = (req.getParameter("country"));
        int countryId = Integer.parseInt(req.getParameter("id"));
        countryDAO.update(countryName, countryId);
    }
//http://localhost:9999/countries?countryId=3
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("countryId"));
        countryDAO.delete(id);
    }
}
