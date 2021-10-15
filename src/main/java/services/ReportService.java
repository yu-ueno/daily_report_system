package services;

import java.time.LocalDateTime;
import java.util.List;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import actions.views.ReportConverter;
import actions.views.ReportView;
import constants.JpaConst;
import models.Report;
import models.validators.ReportValidator;

public class ReportService extends ServiceBase {

    /**
     * 指定した従業員が作成した日報データを、指定されたページ数の一覧画面に表示する分取得
     * @param employee 従業員
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<ReportView> getMinePerPage(EmployeeView employee,int page){


        List<Report> reports = em.createNamedQuery(JpaConst.Q_REP_GET_ALL_MINE, Report.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .setFirstResult(JpaConst.ROW_PER_PAGE*(page-1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();

        return ReportConverter.toViewList(reports);

    }

    /**
     * 指定した従業員が作成した日報データの件数を取得
     * @param employee
     * @return 日報データの件数
     */
    public long countAllMine(EmployeeView employee) {

        long count = (long) em.createNamedQuery(JpaConst.Q_REP_COUNT_ALL_MINE, Long.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .getSingleResult();

        return count;
    }

    /**
     * 日報テーブルのデータの件数を取得
     * @return データの件数
     */
    public List<ReportView> getAllPerPage(int page) {

        List<Report> reports = em.createNamedQuery(JpaConst.Q_REP_GET_ALL, Report.class)
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        return ReportConverter.toViewList(reports);
    }

    /**
     * 日報テーブルデータの件数を取得
     * @return データの件数
     */
    public long countAll() {
        long reports_count = (long) em.createNamedQuery(JpaConst.Q_REP_COUNT, Long.class)
                .getSingleResult();
        return reports_count;
    }


    public ReportView findOne(int id) {
        return ReportConverter.toView(findOneInternal(id));
    }

    /**
     * 更新処理
     * @param rv 更新内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> update(ReportView rv) {

        List<String> errors = ReportValidator.validate(rv);

        if (errors.size() == 0) {

            //更新日時を現在時刻に設定
            LocalDateTime ldt = LocalDateTime.now();
            rv.setUpdatedAt(ldt);

            updateInternal(rv);
        }

        //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    private Report findOneInternal(int id) {
        return em.find(Report.class, id);
    }

    /**
     * 日報データ登録処理
     * @param rv 日報データ
     */
    private void createInternal(ReportView rv) {

        em.getTransaction().begin();
        em.persist(ReportConverter.toModel(rv));
        em.getTransaction().commit();

    }

    /**
     * 日報データ更新処理
     * @param rv 日報データ
     */
    private void updateInternal(ReportView rv) {

        em.getTransaction().begin();
        Report r = findOneInternal(rv.getId());
        ReportConverter.copyViewToModel(r, rv);
        em.getTransaction().commit();

    }

}
