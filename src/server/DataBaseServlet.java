package server;

import beans.CountryBean;
import dao.CountryDAO;
import tables.Country;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@WebServlet(name = "DataBaseServlet", urlPatterns = {"/countries"})
public class DataBaseServlet extends HttpServlet {

    @Resource(name = "jdbc/example")
    DataSource ds;

    private CountryDAO countryDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Connection connection = ds.getConnection();
            try(Statement statement = connection.createStatement()) {
                statement.execute("set search_path to demo");
            }
            countryDAO = new CountryDAO(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CountryBean countryBean = new CountryBean();
        countryBean.setCountries(countryDAO.findAll());
        request.setAttribute("countryBean", countryBean);
        request.getRequestDispatcher("/countries.jsp").forward(request,response);
    }
}
