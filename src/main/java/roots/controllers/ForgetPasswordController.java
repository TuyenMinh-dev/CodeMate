package roots.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import roots.services.EmailService;
import roots.services.impl.AuthServiceImpl;
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
        errorEmail.setText("");
        String email = emailFG.getText();
        if (email == null){
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
        errorOTP.setText("");
        String otp = OTPFG.getText();
        if(otp == null){
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
    public void confirmPassword(ActionEvent event){
        errorPassword.setText("");
        errorCheckPassword.setText("");
        String newPass = newPassword.getText();
        String checkNewPass = checkNewPassword.getText();

        boolean check = false;
        if(newPass.isEmpty()){
            errorPassword.setText("Mật khẩu mới không được để trống!");
            check = true;
        }
        if(checkNewPass.isEmpty()){
            errorCheckPassword.setText("Mật khẩu mới không được để trống!");
            check = true;
        }
        else {
            if(!checkNewPass.equals(newPass)){
                errorCheckPassword.setText("Mật khẩu không trùng khớp!");
                check = true;
            }
        }
        if(check){
            return;
        }
        else{
            String email = emailFG.getText();
            AuthServiceImpl authService = new AuthServiceImpl();
            authService.forgetPassword(email, newPass);
            ChangeFXML.changeFXML(event, "/view/login.fxml");
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
