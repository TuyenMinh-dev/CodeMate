package roots.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import roots.services.EmailService;
import roots.utils.ChangeFXML;
import roots.utils.EmailUtils;

public class ForgetPasswordController {
    @FXML private TextField emailFG;
    @FXML private TextField OTPFG;

    @FXML
    public void sendOTPFG(ActionEvent event){

        if(emailFG != null){
            String email = emailFG.getText();
            EmailService emailService = new EmailService();
            emailService.emailSend(email);
        }

    }

    @FXML
    public void checkOTPFG(ActionEvent event){
        if(OTPFG != null) {
            String otp = OTPFG.getText();
            EmailService emailService = new EmailService();
            if(emailService.checkOTP(otp)){
                ChangeFXML.changeFXML(event, "/view/newPassword.fxml");
            }

        }
    }
    @FXML
    public void comeBackFG1(MouseEvent event){
        ChangeFXML.changeFXML(event, "/view/login.fxml");
    }
    @FXML
    public void comeBackFG2(MouseEvent event){
        ChangeFXML.changeFXML(event, "/view/forgetPassword.fxml");
    }

}
