package actions;

import java.io.IOException;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.MessageConst;
import constants.PropertyConst;
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

    /*
     * ログイン処理
     */
    public void login() throws ServletException,IOException{

        String code = getRequestParam(AttributeConst.EMP_CODE);
        String plainPass = getRequestParam(AttributeConst.EMP_PASS);
        String pepper = getContextScope(PropertyConst.PEPPER);

      //従業員の認証チェック
        Boolean isValidEmployee = service.validateLogin(code, plainPass, pepper);

        //認証チェック
        if (isValidEmployee) {

            //CSRF対策 tokenのチェック
            if (checkToken()) {

                //ログイン従業員のデータ取得
                EmployeeView ev = service.findOne(code, plainPass, pepper);
                //セッションにログインした従業員を設定
                putSessionScope(AttributeConst.LOGIN_EMP, ev);
                //セッションにログイン完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_LOGINED.getMessage());

                redirect(ForwardConst.ACT_TOP, ForwardConst.CMD_INDEX);
            }
        } else {

            //CSRF対策用トークンを設定
            putRequestScope(AttributeConst.TOKEN, getTokenId());
            //認証失敗エラーメッセージ表示フラグをたてる
            putRequestScope(AttributeConst.LOGIN_ERR, true);
            //入力された従業員コードを設定
            putRequestScope(AttributeConst.EMP_CODE, code);

            //ログイン画面表示
            forward(ForwardConst.FW_LOGIN);
        }
    }

    /**
     * ログアウト処理
     */
    public void logout() throws ServletException, IOException {

        //セッションからログイン従業員のパラメータを削除
        removeSessionScope(AttributeConst.LOGIN_EMP);

        //セッションにログアウト時のフラッシュメッセージを追加
        putSessionScope(AttributeConst.FLUSH, MessageConst.I_LOGOUT.getMessage());

        redirect(ForwardConst.ACT_AUTH, ForwardConst.CMD_SHOW_LOGIN);

    }
}
