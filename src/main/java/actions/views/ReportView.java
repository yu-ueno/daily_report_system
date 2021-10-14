package actions.views;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportView {

    //id
    private Integer id;

    //従業員
    private EmployeeView employee;

    //日付
    private LocalDate reportDate;

    //タイトル
    private String title;

    //内容
    private String content;

    //登録日時
    private LocalDateTime createdAt;

    //更新日時
    private LocalDateTime updatedAt;
}
