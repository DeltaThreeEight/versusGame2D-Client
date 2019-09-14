package Resources;

import GUI.Main;

import java.util.ResourceBundle;

public class TextResources {
    private ResourceBundle rb;
    public final String PLAYER_JOIN;
    public final String PLAYER_LEFT;
    public final String AUTH_SUCCESS;
    public final String USER_ALREADY_AUTH;
    public final String AUTHORIZED;
    public final String WRONG_LOG_PASS;
    public final String REG_SUCCESS;
    public final String NOT_UNIQUE;
    public final String NAME;
    public final String HEALTH;
    public final String CREATION_DATE;
    public final String NO_PERSON;
    public final String PERSON_ALREADY_SELECTED;
    public final String PERSON_SELECTED;
    public final String SEL_PERSON;
    public final String SAME_NAME;
    public final String PERSON_REMOVED;
    public final String LEFT_SERVER;
    public final String SERVER_MESSAGE;
    public final String EXPIRED_TOKEN;
    public final String EXPIRED_REGISTRATION_TOKEN;
    public final String EMAIL_CONF;
    public final String WRONG_TOKEN;
    public final String UNCONF_TOKEN;
    public final String DEFAULT_PERSON;
    public final String PERSON_KILLED;

    public final String INFORMATION;

    // Token window
    public final String INPUT_TOKEN;
    public final String ENTER;

    // Registration and login windows
    public final String LOGIN_EMPTY;
    public final String PASS_SHORT;
    public final String EMAIL_EMPTY;
    public final String REG_BTN;
    public final String LOG_BTN;
    public final String REG_LOGIN;
    public final String REG_EMAIL;
    public final String REG_PASS;
    public final String REG_PASS2;
    public final String CNL_BTN;
    public final String LBL_LOGIN;
    public final String LBL_EMAIL;
    public final String LBL_PASS;
    public final String LBL_PASS2;
    public final String PASS_NOT_EQUALS;

    // Main window
    public final String BTN_SEL;
    public final String BTN_CRT;
    public final String BTN_STAT;
    public final String BTN_DEL;
    public final String BTN_ALL;
    public final String BTN_SEND;

    // Person creation window
    public final String ENTER_PRSN_NAME;
    public final String PRSN_NAME;
    public final String SPY;
    public final String MERC;

    public TextResources(ResourceBundle bundle) {
        if (bundle == null)
            throw new NullPointerException();
        this.rb = bundle;

        PLAYER_JOIN = rb.getString("plr_join");
        PLAYER_LEFT = rb.getString("plr_left");
        AUTH_SUCCESS = rb.getString("auth_success");
        USER_ALREADY_AUTH = rb.getString("user_already_auth");
        AUTHORIZED = rb.getString("authorized");
        WRONG_LOG_PASS = rb.getString("wrong_log_pass");
        REG_SUCCESS = rb.getString("reg_success");
        NOT_UNIQUE = rb.getString("not_unique");
        NAME = rb.getString("name");
        HEALTH = rb.getString("health");
        UNCONF_TOKEN = rb.getString("unconf_token");
        SEL_PERSON = rb.getString("sel_person");
        NO_PERSON = rb.getString("no_person");
        PERSON_SELECTED = rb.getString("person_selected");
        PERSON_ALREADY_SELECTED = rb.getString("person_already_selected");
        CREATION_DATE = rb.getString("creation_date");
        SAME_NAME = rb.getString("same_name");
        PERSON_REMOVED = rb.getString("person_removed");
        SERVER_MESSAGE = rb.getString("server_message");
        LEFT_SERVER = rb.getString("left_server");
        EXPIRED_TOKEN = rb.getString("expired_token");
        EXPIRED_REGISTRATION_TOKEN = rb.getString("expired_registration_token");
        EMAIL_CONF = rb.getString("email_conf");
        WRONG_TOKEN = rb.getString("wrong_token");
        DEFAULT_PERSON = rb.getString("def_prsn");
        PERSON_KILLED = rb.getString("person_killed");
        INFORMATION = rb.getString("information");
        INPUT_TOKEN = rb.getString("input_token");
        ENTER = rb.getString("enter");
        LOGIN_EMPTY = rb.getString("login_empty");
        PASS_SHORT = rb.getString("pass_short");
        EMAIL_EMPTY = rb.getString("email_empty");
        REG_BTN = rb.getString("reg_btn");
        LOG_BTN = rb.getString("log_btn");
        REG_LOGIN = rb.getString("reg_login");
        REG_EMAIL = rb.getString("reg_email");
        REG_PASS = rb.getString("reg_pass");
        REG_PASS2 = rb.getString("reg_pass2");
        CNL_BTN = rb.getString("cnl_btn");
        LBL_LOGIN = rb.getString("lbl_login");
        LBL_EMAIL = rb.getString("lbl_email");
        LBL_PASS = rb.getString("lbl_pass");
        LBL_PASS2 = rb.getString("lbl_pass2");
        BTN_SEL = rb.getString("btn_sel");
        BTN_CRT = rb.getString("btn_crt");
        BTN_STAT = rb.getString("btn_stat");
        BTN_DEL = rb.getString("btn_del");
        BTN_ALL = rb.getString("btn_all");
        BTN_SEND = rb.getString("btn_send");
        ENTER_PRSN_NAME = rb.getString("enter_prsn_name");
        PRSN_NAME = rb.getString("prsn_name");
        SPY = rb.getString("spy");
        MERC = rb.getString("merc");
        PASS_NOT_EQUALS = rb.getString("pass_not_equals");
    }


}
