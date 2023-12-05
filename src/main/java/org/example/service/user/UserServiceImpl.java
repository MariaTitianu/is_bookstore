package org.example.service.user;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.example.model.User;
import org.example.model.validator.Notification;
import org.example.repository.user.UserRepository;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public List<User> findAllEmployee() {
        return userRepository.findAllEmployee();
    }

    @Override
    public Notification<Boolean> createOrUpdateEmployee(User user) {
        return userRepository.createOrUpdateEmployee(user);
    }

    @Override
    public Notification<Boolean> delete(String username) {
        return userRepository.delete(username);
    }

    @Override
    public Notification<String> generateEmployeesReport() {
        String employeeReportName = "employeeReport.pdf";
        Notification<String> employeeReportNotification = new Notification<>();
        Notification<List<String>> reportNotification = userRepository.getEmployeeReport();
        if(!reportNotification.hasErrors()){
            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(employeeReportName));

                document.open();
                Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, BaseColor.BLACK);

                for(String s : reportNotification.getResult()) {
                    Paragraph paragraph = new Paragraph(s, font);
                    document.add(paragraph);
                }

                document.close();

                employeeReportNotification.setResult(employeeReportName);
            } catch (DocumentException | FileNotFoundException e) {
                employeeReportNotification.addError(e.toString());
            }
        }
        else{
            employeeReportNotification.addError(reportNotification.getFormattedErrors());
        }
        return employeeReportNotification;
    }
}
