package models.validators;

import java.util.ArrayList;
import java.util.List;

import actions.views.ReportView;
import constants.MessageConst;

public class ReportValidator {

    /**
     * バリデーション呼び出し
     * @param rv 日報インスタンス
     * @return エラーのリスト
     */
    public static List<String> validate(ReportView rv) {
        List<String> errors = new ArrayList<String>();

        //タイトルチェック
        String titleError = validateTitle(rv.getTitle());
        if (!titleError.equals("")) {
            errors.add(titleError);
        }

        //内容チェック
        String contentError = validateContent(rv.getContent());
        if (!contentError.equals("")) {
            errors.add(contentError);
        }

        return errors;
    }

    /**
     * タイトルチェック
     * @param title
     * @return
     */
    private static String validateTitle(String title) {
        if (title == null || title.equals("")) {
            return MessageConst.E_NOTITLE.getMessage();
        }

        //入力値がある場合は空文字を返却
        return "";
    }

    /**
     * 内容チェック
     * @param content
     * @return
     */
    private static String validateContent(String content) {
        if (content == null || content.equals("")) {
            return MessageConst.E_NOCONTENT.getMessage();
        }

        //入力値がある場合は空文字を返却
        return "";
    }
}
