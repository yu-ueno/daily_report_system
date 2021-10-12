package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import services.EmployeeService;

public class EmployeeAction extends ActionBase {

    private EmployeeService service;

    @Override
    public void process() throws ServletException, IOException {

        service = new EmployeeService();

        invoke();

        service.close();
    }

    //一覧画面設定
    public void index() throws ServletException, IOException{

        int page = getPage();
        List<EmployeeView> employees = service.getPerPage(page);

        //全従業員データ件数取得
        long employeeCount = service.countAll();

        //取得した従業員データ、ページ数設定
        putRequestScope(AttributeConst.EMPLOYEES, employees);
        putRequestScope(AttributeConst.EMP_COUNT, employeeCount);
        putRequestScope(AttributeConst.PAGE, page);
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE);

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え
        //セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //一覧画面を表示
        forward(ForwardConst.FW_EMP_INDEX);

    }


}
