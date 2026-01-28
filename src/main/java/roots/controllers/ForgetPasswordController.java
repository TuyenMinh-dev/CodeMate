package roots.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import roots.services.EmailService;
import roots.utils.ChangeFXML;
import roots.utils.EmailUtils;

public class ForgetPasswordController {
    @FXML private TextField emailFG;
    @FXML private TextField OTPFG;
    @FXML private TextField newPassword;
    @FXML private TextField checkNewPassword;

    @FXML private Label errorEmail;
    @FXML private Label errorOTP;
    @FXML private Label errorPassword;
    @FXML private Label errorCheckPassword;

    @FXML
    public void sendOTPFG(ActionEvent event){
        resetErrorEmpty();
        String email = emailFG.getText();
        if (emailFG == null){
            errorEmail.setText("Email không được để trống!");
            return;
        }
        else{
            EmailService emailService = new EmailService();
            emailService.emailSend(email);
        }

    }

    @FXML
    public void checkOTPFG(ActionEvent event){
        resetErrorEmpty();
        String otp = OTPFG.getText();
        if(OTPFG == null){
            errorOTP.setText("Mã OTP không được để trống!");
            return;
        }
        else {
            EmailService emailService = new EmailService();
            if(emailService.checkOTP(otp)){
                ChangeFXML.changeFXML(event, "/view/newPassword.fxml");
            }
            else {
                errorOTP.setText("Mã OTP không hợp lệ!");
                return;
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
    private void resetErrorEmpty(){
        errorEmail.setText("");
        errorOTP.setText("");
        errorPassword.setText("");
        errorCheckPassword.setText("");
    }
}
