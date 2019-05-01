/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import static javafx.scene.control.Alert.AlertType.WARNING;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author cpick
 */

public class FXMLDocumentController implements Initializable
{
    
    private Label label;
    @FXML
    private Button loginBtn;
    @FXML
    private Hyperlink signUp;
    @FXML
    private VBox loginVBox;
    @FXML
    private VBox signupVBox;
    
    // URL to the database
    final String DB_URL = "jdbc:derby://localhost:1527/PasswordVault";
    
    @FXML
    private TextField signupUsername;           // Username signup field
    @FXML
    private PasswordField signupPass;           // User password signup field
    @FXML
    private PasswordField signupConfirmPass;    // User confirm password field
    @FXML
    private Button signupBtn;                   // User signup button
    @FXML
    private Label accountCreatedResult;         // Label to show account created
    @FXML
    private Label signupResultLabel;            // Label to show user already taken
    @FXML
    private Button backBtn;                     // Back to Login from Signup

    @FXML
    private HBox buttonBar;                     // Button bar (Add,Remove,Edit)
    @FXML
    private MenuBar menuBar;                    // Top menu bar
    @FXML
    private VBox addInfoVBox;                   // VBox with add login info fields
    @FXML
    private TextField titleInfo;                // Field to enter Title
    @FXML
    private TextField usernameInfo;             // Field to enter username
    @FXML
    private TextField passwordInfo;             // Field to enter password
    @FXML
    private TextField urlInfo;                  // Field to enter URL
    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField userName;                 // Username login field
    @FXML
    private PasswordField userPassword;         // User password login field
    @FXML
    private Label loginFailLabel;               // Label for login failed
    
    private int userId;                         // Store used ID when logged in
    
    @FXML
    private ListView<String> listView;             // Store titles of login data
    
    // Observable for titles of login data
    private ObservableList<String> observableList;
    @FXML
    private Label showTitle;
    @FXML
    private TextField showUsernameField;
    @FXML
    private TextField showPasswordField;
    @FXML
    private TextField showUrlField;
    @FXML
    private VBox siteInfoBox;
    @FXML
    private Button exitBtn;
    @FXML
    private Button updateBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button removeBtn;
    @FXML
    private Button editBtn;
    @FXML
    private Button backBtn2;
    @FXML
    private Button showBtn;
    
