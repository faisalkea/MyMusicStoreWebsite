package com.musicstorewebsite.controller.admin;

import com.musicstorewebsite.model.Customer;
import com.musicstorewebsite.model.Product;
import com.musicstorewebsite.service.CustomerService;
import com.musicstorewebsite.service.ProductService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

/**
 * Created by faisaljarkass on 1/23/2016.
 */

@Controller
@RequestMapping("/admin")
public class AdminHome {

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    @RequestMapping
    public String adminPage() {
        return "admin";
    }

    @RequestMapping("/productInventory")
    public String productInventory(Model model) {
        List<Product> products = productService.getProductList();
        model.addAttribute("products", products);

        return "productInventory";
    }

    @RequestMapping("/customer")
    public String customerManagement(Model model) {
        List<Customer> customerList = customerService.getAllCustomers();
        model.addAttribute(customerList);

        return "customerManagement";
    }

    /**
     * Download method without a temp file!
     * @param model
     * @param session
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/downloadLogFile2")
    public void downloadCSV(ModelMap model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/csv");
        //("application/unknown");

        response.setHeader("content-disposition", "attachment;filename ="+ new Date() +".csv");
        ServletOutputStream writer = response.getOutputStream();

        //Setup the Columns details
        writer.print("id");
        writer.print(';');
        writer.print("CustomerName");
        writer.print(';');
        writer.print("CustomerEmail");
        writer.print(';');
        writer.print("CustomerPhone");
        writer.print(';');
        writer.print("username");
        writer.print(';');
        writer.print("Password");
        writer.print(';');
        writer.print("Billing Address");
        writer.print(';');
        writer.print("Shipping Address");
        writer.print(';');
        writer.print("Cart Details");
        writer.println(';');

        //Insert data to the respective columns!
        List<Customer> allCustomers = customerService.getAllCustomers();
        for (int i = 0; i < allCustomers.size(); i++){
            writer.print(allCustomers.get(i).getCustomerId());
            writer.print(';');
            writer.print(allCustomers.get(i).getCustomerName());
            writer.print(';');
            writer.print(allCustomers.get(i).getCustomerEmail());
            writer.print(';');
            writer.print(allCustomers.get(i).getCustomerPhone());
            writer.print(';');
            writer.print(allCustomers.get(i).getUsername());
            writer.print(';');
            writer.print(allCustomers.get(i).getPassword());
            writer.print(';');
            writer.print(allCustomers.get(i).getBillingAddress().toString());
            writer.print(';');
            writer.print(allCustomers.get(i).getShippingAddress().toString());
            writer.print(';');
            writer.println(allCustomers.get(i).getCart().toString());
        }

        writer.flush();
        writer.close();
    }

    /**
     * Download method with a temp file!
     * @param session
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/downloadLogFile")
    public void getLogFile(HttpSession session, HttpServletResponse response) throws Exception {
        try {
            //String filePath = "/Users/faisaljarkass/Documents/JAVA_CODE/SpringWorkspace/Udemy/BuildingEcommerceDB/eMusicStoreFinalGit/MusicStoreWebsite-master/src/main/webapp/WEB-INF/resources/filesToDownload";
            String filePath = "/main/webapp/WEB-INF/resources/filesToDownload";
            String fullFilePath = filePath + "/test.csv";

            PrintWriter pw = new PrintWriter(new File(fullFilePath));
            StringBuilder sb = new StringBuilder();
            sb.append("id");
            sb.append(";");
            sb.append("Name");
            sb.append('\n');

            for (int i = 0; i < 10; i++) {
                sb.append(i);
                sb.append(";");
                sb.append("Faisal Jarkass");
                sb.append('\n');
            }

            pw.write(sb.toString());
            pw.close();

            System.out.println(sb.toString());

            File fileToDownload = new File(fullFilePath);
            InputStream inputStream = new FileInputStream(fileToDownload);

            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment; filename=" + new Date() + ".csv");

            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            inputStream.close();
        } catch (Exception e) {
            System.out.println("Request could not be completed at this moment. Please try again.");
            e.printStackTrace();
        }
    }

}
