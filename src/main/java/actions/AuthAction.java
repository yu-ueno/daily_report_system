package actions;

import java.io.IOException;

import javax.servlet.ServletException;

import constants.AttributeConst;
import constants.ForwardConst;
import services.EmployeeService;

public class AuthAction extends ActionBase {

    private EmployeeService service;

    @Override
    public void process() throws ServletException, IOException {

        service = new EmployeeService();

        invoke();

        service.close();
    }

    /*
     * ログイン画面表示
     */
    public void showLogin() throws ServletException, IOException {

        //CSRF対策用トークンを設定
        putRequestScope(AttributeConst.TOKEN, getTokenId());

        //セッションにフラッシュメッセージが登録されている場合はリクエストスコープに設定する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(
                    AttributeConst.FLUSH,
                    getSessionScope(AttributeConst.FLUSH));
            removeSessionScope(AttributeConst.FLUSH);
        }

        //ログイン画面表示
        forward(ForwardConst.FW_LOGIN);

    }
}