    String loginTitle = "";
    boolean passwordVisible = false;

    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        
    }    
    
    /**
     * handLoginButton handles logging the user in
     * @param event 
     */

    @FXML
    private void handleLoginButton(ActionEvent event)
    {
        boolean verified;
        String username = userName.getText();
        String password = userPassword.getText();
        
        try
        {
            UserLogin userLogin = new UserLogin();
            verified = userLogin.checkCredentials(username, password);
            
            if (verified == true)
            {
                loginVBox.setVisible(false);
                menuBar.setVisible(true);
                buttonBar.setVisible(true);
                borderPane.setVisible(true);
                userName.clear();
                userPassword.clear();
                loginFailLabel.setText("");
                userId = userLogin.getUserId(username);
                System.out.println(userId);
                try
                {
                    observableList = 
                           FXCollections.observableList
                                (userLogin.getSavedData(userId));
                    
                    listView.setItems(observableList);
                }
                catch (SQLException ex)
                {
                    System.out.println("ERROR: " + ex.getMessage());
                }     
            }
            else
            {
                loginFailLabel.setText("Incorrect username or password!");
            }
        }
        catch (SQLException ex)
        {
            System.out.println("ERROR: " + ex.getMessage());
        }
    }
    
    /**
     * handleSignUp link disables login VBox and enables signupVBox
     * @param event 
     */

    @FXML
    private void handleSignUp(ActionEvent event)
    {
        loginVBox.setVisible(false);
        signupVBox.setVisible(true);
        backBtn.setVisible(true);
    }
    
    /**
     * handleSignUpButton handles adding the user's username and password
     * to the database
     * @param event 
     */

    @FXML
    private void handleSignUpButton(ActionEvent event)
    {
        
        // Store Username String 
        String username = signupUsername.getText();
        
        // Store Password String
        String password = signupPass.getText();
        
        // Store Confirm Password String
        String confirmPass = signupConfirmPass.getText();
        
        try
        {
            UserSignup userSignUp = new UserSignup();
            boolean userTaken = userSignUp.checkUsername(username);

            if (userTaken == true)
                signupResultLabel.setText("Sorry, that username "
                                      + "is already taken");
            
            else
            {
                if (password.equals(confirmPass))
                {
                    try
                    {
                        userSignUp.insert(username, password);
                        
                        signupVBox.setVisible(false);
                        backBtn.setVisible(false);
                        loginVBox.setVisible(true);
                        accountCreatedResult.setText("Account Created "
                                + "Successfully!");
                    }
                    catch(SQLException ex)
                    {
                        System.out.println("ERROR: " + ex.getMessage());
                    }
                }
                else 
                {
                    signupResultLabel.setText("Passwords do not match!");
                    signupPass.clear();
                    signupConfirmPass.clear();
                }
            }
        }
        catch(SQLException ex)
        {
            System.out.println("ERROR " + ex.getMessage());
        }    
    }  
    
    /**
     * handleBackButton goes back from the signupVBox to the loginVBox
     * @param event 
     */

    @FXML
    private void handleBackButton(ActionEvent event)
    {
        signupVBox.setVisible(false);
        backBtn.setVisible(false);
        loginVBox.setVisible(true);
    }
    
    /**
     * handleAddButton enables addInfoVBox to add site login data
     * @param event 
     */

    @FXML
    private void handleAddButton(ActionEvent event)
    {
        showTitle.setText("Add");
        siteInfoBox.setVisible(false);
        addInfoVBox.setVisible(true);
        backBtn2.setVisible(true);
    }
    
    /**
     * submitLoginInfo add the site login data to the database
     * @param event 
     */

    @FXML
    private void submitLoginInfo(ActionEvent event)
    {
        String title = titleInfo.getText();
        String username = usernameInfo.getText();
        String password = passwordInfo.getText();
        String url = urlInfo.getText();
        int user = userId;
        
        try
        {
            addLoginInfo add = new addLoginInfo();
            add.insert(user, title, username, password, url);
            listView.getItems().add(title);
            addInfoVBox.setVisible(false);
            siteInfoBox.setVisible(false);
            showTitle.setText("");
            backBtn2.setVisible(false);
        }
        catch (SQLException ex)
        {
            System.out.println("ERROR: " + ex.getMessage());
        }   
    }
    
    /**
     * handleRemoveButton removes selected item from the database
     * @param event 
     */

    @FXML
    private void handleRemoveButton(ActionEvent event)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove Site");
        alert.setHeaderText("Are you sure you wish to remove this site?");
        alert.setResizable(false);
        alert.setContentText("Select Ok to remove or Cancel"
                + " to exit.");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) 
        {
            String selection = listView.getSelectionModel().getSelectedItem();
        
            try
            {
                addLoginInfo loginInfo = new addLoginInfo();
        
                loginInfo.remove(selection);
                observableList.remove(selection);
                siteInfoBox.setVisible(false);
                showTitle.setText("");
                listView.setItems(observableList);
                }
            catch (SQLException ex)
            {
                System.out.println("ERROR: " + ex.getMessage());
            }
        }
    }
    
    /**
     * handleMouseClicked handles selecting the listView item and displays
     * that sites login information
     * @param event 
     */

    @FXML
    private void handleMouseClicked(MouseEvent event)
    {
        // Disable the editable text fields
        toggleEditableFieldsOff();  
        
        // Get the mouse clicked selection
        String selection = listView.getSelectionModel().getSelectedItem();
        loginTitle = selection;
        
        // Hold the selected sites data in ArrayList
        ArrayList data = new ArrayList<>();
        
        // Show the InfoBox
        siteInfoBox.setVisible(true);
        addInfoVBox.setVisible(false);
        
        try
        {
            addLoginInfo loginInfo = new addLoginInfo();
            String password = loginInfo.hidePassword(loginTitle);
            // Catch IndexOutOfBoundsException if there is one
            try
            {
                data = loginInfo.show(selection);
                showTitle.setText(data.get(0).toString());
                showUsernameField.setText(data.get(1).toString());

                showPasswordField.setText(password);
                
                showUrlField.setText(data.get(3).toString());
            }
            catch (IndexOutOfBoundsException ex)
            {
                System.out.println("ERROR: " + ex.getMessage());
                siteInfoBox.setVisible(false);
            }
        }
        catch (SQLException ex)
        {
            System.out.println("ERROR: " + ex.getMessage());
        }
    }
    
    /**
     * handleEditButton enables editing the site login data and updating
     * the database
     * @param event 
     */

    @FXML
    private void handleEditButton(ActionEvent event)
    {
        showTitle.setDisable(false);
        showUsernameField.setDisable(false);
        showPasswordField.setDisable(false);
        showUrlField.setDisable(false);
        exitBtn.setVisible(true);
        updateBtn.setVisible(true);
    }
    
    /**
     * toggleEditableFieldsOff disables the text fields
     */
    
    private void toggleEditableFieldsOff()
    {
        showTitle.setDisable(true);
        showUsernameField.setDisable(true);
        showPasswordField.setDisable(true);
        showUrlField.setDisable(true);
        exitBtn.setVisible(false);
        updateBtn.setVisible(false);
    }
    
    /**
     * Disables the editable text fields
     * @param event 
     */

    @FXML
    private void handleExitBtn(ActionEvent event)
    {
        showTitle.setDisable(true);
        showUsernameField.setDisable(true);
        showPasswordField.setDisable(true);
        showUrlField.setDisable(true);
        exitBtn.setVisible(false);
        updateBtn.setVisible(false);
    }
    
    /**
     * handleUpdateBtn updates the edited login site data
     * @param event 
     */

    @FXML
    private void handleUpdateBtn(ActionEvent event)
    {
        String username = showUsernameField.getText();
        String password = showPasswordField.getText();
        String url = showUrlField.getText();
        int user = userId;
        
        try
        {
            addLoginInfo loginInfo = new addLoginInfo();
            loginInfo.update(username, password, url, user);
            showUsernameField.setEditable(false);
            showPasswordField.setEditable(false);
            showUrlField.setEditable(false);
        }
        catch (SQLException ex)
        {
            System.out.println("ERROR: " + ex.getMessage());
        } 
    }

    @FXML
    private void handleLogout(ActionEvent event)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you wish to logout?");
        alert.setResizable(false);
        alert.setContentText("Select Ok to logout or Cancel"
                + " to exit.");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) 
        {
            loginVBox.setVisible(true);
            menuBar.setVisible(false);
            buttonBar.setVisible(false);
            borderPane.setVisible(false);
            userId = 0;
        }
    } 

    @FXML
    private void handleBackButton2(ActionEvent event)
    {
        showTitle.setText("");
        siteInfoBox.setVisible(false);
        addInfoVBox.setVisible(false);
        backBtn2.setVisible(false);
    }

    @FXML
    private void showPassword(ActionEvent event)
    {
        if (passwordVisible == false)
        {
        try
        {
            addLoginInfo loginInfo = new addLoginInfo();
            String password = loginInfo.viewPassword(loginTitle);
            showPasswordField.setText(password);
            showBtn.setStyle("-fx-background-image: url(hide.png)");
            passwordVisible = true;
        }
        
        catch (SQLException ex)
        {
            System.out.println("ERROR: " + ex.getMessage());
        }
        }
        else
        {
            try
            {
                addLoginInfo loginInfo = new addLoginInfo();
                String password = loginInfo.hidePassword(loginTitle);
                showPasswordField.setText(password);
                showBtn.setStyle("-fx-background-image: url(show.png)");
                passwordVisible = false;
                loginInfo.closeConnection();
            }
            catch (SQLException ex)
            {
                System.out.println("ERROR: " + ex.getMessage());
            }
        }
    }

    @FXML
    private void handleCloseButton(ActionEvent event)
    {
        System.exit(0);
    }

    @FXML
    private void handleAboutButton(ActionEvent event)
    {
        Alert about = new Alert(AlertType.INFORMATION);
        about.setTitle("About");
        about.setHeaderText("Password Vault");
        about.setResizable(false);
        about.setContentText("Password Vault allows you to store your"
                + " username and password for sites.");
        about.showAndWait();
    }
}
