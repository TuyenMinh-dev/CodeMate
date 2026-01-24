package roots.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import roots.services.EmailService;
import roots.utils.EmailUtils;

public class ForgetPasswordController {
    @FXML private TextField emailFG;
    @FXML private TextField OTPFG;

    String email = emailFG.getText();
    String otp = OTPFG.getText();

    @FXML
    public void sendOTPFG(){
        EmailService emailService = new EmailService();
        emailService.emailSend(email);
    }

    @FXML
    public void checkOTPFG(){
        EmailService emailService = new EmailService();
        emailService.checkOTP(otp);
    }

}
